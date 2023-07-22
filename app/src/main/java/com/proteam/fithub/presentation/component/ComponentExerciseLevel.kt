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
    var exercise : String? = null
    var level : Int? = 0
    var levelTitle : String? = null

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ComponentExerciseLevel).getAttributeData()
        initBinding()
    }

    fun getExercise(exercise : String?) {
        this.exercise = exercise
        initUi()
    }

    fun getLevel(level : Int?, title : String?) {
        this.level = level
        this.levelTitle = title
        initUi()
    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.component_exercise_level, this, false)
        addView(binding.root)
    }

    private fun initUi() {
        binding.content = if(type == "Exercise") exercise else level?.mappingLevel(levelTitle)

        binding.componentExerciseLevelContent.setTextColor(setTextColor(type))
    }

    private fun Int.mappingLevel(title : String?) : String {
        return "Lv${this}. $title"
    }

    private fun setTextColor(type : String) : Int {
        return if(type == "Exercise") resources.getColor(R.color.text_sub02, null) else level?.validateColor() ?: resources.getColor(R.color.color_level_5, null)
    }

    private fun Int?.validateColor() : Int {
        return when(this) {
            1 -> resources.getColor(R.color.color_level_1, null)
            2 -> resources.getColor(R.color.color_level_2, null)
            3 -> resources.getColor(R.color.color_level_3, null)
            4 -> resources.getColor(R.color.color_level_4, null)
            else -> resources.getColor(R.color.color_level_5, null)
        }
    }

    private fun TypedArray.getAttributeData() {
        type = this.getString(R.styleable.ComponentExerciseLevel_exercise_level_type)!!
    }
}