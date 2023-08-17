package com.proteam.fithub.data.remote.source

import android.util.Log
import com.proteam.fithub.data.remote.request.RequestModifyAlarmPermitData
import com.proteam.fithub.data.remote.response.ResponseAlarmAvailableData
import com.proteam.fithub.data.remote.response.ResponseAlarmPermitData
import com.proteam.fithub.data.remote.service.AlarmService
import com.proteam.fithub.domain.source.AlarmSource
import com.proteam.fithub.presentation.util.BaseResponse
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode
import javax.inject.Inject

class AlarmRemoteSource @Inject constructor(private val service : AlarmService): AlarmSource {
    override suspend fun requestAlarmReadData(alarmId: Int): Result<BaseResponse> {
        val res = service.requestAlarmReadData(alarmId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestAlarmAvailableData(): Result<ResponseAlarmAvailableData.ResultAlarmAvailableData> {
        val res = service.requestAlarmAvailable()
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!.result)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestAlarmPermitData(): Result<ResponseAlarmPermitData.ResultAlarmPermitData> {
        val res = service.requestAlarmPermitData()
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!.result)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestModifyAlarmPermitData(body: RequestModifyAlarmPermitData): Result<ResponseAlarmPermitData.ResultAlarmPermitData> {
        val res = service.requestModifyAlarmPermitData(body)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!.result)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

}