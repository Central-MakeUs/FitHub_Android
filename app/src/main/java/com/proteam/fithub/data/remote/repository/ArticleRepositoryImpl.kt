package com.proteam.fithub.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseArticleDetailData
import com.proteam.fithub.data.remote.response.ResponseArticleHeartClicked
import com.proteam.fithub.data.remote.response.ResponseArticleScrapClicked
import com.proteam.fithub.data.remote.response.ResponsePostArticleData
import com.proteam.fithub.data.remote.service.ArticleService
import com.proteam.fithub.data.remote.source.ArticlePagingSource
import com.proteam.fithub.domain.repository.ArticleRepository
import com.proteam.fithub.domain.source.ArticleSource
import com.proteam.fithub.presentation.util.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val service: ArticleService,
    private val source: ArticleSource
) : ArticleRepository {
    override fun requestArticleData(
        type: String,
        category: Int
    ): Flow<PagingData<ResponseArticleData.ResultArticleData>> {
        return Pager(PagingConfig(pageSize = 12)) {
            ArticlePagingSource(type, Dispatchers.IO, service, category)
        }.flow
    }

    override suspend fun requestArticleDetailData(articleId: Int): Result<ResponseArticleDetailData.ResultArticleDetailData> {
        return source.requestArticleDetailData(articleId)
    }

    override suspend fun requestArticleHeartClicked(articleId: Int): Result<ResponseArticleHeartClicked.ResultArticleHeartClicked> {
        return source.requestArticleHeartClicked(articleId)
    }

    override suspend fun requestArticleScrapClicked(articleId: Int): Result<ResponseArticleScrapClicked.ResultArticleScrapClicked> {
        return source.requestArticleScrapClicked(articleId)
    }

    override suspend fun requestPostArticleData(
        categoryId: Int,
        title: String,
        contents: String,
        exerciseTag: String,
        tagList: List<String>?,
        pictureList: MutableList<MultipartBody.Part>?
    ): Result<ResponsePostArticleData> {
        return source.requestPostArticleData(categoryId, title, contents, exerciseTag, tagList, pictureList)
    }

    override suspend fun requestModifyArticleData(
        articleId: Int,
        title: String,
        contents: String,
        category: Int,
        exerciseTag: String,
        hashTagList: List<String>?,
        newPictureList: MutableList<MultipartBody.Part>?,
        remainPictureUrlList: List<String>?
    ): Result<ResponsePostArticleData> {
        return source.requestModifyArticleData(articleId, title, contents, category, exerciseTag, hashTagList, newPictureList, remainPictureUrlList)
    }

    override suspend fun requestDeleteArticleData(articleId: Int): Result<BaseResponse> {
        return source.requestDeleteBoardData(articleId)
    }
}