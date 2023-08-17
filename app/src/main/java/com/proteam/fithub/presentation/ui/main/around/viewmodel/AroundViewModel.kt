package com.proteam.fithub.presentation.ui.main.around.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.data.remote.response.ResponseLocationData
import com.proteam.fithub.domain.repository.ExerciseRepository
import com.proteam.fithub.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AroundViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val repository : LocationRepository
): ViewModel() {
    private val _currentMarkerItems = MutableLiveData<MutableList<ResponseLocationData.LocationItems>>()
    val currentMarkerItems : LiveData<MutableList<ResponseLocationData.LocationItems>> = _currentMarkerItems

    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation : LiveData<Location> = _currentLocation

    private val _targetLocation = MutableLiveData<Pair<Double, Double>>()
    val targetLocation : LiveData<Pair<Double, Double>> = _targetLocation

    private val _isResearchNeed = MutableLiveData<Boolean>(false)
    val isResearchNeed : LiveData<Boolean> = _isResearchNeed

    private val _exerciseFilters = MutableLiveData<MutableList<ResponseExercises.ExercisesList>>()
    val exerciseFilters: LiveData<MutableList<ResponseExercises.ExercisesList>> = _exerciseFilters

    private val _selectedFilter = MutableLiveData<Int>()
    val selectedFilter: LiveData<Int> = _selectedFilter

    init {
        requestExerciseFilterList()
    }

    fun setCurrentLocation(location : Location) {
        _currentLocation.value = location
    }

    fun setTargetLocation(location : Pair<Double, Double>) {
        _targetLocation.value = location
    }

    fun setSearchNeed(isNeed : Boolean) {
        _isResearchNeed.value = isNeed
    }

    fun requestLocationData(filter : Int?) {
        viewModelScope.launch {
            repository.requestLocationData(filter ?: 0, targetLocation.value?.second.toString(), targetLocation.value?.first.toString(), currentLocation.value?.longitude.toString(), currentLocation.value?.latitude.toString())
                .onSuccess { _currentMarkerItems.value = it.facilitiesList as MutableList }
        }
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
}