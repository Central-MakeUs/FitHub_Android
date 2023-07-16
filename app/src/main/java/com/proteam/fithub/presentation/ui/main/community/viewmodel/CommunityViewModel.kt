package com.proteam.fithub.presentation.ui.main.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository
): ViewModel() {
    private val _isFabClicked = MutableLiveData<Boolean>()
    val isFabClicked : LiveData<Boolean> = _isFabClicked

    private val _exerciseFilters = MutableLiveData<MutableList<ResponseExercises.ExercisesList>>()
    val exerciseFilters : LiveData<MutableList<ResponseExercises.ExercisesList>> = _exerciseFilters

    private val _isRecentSort = MutableLiveData<Boolean>(true)
    val isRecentSort : LiveData<Boolean> = _isRecentSort

    init {
        loadExerciseFilterList()
    }

    private fun loadExerciseFilterList() {
        viewModelScope.launch {
            exerciseRepository.requestExercises()
                .onSuccess { _exerciseFilters.value = it as MutableList }
        }
    }

    fun changeRecentSort(state : Boolean) {
        _isRecentSort.value = state
    }

    fun openFabDialog() {
        _isFabClicked.value = true
    }

    fun closeFabDialog() {
        _isFabClicked.value = false
    }


}