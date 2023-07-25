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
    private val certificateRepository: CertificateRepository,
    private val articleRepository: ArticleRepository
) : ViewModel() {
    private val _isFabClicked = MutableLiveData<Boolean>()
    val isFabClicked: LiveData<Boolean> = _isFabClicked

    private val _exerciseFilters = MutableLiveData<MutableList<ResponseExercises.ExercisesList>>()
    val exerciseFilters: LiveData<MutableList<ResponseExercises.ExercisesList>> = _exerciseFilters

    private val _selectedFilter = MutableLiveData<Int>()
    val selectedFilter: LiveData<Int> = _selectedFilter

    private val _currentFragment = MutableLiveData<Int>()
    val currentFragment : LiveData<Int> = _currentFragment

    private val _isCertificateRecentSort = MutableLiveData<Boolean>(true)
    val isCertificateRecentSort: LiveData<Boolean> = _isCertificateRecentSort

    private val _isBoardRecentSort = MutableLiveData<Boolean>(true)
    val isBoardRecentSort: LiveData<Boolean> = _isBoardRecentSort

    private val _heartResult =
        MutableLiveData<ResponseCertificateHeartClicked.ResultCertificateHeartClicked>()
    val heartResult: LiveData<ResponseCertificateHeartClicked.ResultCertificateHeartClicked> =
        _heartResult

    init {
        requestExerciseFilterList()
    }

    private fun requestExerciseFilterList() {
        viewModelScope.launch {
            exerciseRepository.requestExercises()
                .onSuccess { _exerciseFilters.value = it as MutableList }
        }
    }

    fun notifyChangeFragment(index : Int) {
        _currentFragment.value = index
    }

    fun changeCertificateRecentSort(state: Boolean) {
        _isCertificateRecentSort.value = state
    }

    fun changeBoardRecentSort(state : Boolean) {
        _isBoardRecentSort.value = state
    }

    fun openFabDialog() {
        _isFabClicked.value = true
    }

    fun closeFabDialog() {
        _isFabClicked.value = false
    }

    fun setSelectedFilter(index: Int) {
        _selectedFilter.value = index
    }

    fun requestCertificateItems(
        type: String,
        category: Int
    ): Flow<PagingData<ResponseCertificateData.ResultCertificateData>> {
        return certificateRepository.requestCertificateData(type, category)
    }

    fun requestArticleItems(
        type : String,
        category: Int
    ) : Flow<PagingData<ResponseArticleData.ResultArticleData>> {
        return articleRepository.requestArticleData(type, category)
    }

    fun requestHeartClicked(recordId: Int) {
        viewModelScope.launch {
            certificateRepository.requestCertificateHeartClicked(recordId)
                .onSuccess { _heartResult.value = it }
        }
    }

}