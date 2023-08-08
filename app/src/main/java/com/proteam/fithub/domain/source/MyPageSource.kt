package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.response.ResponseMyInfoData
import com.proteam.fithub.data.remote.response.ResponseMyPageData

interface MyPageSource {

    suspend fun requestMyPageData() : Result<ResponseMyPageData.ResultMyPageData>

    suspend fun requestMyInfoData() : Result<ResponseMyInfoData.ResultMyInfoData>
}