package com.proteam.fithub.domain.source

import com.proteam.fithub.data.remote.request.RequestChangePassword
import com.proteam.fithub.data.remote.request.RequestChangePasswordOnMyPage
import com.proteam.fithub.data.remote.request.RequestCheckSMSAuth
import com.proteam.fithub.data.remote.request.RequestPasswordExist
import com.proteam.fithub.data.remote.request.RequestPhoneNumberAvailable
import com.proteam.fithub.data.remote.request.RequestSMSAuth
import com.proteam.fithub.data.remote.response.ResponseChangePassword
import com.proteam.fithub.data.remote.response.ResponseSignUpWithPhone
import com.proteam.fithub.data.remote.response.ResponseSignUp
import com.proteam.fithub.presentation.util.BaseResponse
import okhttp3.MultipartBody

interface SignUpSource {

    suspend fun requestSMSAuth(body : RequestSMSAuth) : Result<BaseResponse>

    suspend fun requestCheckSMSAuth(body : RequestCheckSMSAuth) : Result<BaseResponse>

    suspend fun requestCheckSameNickName(nickname : String) : Result<BaseResponse>

    suspend fun requestSignUpWithPhone(marketingAgree : Boolean, phoneNumber : String, name : String, nickname : String, password : String, birth : String, gender : String, preferExercises : Int, profileImage : MultipartBody.Part?, fcmToken : String) : Result<ResponseSignUpWithPhone>

    suspend fun requestSignUpWithSocial(marketingAgree : Boolean, name : String, nickname : String, birth : String, gender : String, preferExercises : Int, profileImage : MultipartBody.Part?, fcmToken : String) : Result<ResponseSignUp>

    suspend fun requestChangePassword(body : RequestChangePassword) : Result<ResponseChangePassword>

    suspend fun requestExistPhone(type : Int, body : RequestPhoneNumberAvailable) : Result<BaseResponse>

    suspend fun requestExistPassword(body : RequestPasswordExist) : Result<BaseResponse>

    suspend fun requestChangePasswordOnMyPage(body : RequestChangePasswordOnMyPage) : Result<ResponseChangePassword>
}