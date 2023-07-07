package com.proteam.fithub.data.remote.source

import com.proteam.fithub.data.remote.request.RequestSignInKakao
import com.proteam.fithub.data.remote.response.ResponseSignIn
import com.proteam.fithub.data.remote.service.SignInService
import com.proteam.fithub.domain.source.SignInSource
import javax.inject.Inject

class SignInRemoteSource @Inject constructor(private val service : SignInService): SignInSource {
    override suspend fun signInWithKakao(body: RequestSignInKakao): Result<ResponseSignIn> {
        val res = service.signInWithKakao(body)
        if(res.isSuccessful) {
            return Result.success(res.body()!!)
        }
        return Result.failure(IllegalArgumentException(res.message()))
    }
}