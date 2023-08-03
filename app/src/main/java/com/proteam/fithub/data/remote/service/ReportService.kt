package com.proteam.fithub.data.remote.service

import com.proteam.fithub.presentation.util.BaseResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface ReportService {

    @POST("/users/{userId}/report")
    suspend fun postUserReport(
        @Path ("userId") userId : Int
    ) : Response<BaseResponse>

    @POST("articles/{articleId}/report")
    suspend fun postArticleReport(
        @Path("articleId") articleId : Int
    ) : Response<BaseResponse>

    @POST("records/{recordId}/report")
    suspend fun postRecordReport(
        @Path("recordId") recordId : Int
    ) : Response<BaseResponse>

    @POST("comments/{commentsId}/report")
    suspend fun postCommentReport(
        @Path("commentsId") commentId : Int
    ) : Response<BaseResponse>
}