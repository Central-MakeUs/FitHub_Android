package com.proteam.fithub.presentation.ui

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.kakao.sdk.common.KakaoSdk
import com.proteam.fithub.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FitHub : Application(), LifecycleEventObserver {
    var isForeground = false

    companion object {
        lateinit var mSharedPreferences: SharedPreferences
    }
    override fun onCreate() {
        super.onCreate()
        mSharedPreferences = applicationContext.getSharedPreferences("FitHub", MODE_PRIVATE)
        initKakaoSDK()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun initKakaoSDK() {
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        isForeground = when(event) {
            Lifecycle.Event.ON_START -> {
                true
            }
            Lifecycle.Event.ON_DESTROY -> {
                false
            }
            else -> {
                return
            }
        }
    }


}