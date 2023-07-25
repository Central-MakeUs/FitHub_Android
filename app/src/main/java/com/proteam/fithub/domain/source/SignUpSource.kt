package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.request.RequestChangePassword
import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestPhoneNumberAvailable
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.data.remote.request.RequestSignUpWithPhone
import com.proteam.fithub.data.remote.request.RequestSignUpWithSocial
import com.proteam.fithub.data.remote.response.ResponseChangePassword
import com.proteam.fithub.data.remote.response.ResponseSignUpWithPhone
import com.proteam.fithub.data.remote.response.ResponseSignUpWithSocial
import com.proteam.fithub.presentation.util.BaseResponse
import okhttp3.MultipartBody
import retrofit2.Response

interface SignUpSource {

    suspend fun requestSMSAuth(body : RequestSMSAuth) : Result<BaseResponse>

    suspend fun requestCheckSMSAuth(body : RequestCheckSMSAuth) : Result<BaseResponse>

    suspend fun requestCheckSameNickName(nickname : String) : Result<BaseResponse>

    suspend fun requestUserNumberAvailable(phoneNumber : RequestPhoneNumberAvailable) : Result<BaseResponse>

    suspend fun requestSignUpWithPhone(marketingAgree : Boolean, phoneNumber : String, name : String, nickname : String, password : String, birth : String, gender : String, preferExercises : List<Int>, profileImage : MultipartBody.Part) : Result<ResponseSignUpWithPhone>

    suspend fun requestSignUpWithSocial(body : RequestSignUpWithSocial) : Result<ResponseSignUpWithSocial>

    suspend fun requestChangePassword(body : RequestChangePassword) : Result<ResponseChangePassword>
}