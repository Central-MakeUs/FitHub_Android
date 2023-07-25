package com.proteam.fithub.data.remote.source

import android.util.Log
import com.proteam.fithub.data.remote.request.RequestChangePassword
import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestPhoneNumberAvailable
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.data.remote.request.RequestSignUpWithPhone
import com.proteam.fithub.data.remote.request.RequestSignUpWithSocial
import com.proteam.fithub.data.remote.response.ResponseChangePassword
import com.proteam.fithub.data.remote.response.ResponseSignUpWithPhone
import com.proteam.fithub.data.remote.response.ResponseSignUpWithSocial
import com.proteam.fithub.data.remote.service.SignUpService
import com.proteam.fithub.domain.source.SignUpSource
import com.proteam.fithub.presentation.util.BaseResponse
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode
import com.proteam.fithub.presentation.util.ErrorConverter.setValidate
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class SignUpRemoteSource @Inject constructor(private val service : SignUpService): SignUpSource {
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

    override suspend fun requestUserNumberAvailable(phoneNumber: RequestPhoneNumberAvailable): Result<BaseResponse> {
        val res = service.requestPhoneNumberAvailable(phoneNumber)
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
        preferExercises: List<Int>,
        profileImage: MultipartBody.Part
    ): Result<ResponseSignUpWithPhone> {
        val res = service.requestSignUpWithPhone(marketingAgree, phoneNumber, name, nickname, password, birth, gender, preferExercises, profileImage)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }


    override suspend fun requestSignUpWithSocial(body: RequestSignUpWithSocial): Result<ResponseSignUpWithSocial> {
        val res = service.requestSignUpWithSocial(body)
        return setValidate(res) as Result<ResponseSignUpWithSocial>
    }

    override suspend fun requestChangePassword(body: RequestChangePassword): Result<ResponseChangePassword> {
        val res = service.requestChangePassword(body)
        return setValidate(res) as Result<ResponseChangePassword>
    }
}