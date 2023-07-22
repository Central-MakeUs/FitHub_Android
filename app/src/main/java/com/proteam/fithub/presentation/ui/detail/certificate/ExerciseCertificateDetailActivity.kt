package com.proteam.fithub.presentation.ui.detail.certificate

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.proteam.fithub.R
import com.proteam.fithub.data.data.ComponentUserData
import com.proteam.fithub.databinding.ActivityExerciseCertificateDetailBinding
import com.proteam.fithub.presentation.ui.detail.adapter.CommunityDetailCommentAdapter
import com.proteam.fithub.presentation.ui.detail.certificate.viewmodel.ExerciseCertificateDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExerciseCertificateDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityExerciseCertificateDetailBinding
    private val viewModel : ExerciseCertificateDetailViewModel by viewModels()
    private val commentAdapter by lazy {
        CommunityDetailCommentAdapter(::onCommentHeartClicked)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exercise_certificate_detail)

        requestData()
        initBinding()
        initUi()
        observeHeartClicked()

    }

    private fun requestData() {
        intent.type?.let{ viewModel.requestData(it.toInt()) }
        observeDetailData()
    }

    private fun observeDetailData() {
        viewModel.certificateData.observe(this) { it ->
            binding.exerciseCertificateDetailLayoutUser.getUserData(it.userInfo, it.createdAt)
            binding.detailData = it
            binding.tags = "#" + it.recordCategory.name + it.hashtags.hashtags.map { it.name }.joinToString { " #" }

            requestComment()
        }
    }

    private fun requestComment() {
        lifecycleScope.launch {
            viewModel.requestComment().collect {
                commentAdapter.submitData(it)
            }
        }
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.activity = this
        binding.viewModel = viewModel
    }

    private fun initUi() {
        initCommentRV()
    }

    private fun initCommentRV() {
        binding.exerciseCertificateDetailRvComment.adapter = commentAdapter
    }

    private fun onCommentHeartClicked(index : Int) {

    }

    private fun observeHeartClicked() {
        viewModel.heartResult.observe(this) {
            viewModel.setEffectHeart()
        }
    }

    fun onPostComment() {
        viewModel.requestPostComment()
        observeCommentStatus()
    }

    private fun observeCommentStatus() {
        viewModel.postCommentState.observe(this) {
            if(it == 2000) {
                requestComment()
                initCommentInput()
            }
        }
    }

    private fun initCommentInput() {
        binding.exerciseCertificateDetailTvComment.setText("")
    }
}