package com.proteam.fithub.domain.source

import com.proteam.fithub.presentation.util.BaseResponse

interface AlarmSource {

    suspend fun requestAlarmReadData(alarmId : Int) : Result<BaseResponse>
}