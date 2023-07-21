package com.proteam.fithub.presentation.component

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.common.KakaoSdk.type
import com.proteam.fithub.R
import com.proteam.fithub.data.data.ComponentUserData
import com.proteam.fithub.databinding.ComponentUserInfoBigBinding

class ComponentBigUser(context : Context, attrs : AttributeSet) : ConstraintLayout(context, attrs) {
    private lateinit var binding : ComponentUserInfoBigBinding
    private lateinit var userData : ComponentUserData

    init {
        initBinding()
    }

    fun getUserData(userData : ComponentUserData) {
        this.userData = userData
        initUi()
    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.component_user_info_big, this, false)
        addView(binding.root)
    }

    private fun initUi() {
        binding.data = userData
        binding.componentUserBigLayoutExercise.getExercise(userData.exercise)
        binding.componentUserBigLayoutLevel.getLevel(userData.level)
    }
}