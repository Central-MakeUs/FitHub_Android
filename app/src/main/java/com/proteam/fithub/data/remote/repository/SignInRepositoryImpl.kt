package com.proteam.fithub.data.remote.repository

import com.proteam.fithub.data.remote.request.RequestSignInKakao
import com.proteam.fithub.data.remote.response.ResponseSignIn
import com.proteam.fithub.domain.repository.SignInRepository
import com.proteam.fithub.domain.source.SharedPreferenceSource
import com.proteam.fithub.domain.source.SignInSource
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val source: SignInSource,
    private val localSource: SharedPreferenceSource
) : SignInRepository {
    override suspend fun signInWithKakao(body: RequestSignInKakao): Result<ResponseSignIn> {
        return source.signInWithKakao(body)
    }

    override suspend fun saveAccessToken(accessToken: String) {
        localSource.saveAccessToken(accessToken)
    }
}