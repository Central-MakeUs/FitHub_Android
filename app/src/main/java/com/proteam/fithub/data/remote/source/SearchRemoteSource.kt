package com.proteam.fithub.data.remote.source

import android.util.Log
import com.proteam.fithub.data.remote.response.ResponseSearchTotalData
import com.proteam.fithub.data.remote.service.SearchService
import com.proteam.fithub.domain.source.SearchSource
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode
import javax.inject.Inject

class SearchRemoteSource @Inject constructor(private val service : SearchService): SearchSource {
    override suspend fun getSearchTotalData(tag: String): Result<ResponseSearchTotalData> {
        val res = service.getSearchTotalData(tag)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }
}