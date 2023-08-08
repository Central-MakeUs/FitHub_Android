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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            Log.e("----", "observeAuthSignIn: $it", )
            val intent = Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.putExtra("View", getIntent().extras?.getString("View", null))
            intent.putExtra("PK", getIntent().extras?.getString("PK", null))
            intent.putExtra("AlarmPK", getIntent().extras?.getString("AlarmPK", null))
            startActivity(intent)
            /*when(it) {
                2008 -> {

                }
                else -> startActivity(Intent(this, SocialSignInActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
            }*/
            finish()
        }
    }
}