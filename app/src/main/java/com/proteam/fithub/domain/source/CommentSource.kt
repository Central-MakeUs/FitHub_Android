package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.request.RequestPostComment
import com.proteam.fithub.data.remote.response.ResponseCommentHeartClicked
import com.proteam.fithub.presentation.util.BaseResponse

interface CommentSource {

    suspend fun postComment(type : String, id : Int, body : RequestPostComment) : Result<BaseResponse>

    suspend fun postCommentHeartClicked(type : String, id : Int, commentId : Int) : Result<ResponseCommentHeartClicked.ResultCommentHeartClicked>
}