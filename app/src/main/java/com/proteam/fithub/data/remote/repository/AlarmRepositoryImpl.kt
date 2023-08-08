package com.proteam.fithub.data.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.proteam.fithub.data.remote.response.ResponseAlarmData
import com.proteam.fithub.data.remote.service.AlarmService
import com.proteam.fithub.data.remote.source.AlarmPagingSource
import com.proteam.fithub.domain.repository.AlarmRepository
import com.proteam.fithub.domain.source.AlarmSource
import com.proteam.fithub.presentation.util.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val service: AlarmService,
    private val source: AlarmSource
) : AlarmRepository {
    override fun requestAlarmData(): Flow<PagingData<ResponseAlarmData.ResultAlarmData>> {
        return Pager(PagingConfig(pageSize = 12)) {
            AlarmPagingSource(Dispatchers.IO, service)
        }.flow
    }

    override suspend fun requestAlarmReadData(alarmId: Int): Result<BaseResponse> {
        return source.requestAlarmReadData(alarmId)
    }


}