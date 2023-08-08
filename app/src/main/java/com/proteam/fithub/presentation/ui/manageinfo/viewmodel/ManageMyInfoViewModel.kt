package com.proteam.fithub.presentation.ui.manageinfo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.response.ResponseMyInfoData
import com.proteam.fithub.data.remote.response.ResponseMyPageData
import com.proteam.fithub.domain.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageMyInfoViewModel @Inject constructor(private val myPageRepository: MyPageRepository): ViewModel() {
    private val _myInfoData = MutableLiveData<ResponseMyInfoData.ResultMyInfoData>()
    val myInfoData : LiveData<ResponseMyInfoData.ResultMyInfoData> = _myInfoData

    init {
        requestMyInfoData()
    }

    private fun requestMyInfoData() {
        viewModelScope.launch {
            myPageRepository.requestMyInfoData()
                .onSuccess { _myInfoData.value = it
                    Log.e("----", "requestMyInfoData: $it", )}
        }
    }
}