package com.proteam.fithub.presentation.ui.search.community.normal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchDefaultViewModel @Inject constructor(private val searchRepository: SearchRepository) : ViewModel() {

    private val _examKeywords = MutableLiveData<MutableList<String>>()
    val examKeywords: LiveData<MutableList<String>> = _examKeywords

    init {
        requestKeywordData()
    }

    private fun requestKeywordData() {
        viewModelScope.launch {
            searchRepository.getSearchKeywords()
                .onSuccess { _examKeywords.value = it.keywordList.toMutableList() }
        }
    }
}