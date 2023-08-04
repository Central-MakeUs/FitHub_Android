package com.proteam.fithub.domain.repository

import com.proteam.fithub.data.remote.response.ResponseMyPageData

interface MyPageRepository {

    suspend fun requestMyPageData() : Result<ResponseMyPageData.ResultMyPageData>

}