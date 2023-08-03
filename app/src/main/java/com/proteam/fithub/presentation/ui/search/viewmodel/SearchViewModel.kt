package com.proteam.fithub.presentation.ui.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.response.ResponseSearchTotalData
import com.proteam.fithub.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: SearchRepository): ViewModel() {
    var userInputKeyword = MutableLiveData<String>().apply { value = "" }

    private val _searchCode = MutableLiveData<Int>()
    var searchCode : LiveData<Int> = _searchCode

    private val _totalSearchData = MutableLiveData<ResponseSearchTotalData.ResultSearchTotalData>()
    val totalSearchData : LiveData<ResponseSearchTotalData.ResultSearchTotalData> = _totalSearchData

    fun initUserInputKeyword() {
        userInputKeyword.value = ""
    }

    fun requestSearchTotalData() {
        viewModelScope.launch {
            repository.getSearchTotalData(userInputKeyword.value!!)
                .onSuccess {
                    setSearchCode(it.code)
                    setTotalSearchData(it.result)
                }
                .onFailure { setSearchCode(it.message!!.toInt()) }
        }
    }

    private fun setSearchCode(code : Int) {
        _searchCode.value = code
    }

    fun initSearchCode() {
        _searchCode.value = 0
    }

    private fun setTotalSearchData(data : ResponseSearchTotalData.ResultSearchTotalData) {
        _totalSearchData.value = data
    }
}