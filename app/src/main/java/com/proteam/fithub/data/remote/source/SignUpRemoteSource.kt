package com.proteam.fithub.data.remote.source

import com.proteam.fithub.data.remote.request.RequestChangePassword
import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestPhoneNumberAvailable
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.data.remote.response.ResponseChangePassword
import com.proteam.fithub.data.remote.response.ResponseSignUp
import com.proteam.fithub.data.remote.response.ResponseSignUpWithPhone
import com.proteam.fithub.data.remote.service.SignUpService
import com.proteam.fithub.domain.source.SignUpSource
import com.proteam.fithub.presentation.util.BaseResponse
import com.proteam.fithub.presentation.util.ConvertToRequestBody.textConverter
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode
import com.proteam.fithub.presentation.util.ErrorConverter.setValidate
import okhttp3.MultipartBody
import javax.inject.Inject

class SignUpRemoteSource @Inject constructor(private val service: SignUpService) : SignUpSource {
    override suspend fun requestSMSAuth(body: RequestSMSAuth): Result<BaseResponse> {
        val res = service.requestPhoneAuthCode(body)
        return setValidate(res) as Result<BaseResponse>
    }

    override suspend fun requestCheckSMSAuth(body: RequestCheckSMSAuth): Result<BaseResponse> {
        val res = service.requestCheckPhoneAuthCode(body)
        return setValidate(res) as Result<BaseResponse>
    }

    override suspend fun requestCheckSameNickName(nickname: String): Result<BaseResponse> {
        val res = service.requestCheckSameNickname(nickname)
        return setValidate(res) as Result<BaseResponse>
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
        val res = service.requestSignUpWithPhone(
            marketingAgree,
            phoneNumber.textConverter(),
            name.textConverter(),
            nickname.textConverter(),
            password.textConverter(),
            birth.textConverter(),
            gender.textConverter(),
            preferExercises,
            profileImage
        )
        return when (res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(
                IllegalArgumentException(
                    res.errorBody()?.convertAndGetCode().toString()
                )
            )
        }
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
        val res = service.requestSignUpWithSocial(
            marketingAgree,
            name.textConverter(),
            nickname.textConverter(),
            birth.textConverter(),
            gender.textConverter(),
            preferExercises,
            profileImage
        )
        return when (res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(
                IllegalArgumentException(
                    res.errorBody()?.convertAndGetCode().toString()
                )
            )
        }
    }

    override suspend fun requestChangePassword(body: RequestChangePassword): Result<ResponseChangePassword> {
        val res = service.requestChangePassword(body)
        return setValidate(res) as Result<ResponseChangePassword>
    }

    override suspend fun requestExistPhone(type : Int, body: RequestPhoneNumberAvailable): Result<BaseResponse> {
        val res = service.requestExistPhone(type, body)
        return when (res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(
                IllegalArgumentException(
                    res.errorBody()?.convertAndGetCode().toString()
                )
            )
        }
    }
}