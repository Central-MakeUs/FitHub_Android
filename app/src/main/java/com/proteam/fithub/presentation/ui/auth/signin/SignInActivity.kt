package com.proteam.fithub.presentation.ui.auth.signin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivitySignInBinding
import com.proteam.fithub.presentation.component.ComponentAlertToast
import com.proteam.fithub.presentation.ui.auth.signin.viewmodel.SignInViewModel
import com.proteam.fithub.presentation.ui.auth.signinphone.SignInWithPhoneNumberActivity
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignInBinding
    private val viewModel : SignInViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        initBinding()
        observeSignInState()
    }

    private fun initBinding() {
        binding.activity = this
    }


    fun onKakaoSignInClicked() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            signInWithKakaoTalk()
        } else {
            signInWithKakaoAccount()
        }
    }
    private fun signInWithKakaoTalk() {
        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
            if (error != null) {
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    finish()
                    return@loginWithKakaoTalk
                }
                signInWithKakaoAccount()
            } else if (token != null) {
                getKakaoSignatureID()
            }
        }
    }

    private fun signInWithKakaoAccount() {
        UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoAccountSignInCallback)
    }

    private val kakaoAccountSignInCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            finish()
        } else if (token != null) {
            getKakaoSignatureID()
        }
    }

    private fun getKakaoSignatureID() {
        UserApiClient.instance.me { user, error ->
            user?.let { it.id?.let { it1 -> viewModel.sendKakaoToken(it1) } }
        }
    }

    private fun observeSignInState() {
        viewModel.signInState.observe(this) {
            if(it == 2004) {
                openMainActivity()
            } else if(it == 2005) {
                openSocialSignUp()
            }
        }
    }
    
    fun onPhoneSignInClicked() {
        /** 로그인 화면 **/
        this.requestProcessFinished.launch(Intent(this, SignInWithPhoneNumberActivity::class.java))
    }

    private fun openSocialSignUp() {
        /** 회원가입 화면 **/
        this.requestProcessFinished.launch(Intent(this, SignUpActivity::class.java).setType("Social"))
    }

    private fun openMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private val requestProcessFinished =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val state = it.data!!.extras?.get("state") as Boolean
                if(state) {
                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }
}