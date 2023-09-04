package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.request.RequestModifyAlarmPermitData
import com.proteam.fithub.data.remote.response.ResponseAlarmAvailableData
import com.proteam.fithub.data.remote.response.ResponseAlarmPermitData
import com.proteam.fithub.presentation.util.BaseResponse

interface AlarmSource {

    suspend fun requestAlarmReadData(alarmId : Int) : Result<BaseResponse>

    suspend fun requestAlarmAvailableData() : Result<ResponseAlarmAvailableData.ResultAlarmAvailableData>

    suspend fun requestAlarmPermitData() : Result<ResponseAlarmPermitData.ResultAlarmPermitData>

    suspend fun requestModifyAlarmPermitData(body : RequestModifyAlarmPermitData) : Result<ResponseAlarmPermitData.ResultAlarmPermitData>
}