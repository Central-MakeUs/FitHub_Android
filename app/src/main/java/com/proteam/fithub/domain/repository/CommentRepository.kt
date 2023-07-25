package com.proteam.fithub.domain.repository

import androidx.paging.PagingData
import com.proteam.fithub.data.remote.request.RequestPostComment
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.data.remote.response.ResponseCommentData
import com.proteam.fithub.presentation.util.BaseResponse
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    fun requestComments(type : String, id : Int) : Flow<PagingData<ResponseCommentData.ResultCommentItems>>

    suspend fun postComment(type : String, id : Int, body : RequestPostComment) : Result<BaseResponse>
}