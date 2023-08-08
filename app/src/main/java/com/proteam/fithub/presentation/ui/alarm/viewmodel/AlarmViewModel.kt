package com.proteam.fithub.presentation.ui.alarm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.proteam.fithub.data.remote.response.ResponseAlarmData
import com.proteam.fithub.domain.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(private val alarmRepository: AlarmRepository): ViewModel() {

    fun requestAlarmData() : Flow<PagingData<ResponseAlarmData.ResultAlarmData>> {
        return alarmRepository.requestAlarmData()
    }

    fun requestAlarmRead(alarmId : Int) {
        viewModelScope.launch {
            alarmRepository.requestAlarmReadData(alarmId)
        }
    }
}