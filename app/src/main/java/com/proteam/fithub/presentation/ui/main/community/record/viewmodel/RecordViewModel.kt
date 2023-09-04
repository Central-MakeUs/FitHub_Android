package com.proteam.fithub.presentation.ui.main.community.record.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.domain.repository.RecordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(private val recordRepository: RecordRepository): ViewModel() {

    private val _recordSortType = MutableLiveData(true) /** True : 최신 / False : 인기 **/
    val recordSortType : LiveData<Boolean> = _recordSortType

    fun setSortType(filter : Boolean) {
        _recordSortType.value = filter
    }

    fun requestWhenCommunity(filter : Int) : Flow<PagingData<ResponseCertificateData.ResultCertificateData>> {
        return recordRepository.requestCertificateDataWithCategory(recordSortType.value!!.convertSortType(), filter)
    }

    private fun Boolean.convertSortType() : String {
        return if(this) "date" else "like"
    }
}