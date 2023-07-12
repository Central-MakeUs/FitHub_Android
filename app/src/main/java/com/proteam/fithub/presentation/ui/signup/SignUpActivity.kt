package com.proteam.fithub.presentation.ui.signup

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivitySignUpBinding
import com.proteam.fithub.presentation.ui.signup.agree.SignUpAgreementFragment
import com.proteam.fithub.presentation.ui.signup.social.SignUpSocialFragment
import com.proteam.fithub.presentation.ui.signup.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private val viewModel : SignUpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        initBinding()
        initViewType()

    }

    private fun initViewType() {
        intent.type?.let { viewModel.setType(it) }
        initFragments()
    }

    private fun initBinding() {
        binding.activity = this
    }

    private fun initFragments() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.sign_up_layout_container, if(viewModel.viewType.value == "Phone") SignUpAgreementFragment() else SignUpSocialFragment()).commit()
    }

    fun changeFragments(fragment : Fragment) {
        supportFragmentManager.beginTransaction().addToBackStack(fragment.id.toString()).add(R.id.sign_up_layout_container, fragment, "Sign_Up").commit()
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