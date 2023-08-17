package com.proteam.fithub.domain.repository

import com.proteam.fithub.data.remote.response.ResponseKeywordsData
import com.proteam.fithub.data.remote.response.ResponseLocationData

interface LocationRepository {

    suspend fun requestLocationData(categoryId : Int, x : String, y : String, userX : String, userY : String) : Result<ResponseLocationData.ResultLocationData>

    suspend fun requestKeywordsData() : Result<ResponseKeywordsData.ResultKeywordsData>

    suspend fun requestLocationDataByKeyword(x : String, y : String, userX : String, userY : String, keyword : String) : Result<ResponseLocationData.ResultLocationData>
}