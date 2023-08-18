package com.proteam.fithub.presentation.ui.change_password.check_password.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.request.RequestPasswordExist
import com.proteam.fithub.domain.repository.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckPasswordViewModel @Inject constructor(private val repository : SignUpRepository): ViewModel() {

    private val _passwordState = MutableLiveData<Int>()
    val passwordState : LiveData<Int> = _passwordState

    fun requestCheckExistPassword(password : String) {
        viewModelScope.launch {
            repository.requestExistPassword(RequestPasswordExist(password))
                .onSuccess { _passwordState.value = it.code }
                .onFailure { _passwordState.value = it.message?.toInt() }
        }
    }

    fun initState() {
        _passwordState.value = 0
    }
}