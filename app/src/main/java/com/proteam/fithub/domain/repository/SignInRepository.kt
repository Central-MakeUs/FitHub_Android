package com.proteam.fithub.domain.repository

import com.proteam.fithub.data.remote.response.ResponseSignIn
import com.proteam.fithub.data.remote.request.RequestSignInKakao
import com.proteam.fithub.data.remote.request.RequestSignInPhone

interface SignInRepository {

    suspend fun signInWithKakao(body : RequestSignInKakao) : Result<ResponseSignIn>

    suspend fun signInWithPhone(body : RequestSignInPhone) : Result<ResponseSignIn>

    suspend fun saveUserData(userId : Int?, accessToken : String?)
}