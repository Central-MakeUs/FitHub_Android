package com.proteam.fithub.presentation.ui.auth.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.request.RequestSignInKakao
import com.proteam.fithub.domain.repository.SignInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val signInRepo : SignInRepository,
): ViewModel() {
    private val _signInState = MutableLiveData<String>()
    val signInState : LiveData<String> = _signInState

    fun sendKakaoToken(signatureID : Long) {
        viewModelScope.launch {
            signInRepo.signInWithKakao(RequestSignInKakao("KAKAO$signatureID"))
                .onSuccess {
                    saveUserJWT(it.accessToken)
                    setState("성공")
                }
                .onFailure { setState("카카오 로그인에 실패했습니다.") }
        }
    }

    fun saveUserJWT(jwt : String) {
        viewModelScope.launch {
            signInRepo.saveAccessToken(jwt)
        }
    }

    fun setState(state : String) {
        _signInState.value = state
    }
}