package com.proteam.fithub.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivitySigninWithPhoneNumberBinding
import com.proteam.fithub.presentation.ui.signup.SignUpActivity

class SignInWithPhoneNumberActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySigninWithPhoneNumberBinding
    private val TAG = "----"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_signin_with_phone_number)
        initBinding()
        initInclude()
    }

    private fun initBinding() {
        binding.activity = this
    }

    private fun initInclude() {
        binding.signInWithPhoneLayoutPhoneNumber.getAttr("phone")
        binding.signInWithPhoneLayoutPassword.getAttr("password")

        binding.signInWithPhoneLayoutPhoneNumber.status.observe(this) {phone ->
            binding.signInWithPhoneLayoutPassword.status.observe(this) {pw ->
                binding.signInWithPhoneBtnSignIn.isEnabled = phone && pw
            }
        }
    }

    fun onSignInClicked() {
        // TODO: ID PW 받아온 후 로그인 API 연결하기 
        Log.d(TAG, "onSignInClicked: ${binding.signInWithPhoneLayoutPhoneNumber.returnInput()} / ${binding.signInWithPhoneLayoutPassword.returnInput()}")
    }

    fun onSignUpClicked() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }
}