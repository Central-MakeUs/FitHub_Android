package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.response.ResponseExercises
import retrofit2.Response
import retrofit2.http.GET

interface ExerciseService {

    @GET("users/exercise-category")
    suspend fun requestExerciseList() : Response<ResponseExercises>
}