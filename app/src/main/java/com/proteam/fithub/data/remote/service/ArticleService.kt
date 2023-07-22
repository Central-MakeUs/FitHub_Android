package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseArticleDetailData
import com.proteam.fithub.data.remote.response.ResponseArticleHeartClicked
import com.proteam.fithub.data.remote.response.ResponseArticleScrapClicked
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
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

}