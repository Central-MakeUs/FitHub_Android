package com.proteam.fithub.presentation.ui.main.mypage.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseMyPageData
import com.proteam.fithub.domain.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(private val myPageRepo : MyPageRepository): ViewModel() {
    private val _myPageData = MutableLiveData<ResponseMyPageData.ResultMyPageData>()
    val myPageData : LiveData<ResponseMyPageData.ResultMyPageData> = _myPageData

    private val _deleteProfileStatus = MutableLiveData<Int>()
    val deleteProfileStatus : LiveData<Int> = _deleteProfileStatus

    private val _changeProfileStatus = MutableLiveData<Int>()
    val changeProfileStatus : LiveData<Int> = _changeProfileStatus

    fun requestMyPageData() {
        viewModelScope.launch {
            myPageRepo.requestMyPageData()
                .onSuccess { _myPageData.value = it }
        }
    }

    fun requestProfileToDefault() {
        viewModelScope.launch {
            myPageRepo.requestChangeProfileToDefault()
                .onSuccess { _deleteProfileStatus.value = it.code }
        }
    }

    fun setUserSelectedProfile(path: String) {
        viewModelScope.launch {
            myPageRepo.requestChangeProfileImage(path.mapToMultipart())
                .onSuccess { _changeProfileStatus.value = it.code }
        }
    }

    private fun String.mapToMultipart(): MultipartBody.Part {
        val file = File(this)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("newProfile", file.name, requestFile)
    }

}