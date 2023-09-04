package com.proteam.fithub.data.remote.source

import com.proteam.fithub.data.remote.response.ResponseKeywordsData
import com.proteam.fithub.data.remote.response.ResponseLocationData
import com.proteam.fithub.data.remote.service.LocationService
import com.proteam.fithub.domain.source.LocationSource
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode
import javax.inject.Inject

class LocationRemoteSource @Inject constructor(private val service : LocationService): LocationSource {
    override suspend fun requestLocationDataWithoutKeyword(
        categoryId: Int,
        x: String,
        y: String,
        userX : String,
        userY : String,
        keyword : String
    ): Result<ResponseLocationData.ResultLocationData> {
        val res = service.requestSearchLocationDataWithoutKeyword(categoryId, x, y, userX, userY, keyword)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!.result)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestLocationDataWithKeyword(
        categoryId: Int,
        userX: String,
        userY: String,
        keyword: String
    ): Result<ResponseLocationData.ResultLocationData> {
        val res = service.requestSearchLocationDataWithKeyword(categoryId, userX, userY, keyword)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!.result)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestKeywordsData(): Result<ResponseKeywordsData.ResultKeywordsData> {
        val res = service.requestRecommendKeywords()
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!.result)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestLocationDataByKeyword(
        x: String,
        y: String,
        userX: String,
        userY: String,
        keyword: String
    ): Result<ResponseLocationData.ResultLocationData> {
        val res = service.requestSearchLocationDataByKeywords(x, y, userX, userY, keyword)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!.result)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }
}