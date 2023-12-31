package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.request.RequestPostComment
import com.proteam.fithub.data.remote.response.ResponseCommentData
import com.proteam.fithub.data.remote.response.ResponseCommentHeartClicked
import com.proteam.fithub.presentation.util.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentService {

    @GET("/{type}/{id}/comments")
    suspend fun requestCommentData(
        @Path("type") type : String,
        @Path("id") id : Int,
        @Query("pageIndex") pageIndex : Int?
    ) : ResponseCommentData

    @POST("/{type}/{id}/comments")
    suspend fun requestPostCommentData(
        @Path("type") type : String,
        @Path("id") id : Int,
        @Body body : RequestPostComment
    ) : Response<BaseResponse>

    @POST("/{type}/{id}/comments/{commentId}")
    suspend fun requestCommentHeartClicked(
        @Path("type") type : String,
        @Path("id") id : Int,
        @Path("commentId") commentId : Int
    ) : Response<ResponseCommentHeartClicked>

    @DELETE ("/{type}/{id}/comments/{commentId}")
    suspend fun requestDeleteComment(
        @Path("type") type : String,
        @Path("id") id : Int,
        @Path("commentId") commentId : Int
    ) : Response<BaseResponse>
}