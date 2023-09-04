package com.proteam.fithub.presentation.ui.main.community.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.proteam.fithub.data.remote.response.ResponseArticleData
import com.proteam.fithub.data.remote.response.ResponseCertificateData
import com.proteam.fithub.data.remote.response.ResponseCertificateHeartClicked
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.domain.repository.ArticleRepository
import com.proteam.fithub.domain.repository.CertificateRepository
import com.proteam.fithub.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val certificateRepository: CertificateRepository
) : ViewModel() {
    private val _isFabClicked = MutableLiveData<Boolean>()
    val isFabClicked: LiveData<Boolean> = _isFabClicked

    private val _exerciseFilters = MutableLiveData<MutableList<ResponseExercises.ExercisesList>>()
    val exerciseFilters: LiveData<MutableList<ResponseExercises.ExercisesList>> = _exerciseFilters

    private val _selectedFilter = MutableLiveData<Int>()
    val selectedFilter: LiveData<Int> = _selectedFilter

    private var _todayCertificateData = MutableLiveData<Boolean?>()
    val todayCertificateData : LiveData<Boolean?> = _todayCertificateData


    init {
        requestExerciseFilterList()
    }

    /** Filter **/

    fun setSelectedFilter(index: Int) {
        _selectedFilter.value = index
    }

    private fun requestExerciseFilterList() {
        viewModelScope.launch {
            exerciseRepository.requestExercises()
                .onSuccess { _exerciseFilters.value = it as MutableList }
        }
    }

    /** Floating Action Button **/

    fun openFabDialog() {
        _isFabClicked.value = true
    }

    fun closeFabDialog() {
        _isFabClicked.value = false
    }

    fun checkTodaysCertificate() {
        viewModelScope.launch {
            certificateRepository.requestCertificateToday()
                .onSuccess { _todayCertificateData.value = it.isWrite }
        }
    }

    fun initCertificateState() {
        _todayCertificateData.value = null
    }


}