package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.request.RequestDeleteMyArticles
import com.proteam.fithub.data.remote.request.RequestDeleteMyCertificate
import com.proteam.fithub.data.remote.response.ResponseArticleDetailData
import com.proteam.fithub.data.remote.response.ResponseArticleHeartClicked
import com.proteam.fithub.data.remote.response.ResponseArticleScrapClicked
import com.proteam.fithub.data.remote.response.ResponsePostArticleData
import com.proteam.fithub.presentation.util.BaseResponse
import okhttp3.MultipartBody

interface ArticleSource {

    suspend fun requestArticleDetailData(articleId: Int): Result<ResponseArticleDetailData.ResultArticleDetailData>

    suspend fun requestArticleHeartClicked(articleId: Int): Result<ResponseArticleHeartClicked.ResultArticleHeartClicked>

    suspend fun requestArticleScrapClicked(articleId: Int): Result<ResponseArticleScrapClicked.ResultArticleScrapClicked>

    suspend fun requestPostArticleData(
        categoryId: Int,
        title: String,
        contents: String,
        exerciseTag: String,
        tagList: List<String>?,
        pictureList: MutableList<MultipartBody.Part>?
    ): Result<ResponsePostArticleData>

    suspend fun requestModifyArticleData(
        articleId: Int,
        title: String,
        contents: String,
        category : Int,
        exerciseTag: String,
        hashTagList: List<String>?,
        newPictureList: MutableList<MultipartBody.Part>?,
        remainPictureUrlList : List<String>?
    ): Result<ResponsePostArticleData>

    suspend fun requestDeleteBoardData(articleId: Int) : Result<BaseResponse>

    suspend fun requestDeleteMyArticleData(body : RequestDeleteMyArticles) : Result<BaseResponse>

}