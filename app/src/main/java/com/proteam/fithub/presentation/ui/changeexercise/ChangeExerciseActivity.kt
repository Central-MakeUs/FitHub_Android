package com.proteam.fithub.presentation.ui.changeexercise

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivityChangeExerciseBinding
import com.proteam.fithub.presentation.ui.sign.up.common.exercise.InterestExerciseFragment
import com.proteam.fithub.presentation.util.AnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeExerciseActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChangeExerciseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_exercise)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        initBinding()
        setFragment()
    }

    private fun initBinding() {
        binding.activity = this
    }

    private fun setFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.change_exercise_layout_container, InterestExerciseFragment(), "CHANGE").commit()
    }

    fun onBackPress() {
        finish()
    }

}