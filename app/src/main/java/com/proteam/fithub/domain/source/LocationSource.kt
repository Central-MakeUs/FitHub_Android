package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.response.ResponseKeywordsData
import com.proteam.fithub.data.remote.response.ResponseLocationData

interface LocationSource {

    suspend fun requestLocationDataWithoutKeyword(categoryId : Int, x : String, y : String, userX : String, userY : String, keyword : String) : Result<ResponseLocationData.ResultLocationData>

    suspend fun requestLocationDataWithKeyword(categoryId : Int, userX : String, userY : String, keyword : String) : Result<ResponseLocationData.ResultLocationData>

    suspend fun requestKeywordsData() : Result<ResponseKeywordsData.ResultKeywordsData>

    suspend fun requestLocationDataByKeyword(x : String, y : String, userX : String, userY : String, keyword : String) : Result<ResponseLocationData.ResultLocationData>
}