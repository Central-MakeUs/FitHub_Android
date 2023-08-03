package com.proteam.fithub.presentation.ui.bookmark.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.domain.repository.BookmarkRepository
import com.proteam.fithub.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookMarkViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val bookmarkRepository: BookmarkRepository
): ViewModel() {

    private val _exerciseFilters = MutableLiveData<MutableList<ResponseExercises.ExercisesList>>()
    val exerciseFilters: LiveData<MutableList<ResponseExercises.ExercisesList>> = _exerciseFilters

    private val _selectedFilter = MutableLiveData<Int>()
    val selectedFilter: LiveData<Int> = _selectedFilter

    init {
        requestExerciseFilterList()
    }

    fun setSelectedFilter(index: Int) {
        _selectedFilter.value = index
    }

    private fun requestExerciseFilterList() {
        viewModelScope.launch {
            exerciseRepository.requestExercises()
                .onSuccess { _exerciseFilters.value = it as MutableList }
        }
    }

    fun requestBookmarkData(filter : Int) : Flow<PagingData<ResponseArticleData.ResultArticleData>> {
        return bookmarkRepository.requestBookmarkData(filter)
    }
}