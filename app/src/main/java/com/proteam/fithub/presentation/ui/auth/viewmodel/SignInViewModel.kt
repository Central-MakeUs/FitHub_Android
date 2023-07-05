package com.proteam.fithub.presentation.ui.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

class SignInViewModel : ViewModel() {

    fun sendKakaoToken(signatureID : Long) {
        Log.d("----", "sendKakaoToken: $signatureID")
    }
}