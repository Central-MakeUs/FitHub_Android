package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseCommentHeartClicked(
    val result : ResultCommentHeartClicked
) : BaseResponse() {
    data class ResultCommentHeartClicked(
        val commentId : Int,
        val newLikes : Int
    )
}
