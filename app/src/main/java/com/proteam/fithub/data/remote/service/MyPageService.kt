package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.response.ResponseMyInfoData
import com.proteam.fithub.data.remote.response.ResponseMyPageData
import retrofit2.Response
import retrofit2.http.GET

interface MyPageService {

    @GET("/users/my-page")
    suspend fun requestMyPageData() : Response<ResponseMyPageData>

    @GET("/user/my-page/personal-data")
    suspend fun requestMyInfoData() : Response<ResponseMyInfoData.ResultMyInfoData>
}