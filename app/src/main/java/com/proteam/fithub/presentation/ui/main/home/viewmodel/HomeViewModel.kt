package com.proteam.fithub.presentation.ui.main.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.data.remote.response.ResponseHomeData
import com.proteam.fithub.domain.repository.ExerciseRepository
import com.proteam.fithub.domain.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val exerciseRepository: ExerciseRepository
    ): ViewModel() {
    private val _homeData = MutableLiveData<ResponseHomeData>()
    val homeData : LiveData<ResponseHomeData> = _homeData

    private val _exercises = MutableLiveData<MutableList<ResponseExercises.ExercisesList>>()
    val exercises : LiveData<MutableList<ResponseExercises.ExercisesList>> = _exercises

    private val homeState = MutableLiveData<Boolean>(false)
    private val exerciseState = MutableLiveData<Boolean>(false)

    private val _totalState = MutableLiveData<Boolean>()
    val totalState : LiveData<Boolean> = _totalState


    fun requestExerciseCategory() {
        viewModelScope.launch {
            exerciseRepository.requestExercises()
                .onSuccess {
                    _exercises.value = it as MutableList
                    exerciseState.value = true
                    checkTotalStatus()
                }
        }
    }

    fun requestHomeData() {
        viewModelScope.launch {
            homeRepository.requestHomeData()
                .onSuccess {
                    _homeData.value = it
                    homeState.value = true
                    checkTotalStatus()
                }
        }
    }

    private fun checkTotalStatus() {
        _totalState.value = homeState.value!! && exerciseState.value!!
    }
}