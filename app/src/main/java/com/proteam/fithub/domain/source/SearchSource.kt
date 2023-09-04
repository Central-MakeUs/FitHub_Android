package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.response.ResponseKeywordsData
import com.proteam.fithub.data.remote.response.ResponseSearchTotalData

interface SearchSource {
    suspend fun getSearchTotalData(tag : String) : Result<ResponseSearchTotalData>
    suspend fun getSearchKeywords() : Result<ResponseKeywordsData.ResultKeywordsData>
}