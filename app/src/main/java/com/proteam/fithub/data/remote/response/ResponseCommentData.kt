package com.proteam.fithub.data.remote.response

import com.proteam.fithub.data.data.ComponentUserData
import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseCommentData(
    val result : ResultCommentData
) : BaseResponse() {
    data class ResultCommentData(
        val commentList : List<ResultCommentItems>
    )
    data class ResultCommentItems(
        val commentId : Int,
        val userInfo : ComponentUserData,
        val contents : String,
        val likes : Int,
        val isLiked : Boolean,
        val createdAt : String
    )
}
