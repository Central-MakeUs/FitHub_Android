package com.proteam.fithub.domain.repository

import com.proteam.fithub.data.remote.response.ResponseHomeData
import com.proteam.fithub.data.remote.response.ResponseMyLevelData

interface HomeRepository {

    suspend fun requestHomeData() : Result<ResponseHomeData>

    suspend fun requestMyLevelData() : Result<ResponseMyLevelData.ResultMyLevelData>
}