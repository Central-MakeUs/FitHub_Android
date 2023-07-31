package com.proteam.fithub.data.remote.source

import com.proteam.fithub.data.remote.response.ResponseHomeData
import com.proteam.fithub.data.remote.service.HomeService
import com.proteam.fithub.domain.source.HomeSource
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode
import javax.inject.Inject

class HomeRemoteSource @Inject constructor(private val homeService : HomeService): HomeSource {
    override suspend fun requestHomeData(): Result<ResponseHomeData> {
        val res = homeService.requestHomeData()
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }
}