package com.proteam.fithub.data.remote.repository

import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.domain.repository.SignUpRepository
import com.proteam.fithub.domain.source.SignUpSource
import com.proteam.fithub.presentation.util.BaseResponse
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(private val source : SignUpSource) : SignUpRepository {
    override suspend fun requestSMSAuth(body: RequestSMSAuth): Result<BaseResponse> {
        return source.requestSMSAuth(body)
    }

    override suspend fun requestCheckSMSAuth(body: RequestCheckSMSAuth): Result<BaseResponse> {
        return source.requestCheckSMSAuth(body)
    }
}