package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.response.ResponseSignIn
import com.proteam.fithub.data.remote.request.RequestSignInKakao
import com.proteam.fithub.data.remote.request.RequestSignInPhone
import com.proteam.fithub.presentation.util.BaseResponse

interface SignInSource {

    suspend fun autoSignIn() : Result<BaseResponse>
    suspend fun signInWithKakao(body : RequestSignInKakao) : Result<ResponseSignIn>
    suspend fun signInWithPhone(body : RequestSignInPhone) : Result<ResponseSignIn>

    suspend fun requestSignOut() : Result<BaseResponse>

    suspend fun requestLogOut()
}