package com.proteam.fithub.presentation.ui.write.certificate.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.response.ResponseCertificateDetailData
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.domain.repository.CertificateRepository
import com.proteam.fithub.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class WriteOrModifyCertificateViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val certificateRepository: CertificateRepository
) :
    ViewModel() {

    init {
        requestLoadExercises()
    }

    private val _type = MutableLiveData<String>()
    val type: LiveData<String> = _type

    private val _userSelectedImage = MutableLiveData<Any>()
    val userSelectedImage: LiveData<Any> = _userSelectedImage

    private val _imagePaths = MutableLiveData<Uri>()
    val imagePaths : LiveData<Uri> = _imagePaths

    private val _exercises = MutableLiveData<MutableList<ResponseExercises.ExercisesList>>()
    val exercises: LiveData<MutableList<ResponseExercises.ExercisesList>> = _exercises

    /** User Input **/
    var userInputContent = MutableLiveData<String>().apply { value = "" }
    var userInputTag = MutableLiveData<String>().apply { value = "" }

    private val _userInputTagList = MutableLiveData<MutableList<String>>()
    val userInputTagList: LiveData<MutableList<String>> = _userInputTagList
    private var toolsForUserInputTagList = mutableListOf<String>()

    private val _userSelectExercise = MutableLiveData<ResponseExercises.ExercisesList>()
    val userSelectExercise: LiveData<ResponseExercises.ExercisesList> = _userSelectExercise

    var saveButtonEnabled = MutableLiveData<Boolean>(false)

    private val _saveState = MutableLiveData<Int>()
    val saveState : LiveData<Int> = _saveState

    fun setType(type: String) {
        _type.value = type
    }

    fun setSelectedImage(path: Any) {
        _userSelectedImage.value = path
        checkSaveEnabled()
    }

    fun setPathForDelete(paths : Uri) {
        _imagePaths.postValue(paths)
    }

    fun initTag() {
        addToTagList(userInputTag.value!!)
        userInputTag.value = ""
    }

    private fun addToTagList(tag: String) {
        toolsForUserInputTagList.add(tag)
        notifyTagList()
    }

    fun removeFromTagList(index: Int) {
        toolsForUserInputTagList.removeAt(index - 1)
        notifyTagList()
    }

    private fun notifyTagList() {
        _userInputTagList.value = toolsForUserInputTagList
    }

    private fun requestLoadExercises() {
        viewModelScope.launch {
            exerciseRepository.requestExercises()
                .onSuccess { _exercises.value = it as MutableList }
        }
    }

    fun setUserSelectedExercise(exercise: ResponseExercises.ExercisesList) {
        _userSelectExercise.value = exercise
        checkSaveEnabled()
    }

    fun checkSaveEnabled() {
        saveButtonEnabled.value =
            (_userSelectedImage.value != null) && (_userSelectExercise.value != null) && (userInputContent.value != null)
    }

    fun requestPostCertificate(path : String) {
        Log.e("----", "requestPostCertificate: ${userInputContent.value!!} / ${userInputTagList.value!!}", )
        viewModelScope.launch {
            certificateRepository.requestPostCertificateData(
                userSelectExercise.value!!.id,
                userInputContent.value!!,
                userSelectExercise.value!!.name,
                userInputTagList.value,
                path.mapToMultipart()
            )
                .onSuccess { _saveState.value = it.code }
                .onFailure { _saveState.value = it.message?.toInt() }
        }
    }

    private fun String.mapToMultipart(): MultipartBody.Part {
        val file = File(this)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    fun requestLegacyData() {
        viewModelScope.launch {
            certificateRepository.requestCertificateDetail(type.value!!.toInt())
                .onSuccess { setLegacyDataToUi(it) }
        }
    }

    private fun setLegacyDataToUi(data : ResponseCertificateDetailData.ResultCertificateDetailData) {
        data.apply {
            _userSelectedImage.value = pictureImage
            toolsForUserInputTagList = (hashtags.hashtags.map { it.name } as MutableList).also { it.removeLast() }
            userInputContent.value = contents
            _userSelectExercise.value = ResponseExercises.ExercisesList(recordCategory.categoryId, "", recordCategory.name)
        }
        notifyTagList()

    }

}