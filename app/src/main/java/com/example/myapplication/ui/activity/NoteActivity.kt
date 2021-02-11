package com.example.myapplication.ui.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityNoteBinding
import com.example.myapplication.model.Color
import com.example.myapplication.model.Note
import com.example.myapplication.ui.format
import com.example.myapplication.ui.getColorInt
import com.example.myapplication.ui.viewstate.NoteViewState
import com.example.myapplication.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.item_note.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

private const val SAVE_DELAY = 2L

class NoteActivity : BaseActivity<NoteViewState.Data>() {

    companion object {
        const val EXTRA_NOTE = "NoteActivity.extra.NOTE"

        fun getStartIntent(context: Context, noteId: String?): Intent {
            var intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, noteId)
            return intent
        }
    }

    private var note: Note? = null
    private var color: Color = Color.PINK
    override val ui: ActivityNoteBinding by lazy { ActivityNoteBinding.inflate(layoutInflater) }
    override val viewModel: NoteViewModel by viewModel()
    override val layoutRes: Int = R.layout.activity_note
    private val textChangeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            triggerSaveNote()
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // not used
        }

        override fun afterTextChanged(s: Editable?) {
            // not used
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val noteId = intent.getStringExtra(EXTRA_NOTE)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteId?.let {
            viewModel.loadNote(it)
        }

        if (noteId == null) supportActionBar?.title = getString(R.string.new_note_title)

        ui.colorPicker.onColorClickListener = {
            color = it
            setToolbarColor(it)
            triggerSaveNote()
        }

        setEditListener()
    }

    private fun initView() {
        note?.run {
            toolbar.setBackgroundColor(color.getColorInt(this@NoteActivity))
            removeEditListener()
            if (title != ui.titleEt.text.toString())
                ui.titleEt.setText(title)
            if (note != ui.bodyEt.text.toString())
                ui.bodyEt.setText(note)
            setEditListener()
            supportActionBar?.title = lastChanged.format()
            setToolbarColor(color)
        }
        titleEt.addTextChangedListener(textChangeListener)
        bodyEt.addTextChangedListener(textChangeListener)
    }

    private fun setToolbarColor(color: Color) {
        ui.toolbar.setBackgroundColor(color.getColorInt(this@NoteActivity))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
            menuInflater.inflate(R.menu.menu_note, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> super.onBackPressed().let { true }
        //R.id.palette -> togglePalette().let { true }
        R.id.delete -> deleteNote().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun togglePalette() {
        if (ui.colorPicker.isOpen)
            ui.colorPicker.close()
        else ui.colorPicker.open()
    }

    private fun deleteNote() {
        AlertDialog.Builder(this)
                .setMessage(R.string.delete_dialog_message)
                .setNegativeButton(R.string.cancel_btn_title) { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(R.string.ok_btn_title) { _, _ -> viewModel.deleteNote() }
                .show()
    }

    private fun triggerSaveNote() {
        if (titleEt.text == null || titleEt.text!!.length < 3) return

        launch {
            delay(SAVE_DELAY)
            note = note?.copy(
                    title = titleEt.text.toString(),
                    note = bodyEt.text.toString(),
                    color = color,
                    lastChanged = Date()
            ) ?: Note.createNewNote(titleEt.text.toString(), bodyEt.text.toString())

            if (note != null) viewModel.saveChanges(note!!)
        }
    }

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) finish()

        this.note = data.note
        data.note?.let { color = it.color }
        initView()
    }

    private fun setEditListener() {
        titleEt.addTextChangedListener(textChangeListener)
        bodyEt.addTextChangedListener(textChangeListener)
    }

    private fun removeEditListener() {
        titleEt.removeTextChangedListener(textChangeListener)
        bodyEt.removeTextChangedListener(textChangeListener)
    }

    override fun onBackPressed() {
        if (ui.colorPicker.isOpen) {
            ui.colorPicker.close()
            return
        }
        super.onBackPressed()
    }
}