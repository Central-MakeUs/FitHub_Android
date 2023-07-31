package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.data.remote.response.ResponseSearchTotalData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("/search")
    suspend fun getSearchTotalData(
        @Query("tag") tag : String
    ) : Response<ResponseSearchTotalData>

    @GET("/search/records")
    suspend fun getSearchCertificateData(
        @Query("tag") tag : String,
        @Query("pageIndex") pageIndex : Int?
    ) : ResponseCertificateData

    @GET("/search/records/likes")
    suspend fun getSearchCertificateDataByLike(
        @Query("tag", encoded = true) tag : String,
        @Query("pageIndex") pageIndex : Int?
    ) : ResponseCertificateData

    @GET("/search/articles")
    suspend fun getSearchArticleData(
        @Query("tag") tag : String,
        @Query("pageIndex") pageIndex : Int?
    ) : ResponseArticleData

    @GET("/search/articles/likes")
    suspend fun getSearchArticleDataByLike(
        @Query("tag", encoded = true) tag : String,
        @Query("pageIndex") pageIndex : Int?
    ) : ResponseArticleData
}