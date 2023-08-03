package com.proteam.fithub.data.data

data class HashTagData (
    val hashtags : List<HashTagResult>?
) {
    data class HashTagResult(
        val hashTagId : Int,
        val name : String
    )
}