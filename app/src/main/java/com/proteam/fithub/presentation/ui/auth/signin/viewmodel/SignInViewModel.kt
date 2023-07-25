package com.proteam.fithub.presentation.ui.auth.signin.viewmodel

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
    private val _signInState = MutableLiveData<Int>()
    val signInState : LiveData<Int> = _signInState

    fun sendKakaoToken(signatureID : Long) {
        viewModelScope.launch {
            signInRepo.signInWithKakao(RequestSignInKakao(signatureID.toString()))
                .onSuccess {
                    saveUserJWT(it.result.accessToken)
                    setState(it.code)
                }
        }
    }

    fun saveUserJWT(jwt : String) {
        viewModelScope.launch {
            signInRepo.saveUserData(null, jwt)
        }
    }

    fun setState(state : Int) {
        _signInState.value = state
    }
}