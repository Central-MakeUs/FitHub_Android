package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.response.ResponseKeywordsData
import com.proteam.fithub.data.remote.response.ResponseLocationData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationService {

    @GET("/home/facilities/{categoryId}")
    suspend fun requestSearchLocationDataWithoutKeyword(
        @Path("categoryId") categoryId : Int,
        @Query("x") x : String,
        @Query("y") y : String,
        @Query("userX") userX : String,
        @Query("userY") userY : String,
        @Query("keyword") keyword : String
    ) : Response<ResponseLocationData>

    @GET("home/facilities/keyword/0")
    suspend fun requestSearchLocationDataWithKeyword(
        @Query("categoryId") categoryId: Int,
        @Query("userX") userX : String,
        @Query("userY") userY : String,
        @Query("keyword") keyword : String
    ) : Response<ResponseLocationData>

    @GET("home/facilities/keywords")
    suspend fun requestRecommendKeywords() : Response<ResponseKeywordsData>

    @GET("home/facilities")
    suspend fun requestSearchLocationDataByKeywords(
        @Query("x") x : String,
        @Query("y") y : String,
        @Query("userX") userX : String,
        @Query("userY") userY : String,
        @Query("keyword") keyword : String
    ) : Response<ResponseLocationData>
}