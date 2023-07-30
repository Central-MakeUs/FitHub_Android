package com.proteam.fithub.presentation.ui.detail.certificate

import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivityExerciseCertificateDetailBinding
import com.proteam.fithub.presentation.component.ComponentAlertToast
import com.proteam.fithub.presentation.component.ComponentBottomDialogSelectReportDelete
import com.proteam.fithub.presentation.component.ComponentDialogYesNo
import com.proteam.fithub.presentation.component.ComponentDialogYesNoWithParam
import com.proteam.fithub.presentation.ui.FitHub.Companion.mSharedPreferences
import com.proteam.fithub.presentation.ui.detail.adapter.CommunityDetailCommentAdapter
import com.proteam.fithub.presentation.ui.detail.certificate.viewmodel.ExerciseCertificateDetailViewModel
import com.proteam.fithub.presentation.ui.detail.common.CommentViewModel
import com.proteam.fithub.presentation.ui.write.certificate.WriteOrModifyCertificateActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExerciseCertificateDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityExerciseCertificateDetailBinding
    private val viewModel : ExerciseCertificateDetailViewModel by viewModels()
    private val commentViewModel : CommentViewModel by viewModels()
    private val commentAdapter by lazy {
        CommunityDetailCommentAdapter(::requestCommentHeartClicked, ::onCommentOptionClicked)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exercise_certificate_detail)

        requestData()
        initBinding()
        initUi()
        observeHeartClicked()

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
        binding.exerciseCertificateDetailRvComment.itemAnimator = null
    }

    private fun initCommentInput() {
        binding.exerciseCertificateDetailTvComment.setText("")
    }

    private fun requestData() {
        intent.type?.let{ viewModel.requestData(it.toInt()) }
        observeDetailStatus()
    }

    private fun observeDetailStatus() {
        viewModel.certificateStatus.observe(this) {
            if(it == 2000) observeDetailData()
            else it.toString().showAlert()
        }
    }

    private fun observeDetailData() {
        viewModel.certificateData.observe(this) { it ->
            binding.exerciseCertificateDetailLayoutUser.getUserData(it.userInfo, it.createdAt)
            binding.detailData = it
            binding.tags = it.hashtags.hashtags?.joinToString(" ") { "#${it.name}" }

            requestComment()
        }
    }

    private fun requestComment() {
        lifecycleScope.launch {
            commentViewModel.requestCommentList("records", viewModel.certificateData.value!!.recordId).collect {
                commentAdapter.submitData(it)
            }
        }
    }

    /** Request **/

    private fun requestDeleteCertificate() {
        viewModel.requestDeleteCertificate()
        observeDeleteStatus()
    }

    private fun observeDeleteStatus() {
        viewModel.deleteCertificateStatus.observe(this) {
            if(it == 2000) finish()
            else it.toString().showAlert()
        }
    }

    fun requestPostComment() {
        commentViewModel.requestPostComment("records", viewModel.certificateData.value!!.recordId, viewModel.userInputComment.value!!)
        observeCommentStatus()
        initCommentInput()
    }

    private fun requestCommentHeartClicked(position : Int, index : Int) {
        commentViewModel.requestCommentHeartClicked("records", viewModel.certificateData.value!!.recordId, index)
        observeHeartStatus(position)
    }

    private fun observeHeartStatus(position : Int) {
        commentViewModel.heartStatus.observe(this) {
            if(it == 0) return@observe
            if(it == 2000) observeCommentHeartClicked()
            else {
                it.toString().showAlert()
                return@observe
            }
        }
    }

    /** Observe **/

    private fun observeCommentHeartClicked() {
        //:TODO 수정!
        commentViewModel.commentHeartResult.observe(this) {
            commentAdapter.setHeartAction(commentAdapter.getItemIndex(it.result.commentId), it.result.newLikes)
        }
    }

    private fun observeHeartClicked() {
        viewModel.heartResult.observe(this) {
            viewModel.setEffectHeart()
        }
    }

    private fun observeCommentStatus() {
        commentViewModel.postCommentState.observe(this) {
            if(it == 2000) {
                requestComment()
                requestData()
            }
        }
    }

    /** Etc Detail **/

    fun onOptionClicked() {
        if(viewModel.certificateData.value!!.userInfo.ownerId == mSharedPreferences.getString("userId", "0")?.toInt()) {
            ComponentBottomDialogSelectReportDelete(::modifyCertificate, ::checkReallyDelete).show(supportFragmentManager, "MINE")
        } else {
            ComponentBottomDialogSelectReportDelete(::reportPost, ::reportUser).show(supportFragmentManager, "NOT_MINE")
        }
    }

    private fun modifyCertificate(index : Int?) {
        startActivity(Intent(this, WriteOrModifyCertificateActivity::class.java).setType("${viewModel.certificateData.value?.recordId}"))
        finish()
    }

    private fun checkReallyDelete(index : Int?) {
        ComponentDialogYesNo(::requestDeleteCertificate).show(supportFragmentManager, "MY_CERTIFICATE_ARTICLE")
    }

    private fun reportPost(index : Int?) {
        //:TODO 게시글 신고로직
        //viewModel.certificateData.value.recordId
    }

    private fun reportUser(index : Int?) {
        //:TODO 유저 신고로직
        //viewModel.certificateData.value.userInfo.ownerId
    }

    /** Etc Comment **/

    private fun onCommentOptionClicked(userIndex : Int, commentIndex : Int) {
        if(userIndex == mSharedPreferences.getString("userId", "0")?.toInt()) {
            ComponentBottomDialogSelectReportDelete(::modifyComment, ::checkCommentReallyDelete).also { it.getIndexData(userIndex, commentIndex) }.show(supportFragmentManager, "MINE_COMMENT")
        } else {
            ComponentBottomDialogSelectReportDelete(::reportComment, ::reportCommentUser).also { it.getIndexData(userIndex, commentIndex) }.show(supportFragmentManager, "NOT_MINE_COMMENT")
        }
    }

    private fun checkCommentReallyDelete(index : Int?) {
        ComponentDialogYesNoWithParam(::deleteComment).also { it.setIndex(index!!) }.show(supportFragmentManager, "MY_COMMENT")
    }


    private fun deleteComment(index : Int?) {
        commentViewModel.requestDeleteComment("records", viewModel.certificateData.value!!.recordId, index!!)
        requestComment()
    }

    private fun modifyComment(index : Int?) {
        Log.e("----", "modifyComment: $index", )
    }

    private fun reportComment(user : Int?) {
        //:TODO 댓글 신고로직
        Log.e("----", "reportComment: $user", )
    }

    private fun reportCommentUser(index : Int?) {
        //:TODO 댓글 유저 신고로직
        Log.e("----", "reportCommentUser: $index", )
    }

    private fun String.showAlert() {
        ComponentAlertToast().show(supportFragmentManager, this)
    }
}