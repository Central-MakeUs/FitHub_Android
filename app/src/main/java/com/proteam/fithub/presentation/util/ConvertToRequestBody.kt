package com.proteam.fithub.presentation.util

import android.app.DownloadManager.Request
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object ConvertToRequestBody {

    fun String.textConverter() : RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }


    fun List<String>?.listConverter() : List<RequestBody>? {
        return this?.let { map { it.toRequestBody("text/plain".toMediaTypeOrNull()) } }
    }
}