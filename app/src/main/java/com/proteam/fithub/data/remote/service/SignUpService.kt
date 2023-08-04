package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.request.RequestChangePassword
import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestPhoneNumberAvailable
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.data.remote.response.ResponseChangePassword
import com.proteam.fithub.data.remote.response.ResponseSignUpWithPhone
import com.proteam.fithub.data.remote.response.ResponseSignUp
import com.proteam.fithub.presentation.util.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface SignUpService {

    @POST("/users/sms")
    suspend fun requestPhoneAuthCode(
        @Body body : RequestSMSAuth
    ) : Response<BaseResponse>

    @POST("/users/sms/auth")
    suspend fun requestCheckPhoneAuthCode(
        @Body body : RequestCheckSMSAuth
    ) : Response<BaseResponse>

    @GET("/users/exist-nickname")
    suspend fun requestCheckSameNickname(
        @Query("nickname") nickname : String
    ) : Response<BaseResponse>

    @Multipart
    @POST("/users/sign-up")
    suspend fun requestSignUpWithPhone(
        @Part("marketingAgree") marketingAgree : Boolean,
        @Part("phoneNumber") phoneNumber : RequestBody,
        @Part("name") name : RequestBody,
        @Part("nickname") nickname : RequestBody,
        @Part("password") password : RequestBody,
        @Part("birth") birth : RequestBody,
        @Part("gender") gender : RequestBody,
        @Part("preferExercises") preferExercises : Int,
        @Part profileImage : MultipartBody.Part?
    ) : Response<ResponseSignUpWithPhone>

    @Multipart
    @PATCH("users/sign-up/oauth")
    suspend fun requestSignUpWithSocial(
        @Part("marketingAgree") marketingAgree : Boolean,
        @Part("name") name : RequestBody,
        @Part("nickname") nickname : RequestBody,
        @Part("birth") birth : RequestBody,
        @Part("gender") gender : RequestBody,
        @Part("preferExercises") preferExercises : Int,
        @Part profileImage : MultipartBody.Part?
    ) : Response<ResponseSignUp>

    @PATCH("users/password")
    suspend fun requestChangePassword(
        @Body body : RequestChangePassword
    ) : Response<ResponseChangePassword>

    @POST("users/exist-phone/{type}")
    suspend fun requestExistPhone(
        @Path("type") type : Int,
        @Body body : RequestPhoneNumberAvailable
    ) : Response<BaseResponse>
}