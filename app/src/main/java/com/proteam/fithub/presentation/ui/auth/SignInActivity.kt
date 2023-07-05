package com.proteam.fithub.presentation.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivitySignInBinding
import com.proteam.fithub.presentation.ui.auth.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignInBinding
    private val viewModel : SignInViewModel by viewModels()
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
    fun signInWithKakaoTalk() {
        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
            if (error != null) {
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    /** 유저의 의도적인 로그인 취소의 경우 앱 끄기 **/
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
    
    fun onPhoneSignInClicked() {
        // TODO: 추후 Result를 받아와, 현재 페이지를 Finish할지 여부를 판단해야 할 듯 하다. 
        startActivity(Intent(this, SignInWithPhoneNumberActivity::class.java))
    }
}