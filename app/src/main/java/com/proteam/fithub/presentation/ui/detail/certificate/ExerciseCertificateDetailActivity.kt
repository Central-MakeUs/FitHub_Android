package com.proteam.fithub.presentation.ui.detail.certificate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivityExerciseCertificateDetailBinding
import com.proteam.fithub.presentation.ui.detail.certificate.adapter.ExerciseCertificateDetailCommentAdapter

class ExerciseCertificateDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityExerciseCertificateDetailBinding

    private val commentAdapter by lazy {
        ExerciseCertificateDetailCommentAdapter(::onCommentHeartClicked)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exercise_certificate_detail)

        initUi()

    }

    private fun initUi() {
        initCommentRV()
    }

    private fun initCommentRV() {
        binding.exerciseCertificateDetailRvComment.adapter = commentAdapter
    }

    private fun onCommentHeartClicked(index : Int) {
        //하트추가 로직
    }
}