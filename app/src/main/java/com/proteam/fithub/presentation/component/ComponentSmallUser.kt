package com.proteam.fithub.presentation.component

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.data.data.ComponentUserData
import com.proteam.fithub.databinding.ComponentUserInfoSmallBinding

class ComponentSmallUser(context : Context, attrs : AttributeSet) : ConstraintLayout(context, attrs) {
    private lateinit var binding : ComponentUserInfoSmallBinding
    private lateinit var userData : ComponentUserData

    init {
        initBinding()
    }

    fun getUserData(userData : ComponentUserData) {
        this.userData = userData
        initUi()
    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.component_user_info_small, this, false)
        addView(binding.root)
    }

    private fun initUi() {
        binding.data = userData
        binding.componentUserSmallLayoutExercise.getExercise(userData.exercise)
        binding.componentUserSmallLayoutLevel.getLevel(userData.level)
    }
}