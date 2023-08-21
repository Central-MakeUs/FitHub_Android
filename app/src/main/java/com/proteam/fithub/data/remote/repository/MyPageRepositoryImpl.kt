package com.proteam.fithub.data.remote.repository

import com.proteam.fithub.data.remote.response.ResponseMyInfoData
import com.proteam.fithub.data.remote.response.ResponseMyPageData
import com.proteam.fithub.data.remote.response.ResponseOtherUserProfileData
import com.proteam.fithub.data.remote.response.ResponseTermsData
import com.proteam.fithub.domain.repository.MyPageRepository
import com.proteam.fithub.domain.source.MyPageSource
import com.proteam.fithub.presentation.util.BaseResponse
import okhttp3.MultipartBody
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(private val source : MyPageSource): MyPageRepository {
    override suspend fun requestMyPageData(): Result<ResponseMyPageData.ResultMyPageData> {
        return source.requestMyPageData()
    }

    override suspend fun requestMyInfoData(): Result<ResponseMyInfoData.ResultMyInfoData> {
        return source.requestMyInfoData()
    }

    override suspend fun requestChangeProfileToDefault() : Result<BaseResponse>{
        return source.requestChangeProfileToDefault()
    }

    override suspend fun requestChangeProfileImage(newProfile: MultipartBody.Part): Result<BaseResponse> {
        return source.requestChangeProfileImage(newProfile)
    }

    override suspend fun requestOtherUserProfile(userId: Int): Result<ResponseOtherUserProfileData.ResultOtherUserProfileData> {
        return source.requestOtherUserProfile(userId)
    }

    override suspend fun requestTermsData(termsId: Int): Result<ResponseTermsData.ResultTermsData> {
        return source.requestTermsData(termsId)
    }
}