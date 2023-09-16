package com.proteam.fithub.presentation.ui.splash.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.domain.repository.SignInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val signInRepository: SignInRepository) :
    ViewModel() {
    private val _statusCode = MutableLiveData<Int>()
    val statusCode: LiveData<Int> = _statusCode

    init {
        requestAutoSignIn()
    }

    private fun requestAutoSignIn() {
        viewModelScope.launch {
            signInRepository.autoSignIn()
                .onSuccess { _statusCode.value = it.code }
                .onFailure { _statusCode.value = it.message?.toInt() }
        }
    }
}