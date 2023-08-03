package com.proteam.fithub.domain.repository

import androidx.paging.PagingData
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.data.remote.response.ResponseSearchTotalData
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun getSearchTotalData(tag : String) : Result<ResponseSearchTotalData>

    fun getSearchCertificateData(type : String, tag : String) : Flow<PagingData<ResponseCertificateData.ResultCertificateData>>

    fun getSearchArticleData(type : String, tag : String) : Flow<PagingData<ResponseArticleData.ResultArticleData>>
}