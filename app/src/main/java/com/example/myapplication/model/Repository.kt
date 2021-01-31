package com.example.myapplication.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

object Repository {

    private val notesLiveData = MutableLiveData<List<Note>>()

    private val notes: MutableList<Note> = mutableListOf(
            Note(
                    UUID.randomUUID().toString(),
                    "Первая",
                    "1111111",
                    Color.WHITE
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Вторая",
                    "2222222",
                    Color.BLUE
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Третья",
                    "333333",
                    Color.GREEN
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Четвертая",
                    "444444",
                    Color.PINK
            ),
            Note(
                    UUID.randomUUID().toString(),
                    "Пятая",
                    "555555",
                    Color.RED
            )
    )

    init {
        notesLiveData.value = notes
    }

    fun getNotes(): LiveData<List<Note>> = notesLiveData

    fun saveNote(note: Note) {
        addOrReplace(note)
        notesLiveData.value = notes
    }

    private fun addOrReplace(note: Note) {
        for (i in 0 until notes.size) {
            if (notes[i] == note) {
                notes[i] = note
                return
            }
        }

        notes.add(note)
    }
}