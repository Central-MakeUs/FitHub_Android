package com.proteam.fithub.domain.repository

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

interface SignUpRepository {

    suspend fun requestSMSAuth(body : RequestSMSAuth) : Result<BaseResponse>

    suspend fun requestCheckSMSAuth(body : RequestCheckSMSAuth) : Result<BaseResponse>

    suspend fun requestCheckSameNickName(nickname : String) : Result<BaseResponse>

    suspend fun requestUserNumberAvailable(phoneNumber : RequestPhoneNumberAvailable) : Result<BaseResponse>

    suspend fun requestSignUpWithPhone(body : RequestSignUpWithPhone) : Result<ResponseSignUpWithPhone>

    suspend fun requestSignUpWithSocial(body : RequestSignUpWithSocial) : Result<ResponseSignUpWithSocial>

    suspend fun requestChangePassword(body : RequestChangePassword) : Result<ResponseChangePassword>
}