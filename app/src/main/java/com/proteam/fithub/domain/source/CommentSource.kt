package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.request.RequestPostComment
import com.proteam.fithub.presentation.util.BaseResponse

interface CommentSource {

    suspend fun postComment(type : String, id : Int, body : RequestPostComment) : Result<BaseResponse>
}