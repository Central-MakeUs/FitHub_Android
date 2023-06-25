package com.proteam.fithub.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivityMainBinding
import com.proteam.fithub.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        initBinding()


    }

    private fun initBinding() {
        binding.activity = this
    }
    
    fun onPhoneSignInClicked() {
        // TODO: 추후 Result를 받아와, 현재 페이지를 Finish할지 여부를 판단해야 할 듯 하다. 
        startActivity(Intent(this, SignInWithPhoneNumberActivity::class.java))
    }
}