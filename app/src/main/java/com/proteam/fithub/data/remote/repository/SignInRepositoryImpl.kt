package com.proteam.fithub.data.remote.repository

import com.proteam.fithub.data.remote.request.RequestSignInKakao
import com.proteam.fithub.data.remote.request.RequestSignInPhone
import com.proteam.fithub.data.remote.response.ResponseSignIn
import com.proteam.fithub.domain.repository.SignInRepository
import com.proteam.fithub.domain.source.SharedPreferenceSource
import com.proteam.fithub.domain.source.SignInSource
import com.proteam.fithub.presentation.util.BaseResponse
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
    private val source: SignInSource,
    private val localSource: SharedPreferenceSource
) : SignInRepository {
    override suspend fun autoSignIn(): Result<BaseResponse> {
        return source.autoSignIn()
    }

    override suspend fun signInWithKakao(body: RequestSignInKakao): Result<ResponseSignIn> {
        return source.signInWithKakao(body)
    }

    override suspend fun signInWithPhone(body: RequestSignInPhone): Result<ResponseSignIn> {
        return source.signInWithPhone(body)
    }

    override suspend fun saveUserData(userId: Int?, accessToken: String?) {
        localSource.saveAccessToken(userId, accessToken)
    }

    override suspend fun requestSignOut(): Result<BaseResponse> {
        return source.requestSignOut()
    }

    override suspend fun initUserData() {
        localSource.initUserData()
    }


}