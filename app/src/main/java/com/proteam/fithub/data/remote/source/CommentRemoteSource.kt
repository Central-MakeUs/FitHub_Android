package com.proteam.fithub.data.remote.source

import com.proteam.fithub.data.remote.request.RequestPostComment
import com.proteam.fithub.data.remote.response.ResponseCommentHeartClicked
import com.proteam.fithub.data.remote.service.CommentService
import com.proteam.fithub.domain.source.CommentSource
import com.proteam.fithub.presentation.util.BaseResponse
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode
import com.proteam.fithub.presentation.util.ErrorConverter.setValidate
import javax.inject.Inject


class CommentRemoteSource @Inject constructor(private val service : CommentService): CommentSource {
    override suspend fun postComment(
        type: String,
        id: Int,
        body: RequestPostComment
    ): Result<BaseResponse> {
        val res = service.requestPostCommentData(type, id, body)
        return setValidate(res) as Result<BaseResponse>
    }

    override suspend fun postCommentHeartClicked(
        type: String,
        id: Int,
        commentId: Int
    ): Result<ResponseCommentHeartClicked> {
        val res = service.requestCommentHeartClicked(type, id, commentId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun postDeleteComment(
        type: String,
        id: Int,
        commentId: Int
    ): Result<BaseResponse> {
        val res = service.requestDeleteComment(type, id, commentId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }
}