package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseMainExerciseData(
    val result : ResultMainExerciseData
) : BaseResponse() {
    data class ResultMainExerciseData(
        val currentExerciseCategory : Int
    )
}
