package com.proteam.fithub.presentation.component

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.proteam.fithub.R
import com.proteam.fithub.data.data.ComponentUserData
import com.proteam.fithub.databinding.ComponentUserInfoSmallBinding

class ComponentSmallUser(context : Context, attrs : AttributeSet) : ConstraintLayout(context, attrs) {
    private lateinit var binding : ComponentUserInfoSmallBinding
    private lateinit var userData : ComponentUserData
    private lateinit var time : String

    private val _clickedUserIndex = MutableLiveData<Int>()
    val clickedUserIndex : LiveData<Int> = _clickedUserIndex

    init {
        initBinding()
    }

    fun getUserData(userData : ComponentUserData, time : String) {
        this.userData = userData
        this.time = time
        initUi()
    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.component_user_info_small, this, false)
        addView(binding.root)
    }

    private fun initUi() {
        binding.data = userData
        binding.time = time
        binding.componentUserSmallLayoutExercise.getExercise(userData.mainExerciseInfo?.category)
        binding.componentUserSmallLayoutLevel.getLevel(userData.mainExerciseInfo?.level, userData.mainExerciseInfo?.gradeName)

        binding.componentUserSmallIvProfile.setOnClickListener { _clickedUserIndex.value = userData.ownerId }
    }

    fun userProfileImage() = binding.componentUserSmallIvProfile
    fun userPK() = userData.ownerId
}