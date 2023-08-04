package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseMyPageData (
    val result : ResultMyPageData
) : BaseResponse() {
    data class ResultMyPageData(
        val myInfo : ResultMyInfo,
        val myExerciseList : List<ExerciseData>
    )

    data class ResultMyInfo(
        val ownerId : Int,
        val nickname : String,
        val profileUrl : String,
        val mainExerciseInfo : ExerciseData
    )

    data class ExerciseData(
        val category : String,
        val level : Int,
        val gradeName : String
    )
}
