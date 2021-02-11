package com.example.myapplication.viewmodel

import com.example.myapplication.model.NoAuthException
import com.example.myapplication.model.Repository
import com.example.myapplication.ui.viewstate.SplashViewState
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: Repository) : BaseViewModel<Boolean>() {
    fun requestUser() {
        launch {
            repository.getCurrentUser()?.let { user ->
                setData(true)
            } ?: setError(NoAuthException())
        }
    }
}
