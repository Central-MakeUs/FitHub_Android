package com.proteam.fithub.presentation.ui.search.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    var userInputKeyword = MutableLiveData<String>().apply { value = "" }

    fun initUserInputKeyword() {
        userInputKeyword.value = ""
    }
}