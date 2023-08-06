package com.proteam.fithub.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.sign.`in`.social.SocialSignInActivity
import com.proteam.fithub.presentation.ui.splash.viewModel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity(){
    private val viewModel : SplashViewModel by viewModels()

    private var view : String = ""
    private var pk : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
//        installSplashScreen()
        super.onCreate(savedInstanceState)

        intent.extras?.let {
            view = it.getString("View", null)
            pk = it.getString("PK", null)
        }

        getFCMToken()
        observeAuthSignIn()
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.e("----", "getFCMToken: ${task.result}")
            }
        }
    }

    private fun observeAuthSignIn() {
        viewModel.statusCode.observe(this) {
            when(it) {
                2008 -> {
                    val intent = Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    intent.putExtra("View", view)
                    intent.putExtra("PK", pk)
                    startActivity(intent)
                }
                else -> startActivity(Intent(this, SocialSignInActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
            }
            finish()
        }
    }
}