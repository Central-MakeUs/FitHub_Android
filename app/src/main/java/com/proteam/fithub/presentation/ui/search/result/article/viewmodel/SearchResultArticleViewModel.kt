package com.proteam.fithub.presentation.ui.search.result.article.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchResultArticleViewModel @Inject constructor(private val searchRepository: SearchRepository): ViewModel() {

    private val _isArticleRecentSort = MutableLiveData<Boolean>(true)
    val isArticleRecentSort: LiveData<Boolean> = _isArticleRecentSort

    fun changeArticleRecentSort(state: Boolean) {
        _isArticleRecentSort.value = state
    }

    fun requestSearchCertificateResult(type : String, keyword: String) : Flow<PagingData<ResponseArticleData.ResultArticleData>> {
        return searchRepository.getSearchArticleData(type, keyword)
    }
}