package com.proteam.fithub.presentation.ui.sign.up.common.authcode.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.domain.repository.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthCodeViewModel @Inject constructor(private val signUpRepository: SignUpRepository): ViewModel() {

    private val _authResult = MutableLiveData<Int>()
    val authResult: LiveData<Int> = _authResult


    fun requestSMSAuthCode(phoneNumber: String) {
        viewModelScope.launch {
            signUpRepository.requestSMSAuth(RequestSMSAuth(phoneNumber))
        }
    }

    fun requestCheckSMSAuthCode(phoneNumber : String, userAuthCode: String) {
        viewModelScope.launch {
            signUpRepository.requestCheckSMSAuth(RequestCheckSMSAuth(phoneNumber, userAuthCode.toInt()))
                .onSuccess { _authResult.value = it.code }
                .onFailure { _authResult.value = it.message?.toInt() }
        }
        initAuthResult()
    }

    fun initAuthResult() {
        _authResult.value = 0
    }

}