package com.example.myapplication.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivitySplashBinding
import com.example.myapplication.model.NoAuthException
import com.example.myapplication.model.providers.FireStoreProvider
import com.example.myapplication.ui.viewstate.SplashViewState
import com.example.myapplication.viewmodel.SplashViewModel
import com.firebase.ui.auth.AuthUI
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

private const val RC_SIGN_IN = 42
private const val START_DELAY = 1000L

class SplashActivity : BaseActivity<Boolean>() {

    override val viewModel: SplashViewModel by viewModel()

    val provider: FireStoreProvider by inject()

    override val ui: ActivitySplashBinding
            by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    override val layoutRes: Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({ viewModel.requestUser() }, START_DELAY)
    }

    override fun renderError(error: Throwable) {
        when (error) {
            is NoAuthException -> startLoginActivity()
            else -> error?.message?.let { showError(it) }
        }
    }

    override fun renderData(data: Boolean) {
        if (data) startMainActivity()
    }

    private fun startLoginActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.common_google_signin_btn_icon_dark_focused)
                        .setTheme(R.style.LoginStyle)
                        .setAvailableProviders(listOf(
                                AuthUI.IdpConfig.EmailBuilder().build(),
                                AuthUI.IdpConfig.GoogleBuilder().build()
                        ))
                        .build(),
                RC_SIGN_IN
        )
    }

    private fun startMainActivity() {
        startActivity(MainActivity.getStartIntent(this))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK)
            finish()
        else
            super.onActivityResult(requestCode, resultCode, data)
    }
}