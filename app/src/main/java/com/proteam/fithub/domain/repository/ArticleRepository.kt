package com.proteam.fithub.domain.repository

import androidx.paging.PagingData
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseArticleDetailData
import com.proteam.fithub.data.remote.response.ResponseArticleHeartClicked
import com.proteam.fithub.data.remote.response.ResponseArticleScrapClicked
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {

    fun requestArticleData(type : String, category : Int) : Flow<PagingData<ResponseArticleData.ResultArticleData>>

    suspend fun requestArticleDetailData(articleId : Int) : Result<ResponseArticleDetailData.ResultArticleDetailData>

    suspend fun requestArticleHeartClicked(articleId : Int) : Result<ResponseArticleHeartClicked.ResultArticleHeartClicked>

    suspend fun requestArticleScrapClicked(articleId : Int) : Result<ResponseArticleScrapClicked.ResultArticleScrapClicked>
}