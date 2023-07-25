package com.proteam.fithub.presentation.ui.sign.`in`.social

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.proteam.fithub.presentation.ui.auth.signinphone.SignInWithPhoneNumberActivity
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.sign.`in`.social.viewmodel.SocialSignInViewModel
import com.proteam.fithub.presentation.ui.sign.up.social.SocialSignUpActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialSignInActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignInBinding
    private val viewModel : SocialSignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        initBinding()
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
            user?.let { it.id?.let { it1 -> viewModel.sendSocialToken(it1) } }
        }
        observeSignInState()
    }

    private fun observeSignInState() {
        viewModel.signInState.observe(this) {
            if(it == 2004) {
                onSuccessSignIn()
            } else if(it == 2005) {
                onNeedSignUp()
            }
        }
    }

    private fun onSuccessSignIn() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun onPhoneSignInClicked() {
        /** 로그인 화면 **/
        this.requestProcessFinished.launch(Intent(this, SignInWithPhoneNumberActivity::class.java))
    }

    private fun onNeedSignUp() {
        /** 회원가입 화면 **/
        this.requestProcessFinished.launch(Intent(this, SocialSignUpActivity::class.java).setType("Social"))
    }

    private val requestProcessFinished =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val state = it.data!!.extras?.getBoolean("state")
                if(state == true) {
                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }
}