package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.presentation.util.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SignUpService {

    @POST("/users/sms")
    suspend fun requestPhoneAuthCode(
        @Body body : RequestSMSAuth
    ) : Response<BaseResponse>

    @POST("/users/sms/auth")
    suspend fun requestCheckPhoneAuthCode(
        @Body body : RequestCheckSMSAuth
    ) : Response<BaseResponse>

    @GET("/users/exist-nickname")
    suspend fun requestCheckSameNickname(
        @Query("nickname") nickname : String
    ) : Response<BaseResponse>
}