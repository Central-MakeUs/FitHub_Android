package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseExercises (
    val result : ResultExercises
) : BaseResponse() {
    data class ResultExercises(
        val categoryList : List<ExercisesList>,
        val size : Int
    )

    data class ExercisesList (
        val categoryId : Int,
        val categoryImage : String,
        val name : String
            )
}