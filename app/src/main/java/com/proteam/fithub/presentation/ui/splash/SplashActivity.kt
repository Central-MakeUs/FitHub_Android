package com.proteam.fithub.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.sign.`in`.social.SocialSignInActivity
import com.proteam.fithub.presentation.ui.splash.viewModel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity(){
    private val viewModel : SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        observeAuthSignIn()
    }

    private fun observeAuthSignIn() {
        viewModel.statusCode.observe(this) {
            when(it) {
                2008 -> startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                else -> startActivity(Intent(this, SocialSignInActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
            }
            finish()
        }
    }
}