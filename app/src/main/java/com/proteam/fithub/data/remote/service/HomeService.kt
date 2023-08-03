package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.response.ResponseHomeData
import retrofit2.Response
import retrofit2.http.GET

interface HomeService {

    @GET("/home")
    suspend fun requestHomeData() : Response<ResponseHomeData>
}