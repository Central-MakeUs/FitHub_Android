package com.proteam.fithub.data.remote.repository

import com.proteam.fithub.data.remote.request.RequestChangePassword
import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestPhoneNumberAvailable
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.data.remote.response.ResponseChangePassword
import com.proteam.fithub.data.remote.response.ResponseSignUpWithPhone
import com.proteam.fithub.data.remote.response.ResponseSignUp
import com.proteam.fithub.domain.repository.SignUpRepository
import com.proteam.fithub.domain.source.SignUpSource
import com.proteam.fithub.presentation.util.BaseResponse
import okhttp3.MultipartBody
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

    override suspend fun requestSignUpWithPhone(
        marketingAgree: Boolean,
        phoneNumber: String,
        name: String,
        nickname: String,
        password: String,
        birth: String,
        gender: String,
        preferExercises: Int,
        profileImage: MultipartBody.Part?
    ): Result<ResponseSignUpWithPhone> {
        return source.requestSignUpWithPhone(marketingAgree, phoneNumber, name, nickname, password, birth, gender, preferExercises, profileImage)
    }

    override suspend fun requestSignUpWithSocial(
        marketingAgree: Boolean,
        name: String,
        nickname: String,
        birth: String,
        gender: String,
        preferExercises: Int,
        profileImage: MultipartBody.Part?
    ): Result<ResponseSignUp> {
        return source.requestSignUpWithSocial(marketingAgree, name, nickname, birth, gender, preferExercises, profileImage)
    }

    override suspend fun requestChangePassword(body: RequestChangePassword): Result<ResponseChangePassword> {
        return source.requestChangePassword(body)
    }

    override suspend fun requestExistPhone(type : Int, body: RequestPhoneNumberAvailable): Result<BaseResponse> {
        return source.requestExistPhone(type, body)
    }
}