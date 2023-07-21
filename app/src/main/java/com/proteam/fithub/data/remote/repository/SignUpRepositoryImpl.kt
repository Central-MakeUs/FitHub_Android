package com.proteam.fithub.data.remote.repository

import com.proteam.fithub.data.remote.request.RequestChangePassword
import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestPhoneNumberAvailable
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.data.remote.request.RequestSignUpWithPhone
import com.proteam.fithub.data.remote.request.RequestSignUpWithSocial
import com.proteam.fithub.data.remote.response.ResponseChangePassword
import com.proteam.fithub.data.remote.response.ResponseSignUpWithPhone
import com.proteam.fithub.data.remote.response.ResponseSignUpWithSocial
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

    override suspend fun requestCheckSameNickName(nickname: String): Result<BaseResponse> {
        return source.requestCheckSameNickName(nickname)
    }

    override suspend fun requestUserNumberAvailable(phoneNumber: RequestPhoneNumberAvailable): Result<BaseResponse> {
        return source.requestUserNumberAvailable(phoneNumber)
    }

    override suspend fun requestSignUpWithPhone(body: RequestSignUpWithPhone): Result<ResponseSignUpWithPhone> {
        return source.requestSignUpWithPhone(body)
    }

    override suspend fun requestSignUpWithSocial(body: RequestSignUpWithSocial): Result<ResponseSignUpWithSocial> {
        return source.requestSignUpWithSocial(body)
    }

    override suspend fun requestChangePassword(body: RequestChangePassword): Result<ResponseChangePassword> {
        return source.requestChangePassword(body)
    }
}