package com.proteam.fithub.data.remote.repository

import com.proteam.fithub.data.remote.response.ResponseMyPageData
import com.proteam.fithub.domain.repository.MyPageRepository
import com.proteam.fithub.domain.source.MyPageSource
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(private val source : MyPageSource): MyPageRepository {
    override suspend fun requestMyPageData(): Result<ResponseMyPageData.ResultMyPageData> {
        return source.requestMyPageData()
    }
}