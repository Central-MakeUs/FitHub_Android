package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseExercises (
    val result : List<ExercisesList>
) : BaseResponse() {
    data class ExercisesList (
        val id : Int,
        val imageUrl : String,
        val name : String
            )
}