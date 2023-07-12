package com.proteam.fithub.domain.repository

import com.proteam.fithub.data.remote.response.ResponseSignIn
import com.proteam.fithub.data.remote.request.RequestSignInKakao
import com.proteam.fithub.presentation.util.BaseResponse

interface SignInRepository {

    suspend fun signInWithKakao(body : RequestSignInKakao) : Result<ResponseSignIn>

    suspend fun saveAccessToken(accessToken : String)
}