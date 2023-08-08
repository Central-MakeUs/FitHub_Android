package com.proteam.fithub.presentation.ui.main.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.response.ResponseMyPageData
import com.proteam.fithub.domain.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(private val myPageRepo : MyPageRepository): ViewModel() {

    private val _myPageData = MutableLiveData<ResponseMyPageData.ResultMyPageData>()
    val myPageData : LiveData<ResponseMyPageData.ResultMyPageData> = _myPageData

    fun requestMyPageData() {
        viewModelScope.launch {
            myPageRepo.requestMyPageData()
                .onSuccess { _myPageData.value = it }
        }
    }

}