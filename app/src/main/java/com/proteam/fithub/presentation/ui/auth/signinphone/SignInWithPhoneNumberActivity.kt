package com.proteam.fithub.presentation.ui.auth.signinphone

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivitySigninWithPhoneNumberBinding
import com.proteam.fithub.presentation.component.ComponentAlertToast
import com.proteam.fithub.presentation.component.ComponentDialogYesNo
import com.proteam.fithub.presentation.ui.auth.signin.SignInActivity
import com.proteam.fithub.presentation.ui.auth.signinphone.viewmodel.SignInWithPhoneNumberViewModel
import com.proteam.fithub.presentation.ui.findpassword.FindPasswordActivity
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent

@AndroidEntryPoint
class SignInWithPhoneNumberActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySigninWithPhoneNumberBinding
    private val viewModel : SignInWithPhoneNumberViewModel by viewModels()
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
        binding.signInWithPhoneLayoutPhoneNumber.isFinished.observe(this) {
            binding.signInWithPhoneBtnSignIn.isEnabled = it && binding.signInWithPhoneLayoutPassword.isFinished.value == true
        }
        binding.signInWithPhoneLayoutPassword.isFinished.observe(this) {
            binding.signInWithPhoneBtnSignIn.isEnabled = it && binding.signInWithPhoneLayoutPhoneNumber.isFinished.value == true
        }
    }

    fun onSignInClicked() {
        viewModel.requestSignIn(binding.signInWithPhoneLayoutPhoneNumber.returnUserInputContent(), binding.signInWithPhoneLayoutPassword.returnUserInputContent())
        observeState()
    }

    private fun observeState() {
        viewModel.signInState.observe(this) {
            when(it) {
                "SUCCESS" -> onSignInSuccess()
                "NO_USER_INFO" -> showNotFoundDialog()
                "INVALIDATE_PASSWORD" -> showAlertToast()
            }
        }
    }

    private val requestProcessFinished =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val state = it.data!!.extras?.get("state") as Boolean
                if(state) {
                    setResult(RESULT_OK, Intent(this, SignInActivity::class.java).putExtra("state", true))
                    finish()
                }
            }
        }

    fun onBackPress() {
        finish()
    }

    private fun showAlertToast() {
        ComponentAlertToast().show(supportFragmentManager, "4020")
    }

    private fun onSignInSuccess() {
        this.setResult(RESULT_OK, Intent(this, SignInActivity::class.java).putExtra("state", true))
        finish()
    }

    private fun showNotFoundDialog() {
        ComponentDialogYesNo(::onSignUpClicked).show(supportFragmentManager, "NO_USER_INFO")
    }

    fun onFindPasswordClicked() {
        startActivity(Intent(this, FindPasswordActivity::class.java))
    }

    fun onSignUpClicked() {
        this.requestProcessFinished.launch(Intent(this, SignUpActivity::class.java).setType("Phone"))
    }
}