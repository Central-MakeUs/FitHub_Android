package com.proteam.fithub.presentation.ui.alarm_setting.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.request.RequestModifyAlarmPermitData
import com.proteam.fithub.domain.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmSettingViewModel @Inject constructor(private val repository : AlarmRepository): ViewModel() {
    private val _alarmPermitList = MutableLiveData<MutableList<Boolean>>()
    val alarmPermitList : LiveData<MutableList<Boolean>> = _alarmPermitList

    init {
        requestAlarmPermitData()
    }

    private fun requestAlarmPermitData() {
        viewModelScope.launch {
            repository.requestAlarmPermitData()
                .onSuccess { _alarmPermitList.value = mutableListOf(it.communityPermit, it.marketingPermit) }
        }
    }

    fun requestModifyPermitData(community : Boolean, marketing : Boolean) {
        viewModelScope.launch {
            repository.requestModifyAlarmPermitData(RequestModifyAlarmPermitData(community, marketing))
                .onSuccess {
                    _alarmPermitList.value = mutableListOf(it.communityPermit, it.marketingPermit) }
        }
    }

}