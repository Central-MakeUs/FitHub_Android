package com.proteam.fithub.data.remote.response

data class ExamCertificateData (
    val recordId : Int,
    val path : Int, /* String */
    var isHearted : Boolean,
    var heartCount : Int
)