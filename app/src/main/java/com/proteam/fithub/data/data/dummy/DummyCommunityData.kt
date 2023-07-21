package com.proteam.fithub.data.data.dummy

import com.proteam.fithub.data.data.ComponentUserData

data class DummyCommunityData (
    val user : ComponentUserData,
    val title : String,
    val content : String,
    val tag : String,
    val likeCount : String,
    val commentCount : String,
    val thumbnail : Int
)