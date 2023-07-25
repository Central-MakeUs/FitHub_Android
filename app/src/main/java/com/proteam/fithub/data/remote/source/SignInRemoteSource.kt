package com.proteam.fithub.data.remote.source

import com.proteam.fithub.data.remote.request.RequestSignInKakao
import com.proteam.fithub.data.remote.request.RequestSignInPhone
import com.proteam.fithub.data.remote.response.ResponseSignIn
import com.proteam.fithub.data.remote.service.SignInService
import com.proteam.fithub.domain.source.SignInSource
import com.proteam.fithub.presentation.util.BaseResponse
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode
import com.proteam.fithub.presentation.util.ErrorConverter.setValidate
import javax.inject.Inject

class SignInRemoteSource @Inject constructor(private val service: SignInService) : SignInSource {
    override suspend fun autoSignIn(): Result<BaseResponse> {
        val res = service.autoSignIn()
        return when (res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun signInWithKakao(body: RequestSignInKakao): Result<ResponseSignIn> {
        val res = service.signInWithKakao(body)
        return setValidate(res) as Result<ResponseSignIn>
    }

    override suspend fun signInWithPhone(body: RequestSignInPhone): Result<ResponseSignIn> {
        val res = service.signInWithPhone(body)
        return when (res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }
}