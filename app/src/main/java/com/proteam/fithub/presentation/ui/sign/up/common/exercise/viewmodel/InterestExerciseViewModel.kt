package com.proteam.fithub.presentation.ui.sign.up.common.exercise.viewmodel

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
class InterestExerciseViewModel @Inject constructor(private val exerciseRepository: ExerciseRepository): ViewModel() {

    private val _exercises = MutableLiveData<MutableList<ResponseExercises.ExercisesList>>()
    val exercises: LiveData<MutableList<ResponseExercises.ExercisesList>> = _exercises

    private val _selectedExercises = MutableLiveData<MutableList<Int>>().apply { value = mutableListOf() }
    val selectExercises: LiveData<MutableList<Int>> = _selectedExercises
    private val toolSelectInterestSports = mutableListOf<Int>()

    fun requestExercises() {
        viewModelScope.launch {
            exerciseRepository.requestExercises()
                .onSuccess { _exercises.value = it as MutableList}
        }
    }

    fun addSelectInterestSports(sportsId: Int) {
        toolSelectInterestSports.add(sportsId)
        notifyExercises()
    }

    fun removeSelectInterestSports(sportsId: Int) {
        toolSelectInterestSports.remove(sportsId)
        notifyExercises()
    }

    private fun notifyExercises() {
        _selectedExercises.value = toolSelectInterestSports
    }
}