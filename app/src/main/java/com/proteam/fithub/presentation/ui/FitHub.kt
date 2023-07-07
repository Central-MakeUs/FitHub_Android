package com.proteam.fithub.presentation.ui

import android.app.Application
import android.content.SharedPreferences
import com.kakao.sdk.common.KakaoSdk
import com.proteam.fithub.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FitHub : Application() {

    companion object {
        lateinit var mSharedPreferences: SharedPreferences
    }
    override fun onCreate() {
        super.onCreate()

        mSharedPreferences = applicationContext.getSharedPreferences("FitHub", MODE_PRIVATE)
        initKakaoSDK()
    }

    private fun initKakaoSDK() {
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
    }
}