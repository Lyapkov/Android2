package com.example.myapplication.ui.viewstate

import com.example.myapplication.model.Note
import com.example.myapplication.ui.viewstate.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null) :
        BaseViewState<Note?>(note, error)