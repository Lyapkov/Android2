package com.example.myapplication.ui.viewstate

import com.example.myapplication.model.Note
import com.example.myapplication.ui.viewstate.BaseViewState

class MainViewState(val notes: List<Note>? = null, error: Throwable? = null) :
        BaseViewState<List<Note>?>(notes, error)