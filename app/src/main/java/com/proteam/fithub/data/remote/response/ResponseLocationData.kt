package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseLocationData(
    val result : ResultLocationData
) : BaseResponse() {
    data class ResultLocationData(
        val facilitiesList : List<LocationItems>,
        val size : Int,
        val userX : String,
        val userY : String
    )

    data class LocationItems(
        val name : String,
        val address : String,
        val roadAddress : String,
        val imageUrl : String,
        val phoneNumber : String,
        val category : String,
        val x : String,
        val y : String,
        val dist : String
    )
}
