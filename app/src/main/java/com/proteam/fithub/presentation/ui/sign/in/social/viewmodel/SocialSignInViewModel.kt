package com.proteam.fithub.presentation.ui.sign.`in`.social.viewmodel

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
class SocialSignInViewModel @Inject constructor(private val signInRepo : SignInRepository): ViewModel() {

    private val _signInState = MutableLiveData<Int>()
    val signInState : LiveData<Int> = _signInState

    fun sendSocialToken(signatureID : Long) {
        viewModelScope.launch {
            signInRepo.signInWithKakao(RequestSignInKakao(signatureID.toString()))
                .onSuccess {
                    //:TODO 명칭 획일화하기
                    saveUserJWT(it.result.jwt)
                    setState(it.code)
                }
        }
    }

    private fun saveUserJWT(jwt : String) {
        viewModelScope.launch {
            signInRepo.saveUserData(null, jwt)
        }
    }

    private fun setState(state : Int) {
        _signInState.value = state
    }

}