package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.data.remote.response.ResponseMainExerciseData
import com.proteam.fithub.presentation.util.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ExerciseService {

    @GET("users/exercise-category")
    suspend fun requestExerciseList() : Response<ResponseExercises>

    @GET("users/main-exercise")
    suspend fun requestMainExercise() : Response<ResponseMainExerciseData>

    @PATCH("users/my-page/main-exercise/{categoryId}")
    suspend fun requestModifyMainExercise(
        @Path("categoryId") categoryId : Int
    ) : Response<BaseResponse>
}