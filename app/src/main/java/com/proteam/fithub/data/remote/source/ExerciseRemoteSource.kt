package com.proteam.fithub.data.remote.source

import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.data.remote.response.ResponseMainExerciseData
import com.proteam.fithub.data.remote.service.ExerciseService
import com.proteam.fithub.domain.source.ExerciseSource
import com.proteam.fithub.presentation.util.BaseResponse
import com.proteam.fithub.presentation.util.ErrorConverter.convertAndGetCode
import retrofit2.Response
import javax.inject.Inject

class ExerciseRemoteSource @Inject constructor(private val service : ExerciseService): ExerciseSource {
    override suspend fun requestExercises(): Result<List<ResponseExercises.ExercisesList>> {
        val res = service.requestExerciseList()
        if(res.isSuccessful) {
            if(checkResultCode(res.body()!!.code)) {
                return Result.success(res.body()!!.result)
            } else {
                //코드 판별
            }
        }
        return Result.failure(IllegalArgumentException(res.message()))
    }

    override suspend fun requestMainExercises(): Result<ResponseMainExerciseData.ResultMainExerciseData> {
        val res = service.requestMainExercise()
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!.result)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    override suspend fun requestModifyMainExercise(categoryId: Int): Result<BaseResponse> {
        val res = service.requestModifyMainExercise(categoryId)
        return when(res.code()) {
            in 200..399 -> Result.success(res.body()!!)
            else -> Result.failure(IllegalArgumentException(res.errorBody()?.convertAndGetCode().toString()))
        }
    }

    private fun checkResultCode(code : Int) : Boolean {
        return code == 2000
    }
}