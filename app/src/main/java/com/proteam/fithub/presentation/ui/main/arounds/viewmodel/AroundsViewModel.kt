package com.proteam.fithub.presentation.ui.main.arounds.viewmodel

import android.location.Location
import android.util.Log
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
class AroundsViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val locationRepository: LocationRepository): ViewModel() {

    private val _exerciseFilters = MutableLiveData<MutableList<ResponseExercises.ExercisesList>>()
    val exerciseFilters: LiveData<MutableList<ResponseExercises.ExercisesList>> = _exerciseFilters

    private val _currentMarkerItems = MutableLiveData<MutableList<ResponseLocationData.LocationItems>>()
    val currentMarkerItems : LiveData<MutableList<ResponseLocationData.LocationItems>> = _currentMarkerItems

    private val _filteredMarkerItems = MutableLiveData<MutableList<ResponseLocationData.LocationItems>>()
    val filteredMarkerItems : LiveData<MutableList<ResponseLocationData.LocationItems>> = _filteredMarkerItems

    private val _selectedMarkerItems = MutableLiveData<ResponseLocationData.LocationItems>()
    val selectedMarkerItems : LiveData<ResponseLocationData.LocationItems> = _selectedMarkerItems

    private val _selectedFilter = MutableLiveData<Int>(0)
    val selectedFilter: LiveData<Int> = _selectedFilter

    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation : LiveData<Location> = _currentLocation

    private val _targetLocation = MutableLiveData<Pair<Double, Double>?>()
    val targetLocation : LiveData<Pair<Double, Double>?> = _targetLocation

    private val _isResearchNeed = MutableLiveData<Boolean>(false)
    val isResearchNeed : LiveData<Boolean> = _isResearchNeed

    private val _isCardShowing = MutableLiveData(false)
    val isCardShowing : LiveData<Boolean> = _isCardShowing

    private val _userInputKeyword = MutableLiveData<String>().apply { value = "" }
    val userInputKeyword : LiveData<String> = _userInputKeyword

    init {
        requestExerciseFilterList()
    }

    private fun requestExerciseFilterList() {
        viewModelScope.launch {
            exerciseRepository.requestExercises()
                .onSuccess { _exerciseFilters.value = it as MutableList }
        }
    }

    fun setSelectedFilter(index: Int) {
        _selectedFilter.value = index
    }

    fun setFilteredItems(index : Int) {
        if(index == 0) {
            _filteredMarkerItems.value = currentMarkerItems.value
        }
        else {
            currentMarkerItems.value?.let { _filteredMarkerItems.value = it.filter { it.categoryId == index } as MutableList }
        }
    }

    fun setCurrentLocation(location : Location) {
        _currentLocation.value = location
    }

    fun initTargetLocation() {
        _targetLocation.value = null
    }

    fun setTargetLocation(location : Pair<Double, Double>) {
        _targetLocation.value = location
    }

    fun setSearchNeed(isNeed : Boolean) {
        _isResearchNeed.value = isNeed
    }

    fun requestLocationDataWithoutKeyword() {
        setIsCardShowing(false)
        viewModelScope.launch {
            locationRepository.requestLocationData(0, targetLocation.value?.second?.toString() ?: currentLocation.value?.longitude.toString() , targetLocation.value?.first?.toString() ?: currentLocation.value?.latitude.toString(), currentLocation.value?.longitude.toString(), currentLocation.value?.latitude.toString(), userInputKeyword.value ?: "")
                .onSuccess {
                    _currentMarkerItems.value = it.facilitiesList as MutableList
                }
        }
    }

    fun setSelectedItem(item : ResponseLocationData.LocationItems) {
        _selectedMarkerItems.value = item
    }

    fun setIsCardShowing(isShow : Boolean) {
        _isCardShowing.value = isShow
    }

    fun setCurrentMarkerItems(items : List<ResponseLocationData.LocationItems>) {
        _currentMarkerItems.value = items as MutableList
    }

    fun initKeyword() {
        _userInputKeyword.value = ""
    }

    fun setUserInputKeyword(keyword : String) {
        _userInputKeyword.value = keyword
    }
}