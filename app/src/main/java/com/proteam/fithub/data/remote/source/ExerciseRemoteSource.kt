package com.proteam.fithub.data.remote.source

import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.data.remote.service.ExerciseService
import com.proteam.fithub.domain.source.ExerciseSource
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

    private fun checkResultCode(code : Int) : Boolean {
        return code == 2000
    }
}