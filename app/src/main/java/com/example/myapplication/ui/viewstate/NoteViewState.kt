package com.example.myapplication.ui.viewstate

import com.example.myapplication.model.Note
import com.example.myapplication.ui.viewstate.BaseViewState

class NoteViewState(data: Data = Data(), error: Throwable? = null) :
        BaseViewState<NoteViewState.Data>(data, error) {
    data class Data(val isDeleted: Boolean = false, val note: Note? = null)
}