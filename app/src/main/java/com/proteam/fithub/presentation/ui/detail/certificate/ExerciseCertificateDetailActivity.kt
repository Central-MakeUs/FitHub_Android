package com.proteam.fithub.presentation.ui.detail.certificate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.data.data.ComponentUserData
import com.proteam.fithub.databinding.ActivityExerciseCertificateDetailBinding
import com.proteam.fithub.presentation.ui.detail.adapter.CommunityDetailCommentAdapter

class ExerciseCertificateDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityExerciseCertificateDetailBinding

    private val commentAdapter by lazy {
        CommunityDetailCommentAdapter(::onCommentHeartClicked)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exercise_certificate_detail)

        initUi()

    }

    private fun initUi() {
        initCommentRV()

        binding.exerciseCertificateDetailLayoutUser.getUserData(
            ComponentUserData(
                R.drawable.ic_launcher_background,
                "춘배",
                "2023.07.18",
                "클라이밍",
                "1"
            )
        )
    }

    private fun initCommentRV() {
        binding.exerciseCertificateDetailRvComment.adapter = commentAdapter
    }

    private fun onCommentHeartClicked(index : Int) {
        //하트추가 로직
    }
}