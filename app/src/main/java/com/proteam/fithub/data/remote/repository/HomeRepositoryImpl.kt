package com.proteam.fithub.data.remote.repository

import com.proteam.fithub.data.remote.response.ResponseHomeData
import com.proteam.fithub.domain.repository.HomeRepository
import com.proteam.fithub.domain.source.HomeSource
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val source : HomeSource): HomeRepository {
    override suspend fun requestHomeData(): Result<ResponseHomeData> {
        return source.requestHomeData()
    }
}