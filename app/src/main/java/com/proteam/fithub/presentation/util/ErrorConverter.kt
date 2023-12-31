package com.proteam.fithub.presentation.util

import android.util.Log
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode

import okhttp3.ResponseBody
import retrofit2.Response

object ErrorConverter {
    fun ResponseBody.convertAndGetCode() : Int {
        //Log.e("----", "convertAndGetCode: ${this.string()}", )
        return this.string().split(",")[1].split(":")[1].trim().toInt()
    }

    fun setValidate(res : Response<*>) : Result<Any> {
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            in 400..499 -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
            else -> Result.failure(IllegalArgumentException())
        }
    }
}