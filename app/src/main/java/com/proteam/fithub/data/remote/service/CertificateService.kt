package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.data.remote.response.ResponseCertificateDetailData
import com.proteam.fithub.data.remote.response.ResponseCertificateHeartClicked
import com.proteam.fithub.data.remote.response.ResponsePostCertificateData
import com.proteam.fithub.presentation.util.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Base64.Encoder

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
    @JvmSuppressWildcards
    @POST("records/{categoryId}")
    suspend fun postCertificateData(
        @Path("categoryId", encoded = true) categoryId: Int,
        @Part("contents", encoding = "utf-8") contents : RequestBody,
        @Part("exerciseTag", encoding = "utf-8") exerciseTag : RequestBody,
        @Part("hashTagList", encoding = "utf-8") hashTagList : List<RequestBody>?,
        @Part image : MultipartBody.Part
    ) : Response<ResponsePostCertificateData>


    @DELETE("record/{recordId}")
    suspend fun deleteCertificateData(
        @Path("recordId") recordId: Int
    ) : Response<BaseResponse>

    @Multipart
    @JvmSuppressWildcards
    @PATCH("/record/{recordId}")
    suspend fun modifyCertificateData(
        @Path("recordId") recordId: Int,
        @Part("category", encoding = "utf-8") category : Int,
        @Part("contents", encoding = "utf-8") contents : RequestBody,
        @Part("exerciseTag", encoding = "utf-8") exerciseTag : RequestBody,
        @Part("hashTagList", encoding = "utf-8") hashTagList : List<RequestBody>?,
        @Part newImage : MultipartBody.Part?,
        @Part("remainImageUrl") remainImageUrl : RequestBody?
    ) : Response<ResponsePostCertificateData>
}