package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.response.ResponseSignIn
import com.proteam.fithub.data.remote.request.RequestSignInKakao
import com.proteam.fithub.presentation.util.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignInService {

    @POST("/users/login/social/kakao")
    suspend fun signInWithKakao(
        @Body body : RequestSignInKakao
    ) : Response<ResponseSignIn>
}