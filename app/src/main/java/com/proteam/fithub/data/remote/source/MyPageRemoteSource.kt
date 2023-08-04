package com.proteam.fithub.data.remote.source

import com.proteam.fithub.data.remote.response.ResponseMyPageData
import com.proteam.fithub.data.remote.service.MyPageService
import com.proteam.fithub.domain.source.MyPageSource
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode
import javax.inject.Inject

class MyPageRemoteSource @Inject constructor(private val service : MyPageService): MyPageSource {
    override suspend fun requestMyPageData(): Result<ResponseMyPageData.ResultMyPageData> {
        val res = service.requestMyPageData()
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!.result)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }
}