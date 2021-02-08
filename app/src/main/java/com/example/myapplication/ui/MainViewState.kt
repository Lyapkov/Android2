package com.example.myapplication.ui

import com.example.myapplication.model.Note

class MainViewState(val notes: List<Note>? = null, error: Throwable? = null) :
        BaseViewState<List<Note>?>(notes, error)