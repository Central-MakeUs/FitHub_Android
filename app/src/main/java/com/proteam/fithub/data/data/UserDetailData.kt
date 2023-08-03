package com.proteam.fithub.data.data

data class UserDetailData(
    val userNickname : String,
    val category : String,
    val exp : Int,
    val maxExp : Int,
    val monthRecordCount : Int,
    val contiguousRecordCount : Int,
    val gradeName : String,
    val gradeImageUrl : String
    )
