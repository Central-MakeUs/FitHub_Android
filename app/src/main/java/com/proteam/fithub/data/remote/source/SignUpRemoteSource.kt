package com.proteam.fithub.data.remote.source

import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.data.remote.service.SignUpService
import com.proteam.fithub.domain.source.SignUpSource
import com.proteam.fithub.presentation.util.BaseResponse
import javax.inject.Inject

class SignUpRemoteSource @Inject constructor(private val service : SignUpService): SignUpSource {
    override suspend fun requestSMSAuth(body: RequestSMSAuth): Result<BaseResponse> {
        val res = service.requestPhoneAuthCode(body)
        if(res.isSuccessful) {
            return Result.success(res.body()!!)
        }
        return Result.failure(IllegalArgumentException(res.message()))
    }

    override suspend fun requestCheckSMSAuth(body: RequestCheckSMSAuth): Result<BaseResponse> {
        val res = service.requestCheckPhoneAuthCode(body)
        if(res.isSuccessful) {
            return Result.success(res.body()!!)
        }
        return Result.failure(IllegalArgumentException(res.message()))
    }
}