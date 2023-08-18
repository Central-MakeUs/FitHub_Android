package com.proteam.fithub.domain.repository

import com.proteam.fithub.data.remote.response.ResponseSignIn
import com.proteam.fithub.data.remote.request.RequestSignInKakao
import com.proteam.fithub.data.remote.request.RequestSignInPhone
import com.proteam.fithub.presentation.util.BaseResponse

interface SignInRepository {

    suspend fun autoSignIn() : Result<BaseResponse>

    suspend fun signInWithKakao(body : RequestSignInKakao) : Result<ResponseSignIn>

    suspend fun signInWithPhone(body : RequestSignInPhone) : Result<ResponseSignIn>

    suspend fun saveUserData(userId : Int?, accessToken : String?)

    suspend fun requestSignOut() : Result<BaseResponse>

    suspend fun initUserData()
}