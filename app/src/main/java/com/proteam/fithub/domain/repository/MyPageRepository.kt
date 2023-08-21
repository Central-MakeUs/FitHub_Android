package com.proteam.fithub.domain.repository

import com.proteam.fithub.data.remote.response.ResponseMyInfoData
import com.proteam.fithub.data.remote.response.ResponseMyPageData
import com.proteam.fithub.data.remote.response.ResponseOtherUserProfileData
import com.proteam.fithub.data.remote.response.ResponseTermsData
import com.proteam.fithub.presentation.util.BaseResponse
import okhttp3.MultipartBody

interface MyPageRepository {

    suspend fun requestMyPageData() : Result<ResponseMyPageData.ResultMyPageData>

    suspend fun requestMyInfoData() : Result<ResponseMyInfoData.ResultMyInfoData>

    suspend fun requestChangeProfileToDefault()  : Result<BaseResponse>

    suspend fun requestChangeProfileImage(newProfile : MultipartBody.Part) : Result<BaseResponse>

    suspend fun requestOtherUserProfile(userId : Int) : Result<ResponseOtherUserProfileData.ResultOtherUserProfileData>

    suspend fun requestTermsData(termsId : Int) : Result<ResponseTermsData.ResultTermsData>
}