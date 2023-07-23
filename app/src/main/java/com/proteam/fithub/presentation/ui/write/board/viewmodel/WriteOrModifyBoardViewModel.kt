package com.proteam.fithub.presentation.ui.write.board.viewmodel

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.domain.repository.ArticleRepository
import com.proteam.fithub.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class WriteOrModifyBoardViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val articleRepository: ArticleRepository
) :
    ViewModel() {

    init {
        loadExercises()
    }

    private val _type = MutableLiveData<String>()
    val type: LiveData<String> = _type

    //:TODO 수정
    private val _userSelectedImages = MutableLiveData<MutableList<Uri>>()
    val userSelectedImages: LiveData<MutableList<Uri>> = _userSelectedImages

    private val _imagePaths = MutableLiveData<MutableList<Uri>>()
    val imagePaths : LiveData<MutableList<Uri>> = _imagePaths

    private val _exercises = MutableLiveData<MutableList<ResponseExercises.ExercisesList>>()
    val exercises: LiveData<MutableList<ResponseExercises.ExercisesList>> = _exercises

    /** User Input **/
    var userInputTitle = MutableLiveData<String>().apply { value = "" }
    var userInputContent = MutableLiveData<String>().apply { value = "" }
    var userInputTag = MutableLiveData<String>().apply { value = "" }

    private val _userInputTagList = MutableLiveData<MutableList<String>>()
    val userInputTagList: LiveData<MutableList<String>> = _userInputTagList
    private var toolsForUserInputTagList = mutableListOf<String>()

    private val _userSelectExercise = MutableLiveData<ResponseExercises.ExercisesList>()
    val userSelectExercise: LiveData<ResponseExercises.ExercisesList> = _userSelectExercise

    var saveButtonEnabled = MutableLiveData<Boolean>(false)

    private val _saveState = MutableLiveData<Int>()
    val saveState: LiveData<Int> = _saveState
    fun setType(type: String) {
        _type.value = type
    }

    fun setSelectedImages(path: MutableList<Uri>) {
        _userSelectedImages.postValue(path.map { it.toString().substring(7).toUri() } as MutableList)

        checkSaveEnabled()
    }

    fun setPathForDelete(paths : MutableList<Uri>) {
        _imagePaths.postValue(paths)
    }

    fun dropSelectedImages(position: Int) {
        _userSelectedImages.value = _userSelectedImages.value.apply { this?.removeAt(position) }
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

    private fun loadExercises() {
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
        saveButtonEnabled.postValue(
            (userInputTitle.value != null) && (_userSelectExercise.value != null) && (userInputContent.value != null))
    }


    fun postArticle(path : List<String>) {
        viewModelScope.launch {
            articleRepository.requestPostArticleData(
                userSelectExercise.value!!.id,
                userInputTitle.value!!,
                userInputContent.value!!,
                userSelectExercise.value!!.name,
                userInputTagList.value,
                path.mapToMultipart()
            )
                .onSuccess { _saveState.value = it.code }
                .onFailure { _saveState.value = it.message?.toInt() }
        }
    }

    private fun List<String>.mapToMultipart(): MutableList<MultipartBody.Part> {
        val files = mutableListOf<MultipartBody.Part>()
        for (i in this) {
            files.add(i.convertToMultiPart())
        }
        return files
    }

    private fun String.convertToMultiPart(): MultipartBody.Part {
        val cont = File(this)
        val requestFile = cont.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("pictureList", cont.name, requestFile)
    }

}