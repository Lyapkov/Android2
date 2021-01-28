package com.example.myapplication.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewbinding.ViewBinding
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.Note
import com.example.myapplication.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    //lateinit var ui: MainViewState
    override val viewModel: MainViewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }
    override val layoutRes: Int = com.example.myapplication.R.layout.activity_main
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ui = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(toolbar)

        adapter = MainAdapter(object : OnItemClickListener {
            override fun onItemClick(note: Note) {
                openNoteScreen(note)
            }
        })
        mainRecycler.adapter = adapter

        fab.setOnClickListener{openNoteScreen()}
    }

    private fun openNoteScreen(note: Note? = null) {
        startActivity(NoteActivity.getStartIntent(this, note?.id))
    }

    override fun renderData(data: List<Note>?) {
        if (data == null) return
        adapter.notes = data
    }
}