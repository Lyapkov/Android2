package com.example.myapplication.ui

import com.example.myapplication.model.Note

class NoteViewState(note: Note? = null, error: Throwable? = null) :
        BaseViewState<Note?>(note, error)