package com.proteam.fithub.domain.repository

import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.data.remote.response.ResponseMainExerciseData
import com.proteam.fithub.presentation.util.BaseResponse
import retrofit2.Response

interface ExerciseRepository {

    suspend fun requestExercises() : Result<List<ResponseExercises.ExercisesList>>

    suspend fun requestMainExercises() : Result<ResponseMainExerciseData.ResultMainExerciseData>

    suspend fun requestModifyMainExercise(categoryId : Int) : Result<BaseResponse>
}