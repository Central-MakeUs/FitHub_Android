package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.response.ResponseSignIn
import com.proteam.fithub.data.remote.request.RequestSignInKakao

interface SignInSource {

    suspend fun signInWithKakao(body : RequestSignInKakao) : Result<ResponseSignIn>
}