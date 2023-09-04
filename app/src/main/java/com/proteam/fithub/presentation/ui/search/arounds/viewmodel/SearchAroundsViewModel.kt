package com.proteam.fithub.presentation.ui.search.arounds.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.response.ResponseLocationData
import com.proteam.fithub.domain.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchAroundsViewModel @Inject constructor(private val repository: LocationRepository) :
    ViewModel() {

    var userInputKeyword = MutableLiveData<String>().apply { value = "" }

    private val _currentLocation = MutableLiveData<Pair<Double, Double>>()
    val currentLocation : LiveData<Pair<Double, Double>> = _currentLocation

    private val _currentMarkerItems = MutableLiveData<MutableList<ResponseLocationData.LocationItems>>()
    val currentMarkerItems : LiveData<MutableList<ResponseLocationData.LocationItems>> = _currentMarkerItems

    private val _resultCount = MutableLiveData<Int>()
    val resultCount : LiveData<Int> = _resultCount

    fun initUserInputKeyword() {
        userInputKeyword.value = ""
    }

    fun setCurrentLocation(location : Pair<Double, Double>) {
        _currentLocation.value = location
    }

    fun requestSearchData() {
        viewModelScope.launch {
            userInputKeyword.value?.let { it2 ->
                repository.requestLocationDataWithKeyword(0, currentLocation.value?.second.toString(), currentLocation.value?.first.toString(), it2)
                    .onSuccess {
                        _currentMarkerItems.value = it.facilitiesList.toMutableList()
                        _resultCount.value = it.size
                    }
            }
        }
    }
}