package com.proteam.fithub.domain.repository

import com.proteam.fithub.data.remote.response.ResponseHomeData

interface HomeRepository {

    suspend fun requestHomeData() : Result<ResponseHomeData>
}