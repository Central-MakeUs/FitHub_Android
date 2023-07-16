package com.proteam.fithub.data.remote.repository

import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.domain.repository.ExerciseRepository
import com.proteam.fithub.domain.source.ExerciseSource
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(private val source : ExerciseSource): ExerciseRepository {
    override suspend fun requestExercises(): Result<List<ResponseExercises.ExercisesList>> {
        return source.requestExercises()
    }
}