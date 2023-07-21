package com.proteam.fithub.presentation.component

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ComponentExerciseLevelBinding

class ComponentExerciseLevel(context : Context, attrs : AttributeSet) : ConstraintLayout(context, attrs) {
    private lateinit var binding : ComponentExerciseLevelBinding

    lateinit var type : String
    lateinit var exercise : String
    lateinit var level : String

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ComponentExerciseLevel).getAttributeData()
        initBinding()
    }

    fun getExercise(exercise : String) {
        this.exercise = exercise
        initUi()
    }

    fun getLevel(level : String) {
        this.level = level
        initUi()
    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.component_exercise_level, this, false)
        addView(binding.root)
    }

    private fun initUi() {
        binding.content = if(type == "Exercise") exercise else level.mappingLevel()

        binding.componentExerciseLevelContent.setTextColor(setTextColor(type))
    }

    private fun String.mappingLevel() : String {
        return when(this) {
            "1" -> "Lv1. 우주먼지"
            "2" -> "Lv2. 성운"
            "3" -> "Lv3. 태양"
            "4" -> "Lv4. 블랙홀"
            else -> "Lv5. 은하"
        }
    }

    private fun setTextColor(type : String) : Int {
        return if(type == "Exercise") resources.getColor(R.color.text_sub02, null) else level.validateColor()
    }

    private fun String.validateColor() : Int {
        return when(this) {
            "1" -> resources.getColor(R.color.color_level_1, null)
            "2" -> resources.getColor(R.color.color_level_2, null)
            "3" -> resources.getColor(R.color.color_level_3, null)
            "4" -> resources.getColor(R.color.color_level_4, null)
            else -> resources.getColor(R.color.color_level_5, null)
        }
    }

    private fun TypedArray.getAttributeData() {
        type = this.getString(R.styleable.ComponentExerciseLevel_exercise_level_type)!!
    }
}