package com.proteam.fithub.data.remote.service

import com.proteam.fithub.data.remote.request.RequestChangePassword
import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestPhoneNumberAvailable
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.data.remote.request.RequestSignUpWithPhone
import com.proteam.fithub.data.remote.request.RequestSignUpWithSocial
import com.proteam.fithub.data.remote.response.ResponseChangePassword
import com.proteam.fithub.data.remote.response.ResponseSignUpWithPhone
import com.proteam.fithub.data.remote.response.ResponseSignUpWithSocial
import com.proteam.fithub.presentation.util.BaseResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
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
        @Part("phoneNumber") phoneNumber : String,
        @Part("name") name : String,
        @Part("nickname") nickname : String,
        @Part("password") password : String,
        @Part("birth") birth : String,
        @Part("gender") gender : String,
        @Part("preferExercises") preferExercises : List<Int>,
        @Part profileImage : MultipartBody.Part
    ) : Response<ResponseSignUpWithPhone>

    @PATCH("users/sign-up/oauth")
    suspend fun requestSignUpWithSocial(
        @Body body : RequestSignUpWithSocial
    ) : Response<ResponseSignUpWithSocial>

    @POST("users/password")
    suspend fun requestPhoneNumberAvailable(
        @Body body : RequestPhoneNumberAvailable
    ) : Response<BaseResponse>

    @PATCH("users/password")
    suspend fun requestChangePassword(
        @Body body : RequestChangePassword
    ) : Response<ResponseChangePassword>
}