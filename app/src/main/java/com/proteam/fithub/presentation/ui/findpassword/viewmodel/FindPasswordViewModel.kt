package com.proteam.fithub.presentation.ui.findpassword.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.request.RequestChangePassword
import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestPhoneNumberAvailable
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.domain.repository.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindPasswordViewModel @Inject constructor(private val signUpRepository: SignUpRepository) :
    ViewModel() {
    private val _userPhoneNumberAvailable = MutableLiveData<Boolean>()
    val userPhoneNumberAvailable : LiveData<Boolean> = _userPhoneNumberAvailable

    private val _authResult = MutableLiveData<Int>()
    val authResult: LiveData<Int> = _authResult

    private val userInputPhoneNumber = MutableLiveData<String>()

    private val _passwordResult = MutableLiveData<Int>()
    val passwordResult : LiveData<Int> = _passwordResult

    /** init **/
    fun initAuthResult() {
        _authResult.value = 0
    }


    /** Set User Data **/

    fun setUserPhoneNumber(number : String) {
        userInputPhoneNumber.value = number
    }

    /** Request **/
    fun requestUserNumberAvailable() {
        viewModelScope.launch {
            signUpRepository.requestUserNumberAvailable(RequestPhoneNumberAvailable(userInputPhoneNumber.value!!))
                .onSuccess { _userPhoneNumberAvailable.value = true }
                .onFailure { _userPhoneNumberAvailable.value = false }
        }
        initAuthResult()
    }

    fun requestSMSAuthCode() {
        viewModelScope.launch {
            signUpRepository.requestSMSAuth(RequestSMSAuth(userInputPhoneNumber.value!!))
                .onSuccess { }
        }
    }

    fun requestCheckSMSAuthCode(userAuthCode: String) {
        viewModelScope.launch {
            signUpRepository.requestCheckSMSAuth(RequestCheckSMSAuth(userInputPhoneNumber.value!!, userAuthCode.toInt()))
                .onSuccess { _authResult.value = it.code }
                .onFailure { _authResult.value = it.message?.toInt() }
        }
    }

    fun requestChangePassword(password : String) {
        viewModelScope.launch {
            signUpRepository.requestChangePassword(RequestChangePassword(userInputPhoneNumber.value!!, password))
                .onSuccess { _passwordResult.value = it.code }
                .onFailure { _passwordResult.value = it.message?.toInt() }
        }

    }


}