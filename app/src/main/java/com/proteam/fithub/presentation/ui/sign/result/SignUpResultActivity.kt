package com.proteam.fithub.presentation.ui.sign.result

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.proteam.fithub.databinding.ActivitySignUpResultBinding
import com.proteam.fithub.presentation.ui.main.MainActivity

class SignUpResultActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
    }

    private fun initBinding() {
        binding.nickname = intent.type
    }

    fun onFinishClicked() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }
}