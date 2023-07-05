package com.proteam.fithub.presentation.ui

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.proteam.fithub.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FitHub : Application() {

    override fun onCreate() {
        super.onCreate()

        initKakaoSDK()
    }

    private fun initKakaoSDK() {
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
    }
}