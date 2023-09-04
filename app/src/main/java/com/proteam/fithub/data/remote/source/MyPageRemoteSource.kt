package com.proteam.fithub.data.remote.source

import android.util.Log
import com.proteam.fithub.data.remote.response.ResponseMyInfoData
import com.proteam.fithub.data.remote.response.ResponseMyPageData
import com.proteam.fithub.data.remote.response.ResponseOtherUserProfileData
import com.proteam.fithub.data.remote.response.ResponseTermsData
import com.proteam.fithub.data.remote.service.MyPageService
import com.proteam.fithub.domain.source.MyPageSource
import com.proteam.fithub.presentation.util.BaseResponse
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode
import okhttp3.MultipartBody
import javax.inject.Inject

class MyPageRemoteSource @Inject constructor(private val service : MyPageService): MyPageSource {
    override suspend fun requestMyPageData(): Result<ResponseMyPageData.ResultMyPageData> {
        val res = service.requestMyPageData()
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!.result)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestMyInfoData(): Result<ResponseMyInfoData.ResultMyInfoData> {
        val res = service.requestMyInfoData()
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!.result)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestChangeProfileToDefault() : Result<BaseResponse> {
        val res = service.requestChangeProfileToDefault()
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestChangeProfileImage(newProfile: MultipartBody.Part): Result<BaseResponse> {
        val res = service.requestChangeProfile(newProfile)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestOtherUserProfile(userId: Int): Result<ResponseOtherUserProfileData.ResultOtherUserProfileData> {
        val res = service.requestOtherUserProfile(userId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!.result)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestTermsData(termsId: Int): Result<ResponseTermsData.ResultTermsData> {
        val res = service.requestTermsData(termsId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!.result)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }
}