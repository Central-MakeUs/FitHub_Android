package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.response.ResponseHomeData
import com.proteam.fithub.data.remote.response.ResponseMyLevelData

interface HomeSource {

    suspend fun requestHomeData() : Result<ResponseHomeData>

    suspend fun requestMyLevelData() : Result<ResponseMyLevelData.ResultMyLevelData>
}