package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.data.remote.response.ResponseCertificateDetailData
import com.proteam.fithub.data.remote.response.ResponseCertificateHeartClicked
import com.proteam.fithub.data.remote.response.ResponsePostCertificateData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface CertificateService {

    @GET("/records/{categoryId}")
    suspend fun requestCertificateData(
        @Path("categoryId") categoryId : Int,
        @Query("last") last : Int?
    ) : ResponseCertificateData

    @GET("/records/{categoryId}likes")
    suspend fun requestCertificateDataByLike(
        @Path("categoryId") categoryId : Int,
        @Query("last") last : Int?
    ) : ResponseCertificateData

    @GET("/records/{recordId}/spec")
    suspend fun requestCertificateDetailData(
        @Path("recordId") recordId : Int
    ) : Response<ResponseCertificateDetailData>

    @POST("/records/{recordId}/likes")
    suspend fun requestClickCertificateHeart(
        @Path("recordId") recordId: Int
    ) : Response<ResponseCertificateHeartClicked>

    @Multipart
    @POST("records/{categoryId}")
    suspend fun postCertificateData(
        @Path("categoryId") categoryId: Int,
        @Part("contents") contents : String,
        @Part("exerciseTag") exerciseTag : String,
        @Part("hashTagList") hashTagList : List<String>,
        @Part image : MultipartBody.Part
    ) : Response<ResponsePostCertificateData>
}