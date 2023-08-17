package com.proteam.fithub.domain.repository

import androidx.paging.PagingData
import com.proteam.fithub.data.remote.request.RequestModifyAlarmPermitData
import com.proteam.fithub.data.remote.response.ResponseAlarmAvailableData
import com.proteam.fithub.data.remote.response.ResponseAlarmData
import com.proteam.fithub.data.remote.response.ResponseAlarmPermitData
import com.proteam.fithub.presentation.util.BaseResponse
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    fun requestAlarmData() : Flow<PagingData<ResponseAlarmData.ResultAlarmData>>

    suspend fun requestAlarmReadData(alarmId : Int) : Result<BaseResponse>

    suspend fun requestAlarmAvailableData() : Result<ResponseAlarmAvailableData.ResultAlarmAvailableData>

    suspend fun requestAlarmPermitData() : Result<ResponseAlarmPermitData.ResultAlarmPermitData>

    suspend fun requestModifyAlarmPermitData(body : RequestModifyAlarmPermitData) : Result<ResponseAlarmPermitData.ResultAlarmPermitData>
}