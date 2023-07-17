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

    lateinit var userProfile : Drawable /** String **/
    lateinit var userNickName : String
    lateinit var userExercise : String
    lateinit var userLevel : String
    lateinit var date : String

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ComponentSmallUser).getAttributeData()
        initBinding()
        initUi()
    }

    private fun getUserData() : ComponentUserData {
        return ComponentUserData(
            userProfile, userNickName, date, userExercise, userLevel
        )

    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.component_user_info_small, this, false)
        addView(binding.root)
    }

    private fun initUi() {
        binding.data = getUserData()
    }

    private fun TypedArray.getAttributeData() {
        userProfile = this.getDrawable(R.styleable.ComponentSmallUser_small_userProfile)!!
        userNickName = this.getString(R.styleable.ComponentSmallUser_small_userNickName)!!
        date = this.getString(R.styleable.ComponentSmallUser_small_time)!!
        userExercise = this.getString(R.styleable.ComponentSmallUser_small_exercise)!!
        userLevel = this.getString(R.styleable.ComponentSmallUser_small_level)!!
    }
}