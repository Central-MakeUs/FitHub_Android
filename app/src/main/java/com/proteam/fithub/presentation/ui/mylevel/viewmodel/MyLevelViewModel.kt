package com.proteam.fithub.presentation.ui.mylevel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.response.ResponseMyLevelData
import com.proteam.fithub.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyLevelViewModel @Inject constructor(private val homeRepository: HomeRepository): ViewModel() {
    private val _myLevelData = MutableLiveData<ResponseMyLevelData.ResultMyLevelData>()
    val myLevelData : LiveData<ResponseMyLevelData.ResultMyLevelData> = _myLevelData

    init {
        requestLevelData()
    }

    private fun requestLevelData() {
        viewModelScope.launch {
            homeRepository.requestMyLevelData()
                .onSuccess { _myLevelData.value = it }
        }
    }
}