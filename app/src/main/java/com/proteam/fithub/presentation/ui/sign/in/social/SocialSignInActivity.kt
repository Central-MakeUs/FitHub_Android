package com.proteam.fithub.presentation.ui.sign.`in`.social

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivitySignInBinding
import com.proteam.fithub.presentation.util.LoadingDialog
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.sign.`in`.number.NumberSignInActivity
import com.proteam.fithub.presentation.ui.sign.`in`.social.viewmodel.SocialSignInViewModel
import com.proteam.fithub.presentation.ui.sign.up.social.SocialSignUpActivity
import com.proteam.fithub.presentation.util.CustomSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialSignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val viewModel: SocialSignInViewModel by viewModels()
    private var token : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        getFcmToken()
        initBinding()
    }

    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isSuccessful) {
                token = it.result
            }
        }
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
            user?.let {
                it.id?.let { it1 ->
                    token?.let { it2 ->
                        viewModel.sendSocialToken(it1, it2).also { showLoadingDialog() }
                    }
                }
            }
        }
        observeSignInState()
    }

    private fun observeSignInState() {
        viewModel.signInState.observe(this) {
            dismissLoadingDialog()
            when (it) {
                in 2000..3000 -> validateWhenSuccess(it)
                in 4000..5000 -> validateWhenServerError(it)
                0 -> return@observe
            }
            viewModel.initState()
        }
    }

    private fun validateWhenSuccess(code: Int) {
        when (code) {
            2004 -> onSuccessSignIn()
            2005 -> onNeedSignUp()
        }
    }

    private fun validateWhenServerError(code : Int) {
        CustomSnackBar.makeSnackBar(binding.root, code.toString())
    }

    private fun onSuccessSignIn() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    fun onPhoneSignInClicked() {
        /** 로그인 화면 **/
        this.requestProcessFinished.launch(Intent(this, NumberSignInActivity::class.java))
    }

    private fun onNeedSignUp() {
        /** 회원가입 화면 **/
        this.requestProcessFinished.launch(
            Intent(
                this,
                SocialSignUpActivity::class.java
            ).setType("Social")
        )
    }

    private val requestProcessFinished =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val state = it.data!!.extras?.getBoolean("state")
                if (state == true) {
                    finish()
                }
            }
        }

    private var loadingDialog = LoadingDialog()
    private fun showLoadingDialog() = loadingDialog.show(supportFragmentManager, null)
    private fun dismissLoadingDialog() = loadingDialog.dismiss()
}