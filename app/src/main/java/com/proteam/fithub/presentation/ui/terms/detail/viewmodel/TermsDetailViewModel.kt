package com.proteam.fithub.presentation.ui.terms.detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.response.ResponseTermsData
import com.proteam.fithub.domain.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermsDetailViewModel @Inject constructor(private val myPageRepository: MyPageRepository) :
    ViewModel() {

    private val _termsData = MutableLiveData<ResponseTermsData.ResultTermsData>()
    val termsData : LiveData<ResponseTermsData.ResultTermsData> = _termsData

    fun requestTermsData(position: Int) {
        viewModelScope.launch {
            myPageRepository.requestTermsData(position)
                .onSuccess { _termsData.value = it }
        }
    }
}