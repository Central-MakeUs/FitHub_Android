package com.proteam.fithub.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.domain.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val alarmRepository: AlarmRepository): ViewModel() {

    private val _curFragmentTag = MutableLiveData<String>()
    val curFragmentTag : LiveData<String> = _curFragmentTag

    private val _remainAlarm = MutableLiveData<Boolean>()
    val remainAlarm : LiveData<Boolean> = _remainAlarm

    fun setFragmentTag(tag : String) {
        _curFragmentTag.value = tag
    }

    fun requestAlarmRead(alarmId : Int) {
        viewModelScope.launch {
            alarmRepository.requestAlarmReadData(alarmId)
        }
    }

    fun requestCheckAlarmAvailable() {
        viewModelScope.launch {
            alarmRepository.requestAlarmAvailableData()
                .onSuccess { _remainAlarm.value = it.isRemain }
        }
    }
}