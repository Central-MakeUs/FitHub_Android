package com.proteam.fithub.presentation.ui.auth.signinphone.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.request.RequestSignInPhone
import com.proteam.fithub.data.remote.response.ResponseSignIn
import com.proteam.fithub.domain.repository.SignInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInWithPhoneNumberViewModel @Inject constructor(
    private val signInRepository: SignInRepository
): ViewModel() {

    private val _signInState = MutableLiveData<String>()
    val signInState : LiveData<String> = _signInState

    fun requestSignIn(number : String, password : String) {
        viewModelScope.launch {
            signInRepository.signInWithPhone(RequestSignInPhone(number, password))
                .onSuccess {
                    validateCode(it.code)
                    saveUserData(it.result)
                }
                .onFailure { validateCode(it.message!!.toInt()) }
        }
    }

    private fun validateCode(code : Int) {
        _signInState.value = when(code) {
            2000 -> "SUCCESS"
            4019 -> "NO_USER_INFO"
            4020 -> "INVALIDATE_PASSWORD"
            else -> "ERROR"
        }
    }

    private fun saveUserData(result : ResponseSignIn.ResultSignIn) {
        viewModelScope.launch {
            signInRepository.saveUserData(result.userId, result.accessToken)
        }
    }

}