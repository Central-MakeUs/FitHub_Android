package com.proteam.fithub.domain.repository

import com.proteam.fithub.data.remote.response.ResponseExercises
import retrofit2.Response

interface ExerciseRepository {

    suspend fun requestExercises() : Result<List<ResponseExercises.ExercisesList>>
}