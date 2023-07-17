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

    lateinit var userProfile : Drawable /** String **/
    lateinit var userNickName : String
    lateinit var userExercise : String
    lateinit var userLevel : String
    lateinit var date : String

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ComponentBigUser).getAttributeData()
        initBinding()
        initUi()
    }

    private fun getUserData() : ComponentUserData {
        return ComponentUserData(
            userProfile, userNickName, date, userExercise, userLevel
        )

    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.component_user_info_big, this, false)
        addView(binding.root)
    }

    private fun initUi() {
        binding.data = getUserData()
    }

    private fun TypedArray.getAttributeData() {
        userProfile = this.getDrawable(R.styleable.ComponentBigUser_big_userProfile)!!
        userNickName = this.getString(R.styleable.ComponentBigUser_big_userNickName)!!
        date = this.getString(R.styleable.ComponentBigUser_big_time)!!
        userExercise = this.getString(R.styleable.ComponentBigUser_big_exercise)!!
        userLevel = this.getString(R.styleable.ComponentBigUser_big_level)!!
    }
}