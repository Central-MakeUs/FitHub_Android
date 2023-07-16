package com.proteam.fithub.presentation.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _curFragmentTag = MutableLiveData<String>()
    val curFragmentTag : LiveData<String> = _curFragmentTag

    fun setFragmentTag(tag : String) {
        _curFragmentTag.value = tag
    }
}