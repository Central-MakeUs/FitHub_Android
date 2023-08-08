package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.response.ResponseMyInfoData
import com.proteam.fithub.data.remote.response.ResponseMyPageData
import com.proteam.fithub.data.remote.response.ResponseOtherUserProfileData
import com.proteam.fithub.presentation.util.BaseResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Path

interface MyPageService {

    @GET("/users/my-page")
    suspend fun requestMyPageData() : Response<ResponseMyPageData>

    @GET("/user/my-page/personal-data")
    suspend fun requestMyInfoData() : Response<ResponseMyInfoData.ResultMyInfoData>

    @PATCH("/users/my-page/profile/default")
    suspend fun requestChangeProfileToDefault() : Response<BaseResponse>

    @Multipart
    @PATCH("/users/my-page/profile")
    suspend fun requestChangeProfile(
        @Part newProfile : MultipartBody.Part
    ) : Response<BaseResponse>

    @GET("/users/{userId}")
    suspend fun requestOtherUserProfile(
        @Path("userId") userId : Int
    ) : Response<ResponseOtherUserProfileData>
}