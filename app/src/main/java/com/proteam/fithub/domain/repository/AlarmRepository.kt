package com.proteam.fithub.domain.repository

import androidx.paging.PagingData
import com.proteam.fithub.data.remote.response.ResponseAlarmData
import com.proteam.fithub.presentation.util.BaseResponse
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    fun requestAlarmData() : Flow<PagingData<ResponseAlarmData.ResultAlarmData>>

    suspend fun requestAlarmReadData(alarmId : Int) : Result<BaseResponse>
}