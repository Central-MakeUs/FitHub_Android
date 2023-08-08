package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseOtherUserProfileData(
    val result : ResultOtherUserProfileData
) : BaseResponse() {
    data class ResultOtherUserProfileData(
        val profileUrl : String,
        val nickname : String,
        val mainExerciseInfo : ResponseMyPageData.ExerciseData
    )
}
