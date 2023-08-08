package com.proteam.fithub.data.remote.repository

import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.data.remote.response.ResponseMainExerciseData
import com.proteam.fithub.domain.repository.ExerciseRepository
import com.proteam.fithub.domain.source.ExerciseSource
import com.proteam.fithub.presentation.util.BaseResponse
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(private val source : ExerciseSource): ExerciseRepository {
    override suspend fun requestExercises(): Result<List<ResponseExercises.ExercisesList>> {
        return source.requestExercises()
    }

    override suspend fun requestMainExercises(): Result<ResponseMainExerciseData.ResultMainExerciseData> {
        return source.requestMainExercises()
    }

    override suspend fun requestModifyMainExercise(categoryId: Int): Result<BaseResponse> {
        return source.requestModifyMainExercise(categoryId)
    }
}