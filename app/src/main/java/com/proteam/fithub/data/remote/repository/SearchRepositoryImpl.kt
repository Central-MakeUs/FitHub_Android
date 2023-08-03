package com.proteam.fithub.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kakao.sdk.common.KakaoSdk.type
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.data.remote.response.ResponseSearchTotalData
import com.proteam.fithub.data.remote.service.SearchService
import com.proteam.fithub.data.remote.source.SearchArticlePagingSource
import com.proteam.fithub.data.remote.source.SearchCertificatePagingSource
import com.proteam.fithub.domain.repository.SearchRepository
import com.proteam.fithub.domain.source.SearchSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val source : SearchSource,
    private val service : SearchService): SearchRepository {
    override suspend fun getSearchTotalData(tag: String): Result<ResponseSearchTotalData> {
        return source.getSearchTotalData(tag)
    }

    override fun getSearchCertificateData(type : String, tag: String): Flow<PagingData<ResponseCertificateData.ResultCertificateData>> {
        return Pager(PagingConfig(pageSize = 12)) {
            SearchCertificatePagingSource(type, Dispatchers.IO, service, tag)
        }.flow
    }

    override fun getSearchArticleData(
        type: String,
        tag: String
    ): Flow<PagingData<ResponseArticleData.ResultArticleData>> {
        return Pager(PagingConfig(pageSize = 12)) {
            SearchArticlePagingSource(type, Dispatchers.IO, service, tag)
        }.flow
    }


}