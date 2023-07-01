package com.proteam.fithub.presentation.ui.signup

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivitySignUpBinding
import com.proteam.fithub.presentation.ui.signup.agree.SignUpAgreementFragment
import com.proteam.fithub.presentation.ui.signup.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private val viewModel : SignUpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        initBinding()
        initFragments()
    }

    private fun initBinding() {
        binding.activity = this
    }

    private fun initFragments() {
        supportFragmentManager.beginTransaction().replace(R.id.sign_up_layout_container, SignUpAgreementFragment()).commit()
    }

    fun onBackPress() {
        if(supportFragmentManager.backStackEntryCount == 0) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction().commit()
        }
    }
}