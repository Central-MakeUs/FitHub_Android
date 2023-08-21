package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.request.RequestChangePassword
import com.proteam.fithub.data.remote.response.ResponseSignIn
import com.proteam.fithub.data.remote.request.RequestSignInKakao
import com.proteam.fithub.data.remote.request.RequestSignInPhone
import com.proteam.fithub.data.remote.request.RequestSignUpWithPhone
import com.proteam.fithub.data.remote.response.ResponseChangePassword
import com.proteam.fithub.presentation.util.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface SignInService {

    @GET("/")
    suspend fun autoSignIn() : Response<BaseResponse>

    @POST("/users/login/social/kakao")
    suspend fun signInWithKakao(
        @Body body : RequestSignInKakao
    ) : Response<ResponseSignIn>

    @POST("/users/sign-in")
    suspend fun signInWithPhone(
        @Body body : RequestSignInPhone
    ) : Response<ResponseSignIn>

    @POST("/users/quit")
    suspend fun requestSignOut() : Response<BaseResponse>

    @POST("/users/logout")
    suspend fun requestLogOut()
}