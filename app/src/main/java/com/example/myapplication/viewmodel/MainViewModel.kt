package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Repository
import com.example.myapplication.ui.MainViewState

class MainViewModel: ViewModel() {

    private val viewStateLifeData: MutableLiveData<MainViewState> = MutableLiveData()

    init {
        viewStateLifeData.value = MainViewState(Repository.getNotes())
    }

    fun viewState(): LiveData<MainViewState> = viewStateLifeData
}