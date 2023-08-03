package com.proteam.fithub.presentation.ui.sign.`in`.number

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivitySignInWithNumberBinding
import com.proteam.fithub.presentation.component.ComponentAlertToast
import com.proteam.fithub.presentation.component.ComponentDialogYesNo
import com.proteam.fithub.presentation.ui.findpassword.FindPasswordActivity
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.sign.`in`.number.viewmodel.NumberSignInViewModel
import com.proteam.fithub.presentation.ui.sign.`in`.social.SocialSignInActivity
import com.proteam.fithub.presentation.ui.sign.up.number.NumberSignUpActivity
import com.proteam.fithub.presentation.util.CustomSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NumberSignInActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignInWithNumberBinding
    private val viewModel : NumberSignInViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in_with_number)

        initBinding()
        initUi()
    }

    private fun initBinding() {
        binding.activity = this
        binding.lifecycleOwner = this
    }

    private fun initUi() {
        initComponent()
    }

    private fun initComponent() {
        binding.signInWithPhoneLayoutPhoneNumber.setErrorEnable(false)
        binding.signInWithPhoneLayoutPassword.setErrorEnable(false, false)
    }

    fun onSignInClicked() {
        viewModel.requestSignIn(binding.signInWithPhoneLayoutPhoneNumber.returnUserInputContent(), binding.signInWithPhoneLayoutPassword.returnUserInputContent())
        observeState()
    }

    private fun observeState() {
        viewModel.signInState.observe(this) {
            when(it) {
                0 -> return@observe
                2000 -> onSignInSuccess()
                4019 -> ComponentDialogYesNo(::onSignUpClicked).show(supportFragmentManager, "NO_USER_INFO")
                4020 -> CustomSnackBar.makeSnackBar(binding.root, it.toString())
            }
            viewModel.initState()
        }
    }

    /** 회원가입 콜백 **/
    private val requestProcessFinished =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val state = it.data!!.extras?.getBoolean("state")
                if(state == true) {
                    onSignUpSuccess()
                }
            }
        }

    fun onBackPress() {
        finish()
    }

    private fun onSignInSuccess() {
        this.setResult(RESULT_OK, Intent(this, SocialSignInActivity::class.java).putExtra("state", true))
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun onSignUpSuccess() {
        this.setResult(RESULT_OK, Intent(this, SocialSignInActivity::class.java).putExtra("state", true))
        finish()
    }

    fun onFindPasswordClicked() {
        startActivity(Intent(this, FindPasswordActivity::class.java))
    }

    fun onSignUpClicked() {
        this.requestProcessFinished.launch(Intent(this, NumberSignUpActivity::class.java))
    }

    fun phoneBinding() = binding.signInWithPhoneLayoutPhoneNumber
    fun passwordBinding() = binding.signInWithPhoneLayoutPassword
}