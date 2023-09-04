package com.proteam.fithub.presentation.ui.search.community.result.certificate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.kakao.sdk.common.KakaoSdk.type
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchResultCertificateViewModel @Inject constructor(private val searchRepository: SearchRepository) :
    ViewModel() {

    private val _isCertificateRecentSort = MutableLiveData<Boolean>(true)
    val isCertificateRecentSort: LiveData<Boolean> = _isCertificateRecentSort


    fun changeCertificateRecentSort(state: Boolean) {
        _isCertificateRecentSort.value = state
    }

    fun requestSearchCertificateResult(type : String, keyword: String) : Flow<PagingData<ResponseCertificateData.ResultCertificateData>> {
        return searchRepository.getSearchCertificateData(type, keyword)
    }
}