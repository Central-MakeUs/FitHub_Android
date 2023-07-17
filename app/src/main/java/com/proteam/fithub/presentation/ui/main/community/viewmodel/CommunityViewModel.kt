package com.proteam.fithub.presentation.ui.main.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ExamCertificateData
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

    private val _certificateItems = MutableLiveData<MutableList<ExamCertificateData>>()
    val certificateItems : LiveData<MutableList<ExamCertificateData>> = _certificateItems

    init {
        requestExerciseFilterList()
    }

    private fun requestExerciseFilterList() {
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

    fun requestCertificateItems() {
        _certificateItems.value = certificate()
    }

    /** Dummy **/
    private fun certificate() : MutableList<ExamCertificateData> = mutableListOf<ExamCertificateData>().apply {
        for(i in 0 until 6) {
            add(ExamCertificateData(i, R.drawable.ic_launcher_background, true, 5))
        }
    }


}