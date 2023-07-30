package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseArticleDetailData
import com.proteam.fithub.data.remote.response.ResponseArticleHeartClicked
import com.proteam.fithub.data.remote.response.ResponseArticleScrapClicked
import com.proteam.fithub.data.remote.response.ResponsePostArticleData
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
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleService {

    @GET("/articles/{categoryId}")
    suspend fun requestArticleData(
        @Path("categoryId") categoryId: Int,
        @Query("last") last: Int?
    ): ResponseArticleData

    @GET("/articles/{categoryId}/likes")
    suspend fun requestArticleDataByLike(
        @Path("categoryId") categoryId: Int,
        @Query("last") last: Int?
    ): ResponseArticleData

    @GET("/articles/{articleId}/spec")
    suspend fun requestArticleDetail(
        @Path("articleId") articleId: Int
    ): Response<ResponseArticleDetailData>


    @POST("/articles/{articleId}/likes")
    suspend fun requestArticleHeartClicked(
        @Path("articleId") articleId: Int
    ): Response<ResponseArticleHeartClicked.ResultArticleHeartClicked>

    @POST("/articles/{articleId}/scrap")
    suspend fun requestArticleScrapClicked(
        @Path("articleId") articleId: Int
    ): Response<ResponseArticleScrapClicked.ResultArticleScrapClicked>

    @Multipart
    @JvmSuppressWildcards
    @POST("/articles/{categoryId}")
    suspend fun requestPostArticle(
        @Path("categoryId") categoryId: Int,
        @Part("title") title : RequestBody,
        @Part("contents") contents : RequestBody,
        @Part("exerciseTag") exerciseTag : RequestBody,
        @Part("tagList") tagList : List<RequestBody>?,
        @Part pictureList : MutableList<MultipartBody.Part>?
    ) : Response<ResponsePostArticleData>

    @Multipart
    @JvmSuppressWildcards
    @PATCH("/articles/{articleId}")
    suspend fun requestModifyArticle(
        @Path("articleId") articleId: Int,
        @Part("title") title : RequestBody,
        @Part("contents") contents : RequestBody,
        @Part("category") category : Int,
        @Part("exerciseTag") exerciseTag : RequestBody,
        @Part("hashTagList") hashTagList : List<RequestBody>?,
        @Part newPictureList : MutableList<MultipartBody.Part>?,
        @Part("remainPictureUrlList") remainPictureUrlList : List<RequestBody>?
    ) : Response<ResponsePostArticleData>

    @DELETE("articles/{articleId}")
    suspend fun deleteArticleData(
        @Path("articleId") articleId: Int
    ) : Response<BaseResponse>
}