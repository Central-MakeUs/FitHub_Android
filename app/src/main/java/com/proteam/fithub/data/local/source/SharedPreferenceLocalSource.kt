package com.proteam.fithub.data.local.source

import com.proteam.fithub.data.remote.response.ResponseSignIn
import com.proteam.fithub.domain.source.SharedPreferenceSource
import com.proteam.fithub.presentation.ui.FitHub.Companion.mSharedPreferences

class SharedPreferenceLocalSource : SharedPreferenceSource {

    override suspend fun saveAccessToken(userId : Int?, accessToken : String) {
        try {
            mSharedPreferences.edit().apply{
                putString("jwt", accessToken).apply()
                userId?.let { putString("userId", it.toString()) }
            }
        } catch (exception : Exception) {
            throw exception
        }
    }

    override suspend fun getUserId(): String {
        return mSharedPreferences.getString("userId", "NULL") ?: "NULL"
    }

    override suspend fun deleteUserJWT() {
        mSharedPreferences.edit().clear().apply()
    }
}