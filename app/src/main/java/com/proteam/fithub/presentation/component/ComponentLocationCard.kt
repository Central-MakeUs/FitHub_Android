package com.proteam.fithub.presentation.component

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseLocationData
import com.proteam.fithub.databinding.ComponentLocationCardBinding

class ComponentLocationCard(context : Context, attrs : AttributeSet) : ConstraintLayout(context, attrs) {
    private lateinit var binding : ComponentLocationCardBinding

    init {
        initBinding()
    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.component_location_card, this, false)
        addView(binding.root)
    }

    fun getData(data : ResponseLocationData.LocationItems?) {
        binding.data = data
        binding.componentLocationCardLayoutLevel.getExercise(data?.category)
    }

}