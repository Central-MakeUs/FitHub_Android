package com.proteam.fithub.presentation.ui.sign.`in`.number.viewmodel

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
class NumberSignInViewModel @Inject constructor(private val signInRepository: SignInRepository): ViewModel() {
    private val _signInState = MutableLiveData<Int>()
    val signInState : LiveData<Int> = _signInState

    fun requestSignIn(number : String, password : String, token : String) {
        Log.e("----", "requestSignIn: $token", )
        viewModelScope.launch {
            signInRepository.signInWithPhone(RequestSignInPhone(number, password, token))
                .onSuccess {
                    _signInState.value = it.code
                    saveUserData(it.result)
                }
                .onFailure { _signInState.value = it.message?.toInt() }
        }
    }

    private fun saveUserData(result : ResponseSignIn.ResultSignIn) {
        viewModelScope.launch {
            signInRepository.saveUserData(result.userId, result.accessToken)
        }
    }

    fun initState() {
        _signInState.value = 0
    }

}