package com.example.myapplication.viewmodel

import com.example.myapplication.model.NoAuthException
import com.example.myapplication.model.Repository
import com.example.myapplication.ui.viewstate.SplashViewState

class SplashViewModel(private val repository: Repository = Repository) : BaseViewModel<Boolean?, SplashViewState>() {
    fun requestUser() {
        repository.getCurrentUser().observeForever { user ->
            viewStateLiveData.value = user?.let {
                SplashViewState(isAuth = true)
            } ?: SplashViewState(error = NoAuthException())
        }
    }
}