package com.proteam.fithub.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.messaging.FirebaseMessaging
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivitySplashBinding
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.onboarding.OnBoardingActivity
import com.proteam.fithub.presentation.ui.sign.`in`.social.SocialSignInActivity
import com.proteam.fithub.presentation.ui.splash.viewModel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    //private lateinit var binding : ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        observeAuthSignIn()
    }

    private fun observeAuthSignIn() {
        viewModel.statusCode.observe(this) {

            when (it) {
                2008 -> {
                    val intent = Intent(
                        this,
                        MainActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    intent.putExtra("View", getIntent().extras?.getString("View", null))
                    intent.putExtra("PK", getIntent().extras?.getString("PK", null))
                    intent.putExtra("AlarmPK", getIntent().extras?.getString("AlarmPK", null))
                    startActivity(intent)
                }

                else -> startActivity(
                    Intent(
                        this,
                        OnBoardingActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                )
            }
            finish()
        }
    }
}