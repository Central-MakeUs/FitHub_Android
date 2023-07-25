package com.proteam.fithub.presentation.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivitySignUpBinding
import com.proteam.fithub.presentation.ui.auth.signinphone.SignInWithPhoneNumberActivity
import com.proteam.fithub.presentation.ui.sign.up.common.agreement.AgreementFragment
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
        binding.lifecycleOwner = this
    }

    private fun initFragments() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.sign_up_layout_container, AgreementFragment(), viewModel.viewType.value).commit()
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

    fun onFinishSignUp() {
        setResult(RESULT_OK, Intent(this, SignInWithPhoneNumberActivity::class.java).putExtra("state", true))
        finish()
    }
}