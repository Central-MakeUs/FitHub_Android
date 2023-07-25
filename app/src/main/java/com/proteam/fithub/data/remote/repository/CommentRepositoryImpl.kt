package com.proteam.fithub.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.proteam.fithub.data.remote.request.RequestPostComment
import com.proteam.fithub.data.remote.response.ResponseCommentData
import com.proteam.fithub.data.remote.response.ResponseCommentHeartClicked
import com.proteam.fithub.data.remote.service.CommentService
import com.proteam.fithub.data.remote.source.CommentPagingSource
import com.proteam.fithub.domain.repository.CommentRepository
import com.proteam.fithub.domain.source.CommentSource
import com.proteam.fithub.presentation.util.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val service: CommentService,
    private val source: CommentSource
) : CommentRepository {
    override fun requestComments(
        type: String,
        id: Int
    ): Flow<PagingData<ResponseCommentData.ResultCommentItems>> {
        return Pager(PagingConfig(pageSize = 10)) {
            CommentPagingSource(type = type, id = id, Dispatchers.IO, service)
        }.flow
    }

    override suspend fun postComment(
        type: String,
        id: Int,
        body: RequestPostComment
    ): Result<BaseResponse> {
        return source.postComment(type, id, body)
    }

    override suspend fun postCommentHeartClicked(
        type: String,
        id: Int,
        commentId: Int
    ): Result<ResponseCommentHeartClicked.ResultCommentHeartClicked> {
        return source.postCommentHeartClicked(type, id, commentId)
    }
}