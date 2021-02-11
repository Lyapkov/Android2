package com.example.myapplication.model

import com.example.myapplication.model.providers.FireStoreProvider
import com.example.myapplication.model.providers.RemoteDataProvider

class Repository(private val remoteDataProvider: RemoteDataProvider) {

    suspend fun getNotes() = remoteDataProvider.subscribeToAllNotes()
    suspend fun saveNote(note: Note) = remoteDataProvider.saveNote(note)
    suspend fun getNoteById(id: String) = remoteDataProvider.getNoteById(id)
    suspend fun getCurrentUser() = remoteDataProvider.getCurrentUser()
    suspend fun deleteNote(noteId: String) = remoteDataProvider.deleteNote(noteId)
}