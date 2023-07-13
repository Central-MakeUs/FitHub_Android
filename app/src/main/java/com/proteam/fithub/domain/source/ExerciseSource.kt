package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.response.ResponseExercises
import retrofit2.Response

interface ExerciseSource {

    suspend fun requestExercises() : Result<ResponseExercises.ResultExercises>
}