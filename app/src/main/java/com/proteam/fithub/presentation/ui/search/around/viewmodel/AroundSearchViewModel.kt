package com.proteam.fithub.presentation.ui.search.around.viewmodel

import android.location.Location
import android.util.Log
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
class AroundSearchViewModel @Inject constructor(private val locationRepository: LocationRepository) :
    ViewModel() {
    private val _currentMarkerItems =
        MutableLiveData<MutableList<ResponseLocationData.LocationItems>>()
    val currentMarkerItems: LiveData<MutableList<ResponseLocationData.LocationItems>> =
        _currentMarkerItems

    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location> = _currentLocation

    var userInputKeyword = MutableLiveData<String>().apply { value = "" }


    fun setCurrentLocation(location: Location) {
        _currentLocation.value = location
    }

    fun initKeyword() {
        userInputKeyword.value = ""
    }

    fun requestSearchLocationData(x: String?, y: String?) {
        viewModelScope.launch {
            currentLocation.value?.let {
                locationRepository.requestLocationDataByKeyword(
                    x ?: "",
                    y ?: "",
                    it.longitude.toString(),
                    it.latitude.toString(),
                    userInputKeyword.value!!
                )
                    .onSuccess { _currentMarkerItems.value = it.facilitiesList.toMutableList() }
                    .onFailure { Log.e("----", "requestSearchLocationData: ${it.message}", ) }
            }
        }
    }

}