package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.response.ResponseSignIn
import com.proteam.fithub.data.remote.request.RequestSignInKakao
import com.proteam.fithub.presentation.util.BaseResponse

interface SignInSource {

    suspend fun signInWithKakao(body : RequestSignInKakao) : Result<ResponseSignIn>
}