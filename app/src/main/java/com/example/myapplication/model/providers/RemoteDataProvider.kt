package com.example.myapplication.model.providers

import androidx.lifecycle.LiveData
import com.example.myapplication.model.Note
import com.example.myapplication.model.NoteResult
import com.example.myapplication.model.User
import kotlinx.coroutines.channels.ReceiveChannel

interface RemoteDataProvider {

    suspend fun subscribeToAllNotes(): ReceiveChannel<NoteResult>
    suspend fun getNoteById(id: String): Note
    suspend fun saveNote(note: Note) : Note
    suspend fun getCurrentUser(): User?
    suspend fun deleteNote(noteId: String): Note?
}