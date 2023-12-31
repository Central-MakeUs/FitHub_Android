package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.request.RequestModifyAlarmPermitData
import com.proteam.fithub.data.remote.response.ResponseAlarmAvailableData
import com.proteam.fithub.data.remote.response.ResponseAlarmData
import com.proteam.fithub.data.remote.response.ResponseAlarmPermitData
import com.proteam.fithub.presentation.util.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface AlarmService {

    @GET("users/alarms")
    suspend fun requestAlarmListData(
        @Query("pageIndex") pageIndex : Int) : ResponseAlarmData

    @GET("users/alarms/{alarmId}")
    suspend fun requestAlarmReadData(
        @Path("alarmId") alarmId : Int
    ) : Response<BaseResponse>

    @GET("user/my-alarm")
    suspend fun requestAlarmAvailable() : Response<ResponseAlarmAvailableData>

    @GET("home/notification-permit")
    suspend fun requestAlarmPermitData() : Response<ResponseAlarmPermitData>

    @PATCH("home/notification-permit")
    suspend fun requestModifyAlarmPermitData(
        @Body body : RequestModifyAlarmPermitData
    ) : Response<ResponseAlarmPermitData>
}