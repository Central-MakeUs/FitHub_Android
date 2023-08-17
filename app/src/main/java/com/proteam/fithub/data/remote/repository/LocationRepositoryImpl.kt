package com.proteam.fithub.data.remote.repository

import com.proteam.fithub.data.remote.response.ResponseKeywordsData
import com.proteam.fithub.data.remote.response.ResponseLocationData
import com.proteam.fithub.domain.repository.LocationRepository
import com.proteam.fithub.domain.source.LocationSource
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(private val source : LocationSource): LocationRepository {
    override suspend fun requestLocationData(
        categoryId: Int,
        x: String,
        y: String,
        userX : String,
        userY : String
    ): Result<ResponseLocationData.ResultLocationData> {
        return source.requestLocationData(categoryId, x, y, userX, userY)
    }

    override suspend fun requestKeywordsData(): Result<ResponseKeywordsData.ResultKeywordsData> {
        return source.requestKeywordsData()
    }

    override suspend fun requestLocationDataByKeyword(
        x: String,
        y: String,
        userX: String,
        userY: String,
        keyword: String
    ): Result<ResponseLocationData.ResultLocationData> {
        return source.requestLocationDataByKeyword(x, y, userX, userY, keyword)
    }
}