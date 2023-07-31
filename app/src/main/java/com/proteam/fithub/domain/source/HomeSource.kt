package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.response.ResponseHomeData

interface HomeSource {

    suspend fun requestHomeData() : Result<ResponseHomeData>
}