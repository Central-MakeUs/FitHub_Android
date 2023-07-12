package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.presentation.util.BaseResponse
import retrofit2.Response

interface SignUpSource {

    suspend fun requestSMSAuth(body : RequestSMSAuth) : Result<BaseResponse>

    suspend fun requestCheckSMSAuth(body : RequestCheckSMSAuth) : Result<BaseResponse>

    suspend fun requestCheckSameNickName(nickname : String) : Result<BaseResponse>
}