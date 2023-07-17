package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.request.RequestChangePassword
import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestPhoneNumberAvailable
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.data.remote.request.RequestSignUpWithPhone
import com.proteam.fithub.data.remote.response.ResponseChangePassword
import com.proteam.fithub.data.remote.response.ResponseSignUpWithPhone
import com.proteam.fithub.presentation.util.BaseResponse
import retrofit2.Response

interface SignUpSource {

    suspend fun requestSMSAuth(body : RequestSMSAuth) : Result<BaseResponse>

    suspend fun requestCheckSMSAuth(body : RequestCheckSMSAuth) : Result<BaseResponse>

    suspend fun requestCheckSameNickName(nickname : String) : Result<BaseResponse>

    suspend fun requestUserNumberAvailable(phoneNumber : RequestPhoneNumberAvailable) : Result<BaseResponse>

    suspend fun requestSignUpWithPhone(body : RequestSignUpWithPhone) : Result<ResponseSignUpWithPhone>

    suspend fun requestChangePassword(body : RequestChangePassword) : Result<ResponseChangePassword>
}