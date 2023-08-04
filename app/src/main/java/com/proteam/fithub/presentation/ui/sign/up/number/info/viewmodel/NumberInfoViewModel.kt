package com.proteam.fithub.presentation.ui.sign.up.number.info.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestPhoneNumberAvailable
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.domain.repository.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NumberInfoViewModel @Inject constructor(private val signUpRepository: SignUpRepository): ViewModel() {

    private val _phoneAvailableState = MutableLiveData<Int>()
    val phoneAvailableState : LiveData<Int> = _phoneAvailableState

    /** Set Data **/



    /** Request **/

    fun requestExistPhone(phoneNumber: String) {
        viewModelScope.launch {
            signUpRepository.requestExistPhone(0, RequestPhoneNumberAvailable(phoneNumber))
                .onSuccess { _phoneAvailableState.value = it.code }
                .onFailure { _phoneAvailableState.value = it.message?.toInt() }
        }
    }
}