package com.proteam.fithub.data.remote.response

import com.proteam.fithub.presentation.util.BaseResponse

data class ResponseMyLevelData (
    val result : ResultMyLevelData
) : BaseResponse() {
    data class ResultMyLevelData(
        val myLevelInfo : MyLevelInfo,
        val fithubLevelInfo: FithubLevelInfo
    )

    data class MyLevelInfo(
        val levelIconUrl : String,
        val level : Int,
        val levelName : String,
        val levelSummary : String,
        val levelDescription : String
    )

    data class FithubLevelInfo (
        val expSummary : String,
        val expDescription : String,
        val comboSummary : String,
        val comboDescription : String,
        val fithubLevelList : List<FithubLevelDetail>

            )

    data class FithubLevelDetail(
        val levelIconUrl : String,
        val level : Int,
        val levelName : String
    )
}