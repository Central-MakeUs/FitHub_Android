package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.response.ResponseSignIn

interface SharedPreferenceSource {

    suspend fun saveAccessToken(userId : Int?, accessToken : String?)

    suspend fun getUserId() : String

    suspend fun deleteUserJWT()

}