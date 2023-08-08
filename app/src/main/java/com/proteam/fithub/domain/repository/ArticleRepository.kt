package com.proteam.fithub.domain.repository

import androidx.paging.PagingData
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseArticleDetailData
import com.proteam.fithub.data.remote.response.ResponseArticleHeartClicked
import com.proteam.fithub.data.remote.response.ResponseArticleScrapClicked
import com.proteam.fithub.data.remote.response.ResponseMyArticleData
import com.proteam.fithub.data.remote.response.ResponseMyCertificateData
import com.proteam.fithub.data.remote.response.ResponsePostArticleData
import com.proteam.fithub.presentation.util.BaseResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ArticleRepository {

    fun requestArticleData(
        type: String,
        category: Int
    ): Flow<PagingData<ResponseArticleData.ResultArticleData>>

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
    suspend fun requestDeleteArticleData(articleId: Int) : Result<BaseResponse>

    fun requestMyArticleData(category : Int) : Flow<PagingData<ResponseMyArticleData.ResultMyArticleData>>
}