package com.proteam.fithub.data.remote.response

import com.proteam.fithub.data.data.CategoryData
import com.proteam.fithub.data.data.ComponentUserData
import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseArticleData (
    val result : ListResultArticleData
) : BaseResponse() {
    data class ListResultArticleData(
        val articleList : List<ResultArticleData>
    )
    data class ResultArticleData(
       val articleId : Int,
       val userInfo : ComponentUserData,
       val articleCategory : CategoryData,
       val title : String,
       val contents : String,
       val pictureUrl : String?,
       val exerciseTag : String,
       var likes : Int,
       var isLiked : Boolean,
       val comments : Int,
       val createdAt : String
    )
}