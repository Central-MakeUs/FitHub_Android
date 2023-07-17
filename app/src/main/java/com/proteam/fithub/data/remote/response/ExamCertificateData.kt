package com.proteam.fithub.data.remote.response

data class ExamCertificateData (
    val index : Int,
    val path : Int, /* String */
    var isHearted : Boolean,
    var heartCount : Int
)