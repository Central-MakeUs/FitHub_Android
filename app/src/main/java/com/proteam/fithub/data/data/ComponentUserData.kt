package com.proteam.fithub.data.data

data class ComponentUserData (
    val ownerId : Int,
    val nickname : String,
    val profileUrl : String?,
    val mainExerciseInfo : UserExerciseInfo?
) {
    data class UserExerciseInfo(
        val category : String?,
        val level : Int,
        val gradeName : String
    )
}