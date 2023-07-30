package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.response.ResponseCertificateData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecordService {

    @GET("/records/{categoryId}")
    suspend fun requestCertificateDataWithCategory(
        @Path("categoryId") categoryId : Int,
        @Query("pageIndex") pageIndex : Int?
    ) : ResponseCertificateData

    @GET("/records/{categoryId}likes")
    suspend fun requestCertificateDataWithCategoryByLike(
        @Path("categoryId") categoryId : Int,
        @Query("last") last : Int?
    ) : ResponseCertificateData

}