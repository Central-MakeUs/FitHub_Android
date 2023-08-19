package com.proteam.fithub.presentation.ui.detail.certificate

import android.content.Intent
import android.os.Bundle
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
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.otheruser.OtherUserProfileActivity
import com.proteam.fithub.presentation.ui.write.certificate.WriteOrModifyCertificateActivity
import com.proteam.fithub.presentation.util.CustomSnackBar
import com.proteam.fithub.presentation.util.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExerciseCertificateDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExerciseCertificateDetailBinding
    private val viewModel: ExerciseCertificateDetailViewModel by viewModels()
    private val commentViewModel: CommentViewModel by viewModels()
    private val commentAdapter by lazy {
        CommunityDetailCommentAdapter(
            ::requestCommentHeartClicked,
            ::onCommentOptionClicked,
            ::commentUserProfileClicked
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_exercise_certificate_detail)

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
        intent.type?.let { viewModel.requestData(it.toInt()).also { showLoadingDialog() } }
        observeDetailStatus()
    }

    private fun observeDetailStatus() {
        viewModel.certificateStatus.observe(this) {
            if (it == 2000) observeDetailData()
            else it.toString().showAlert()
        }
    }

    private fun observeDetailData() {
        viewModel.certificateData.observe(this) { it ->
            dismissLoadingDialog()

            binding.exerciseCertificateDetailLayoutUser.apply {
                getUserData(it.userInfo, it.createdAt)
                userProfileImage().setOnClickListener { it2 ->
                    if (it.userInfo.ownerId != mSharedPreferences.getString("userId", "0")
                            ?.toInt()
                    ) {
                        startActivity(
                            Intent(
                                this@ExerciseCertificateDetailActivity,
                                OtherUserProfileActivity::class.java
                            ).setType(it.userInfo.ownerId.toString())
                        )
                    } else {
                        startActivity(
                            Intent(
                                this@ExerciseCertificateDetailActivity,
                                MainActivity::class.java
                            ).setType("MY_PAGE")
                        )
                    }
                }
            }
            binding.detailData = it
            binding.tags = it.hashtags.hashtags?.joinToString(" ") { "#${it.name}" }

            requestComment()
        }
    }

    private fun requestComment() {
        lifecycleScope.launch {
            commentViewModel.requestCommentList(
                "records",
                viewModel.certificateData.value!!.recordId
            ).collect {
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
            if (it == 2000) finish()
            else it.toString().showAlert()
        }
    }

    fun requestPostComment() {
        commentViewModel.requestPostComment(
            "records",
            viewModel.certificateData.value!!.recordId,
            viewModel.userInputComment.value!!
        )
        observeCommentStatus()
        initCommentInput()
    }

    private fun requestCommentHeartClicked(position: Int, index: Int) {
        commentViewModel.requestCommentHeartClicked(
            "records",
            viewModel.certificateData.value!!.recordId,
            index
        )
        observeHeartStatus(position)
    }

    private fun observeHeartStatus(position: Int) {
        commentViewModel.heartStatus.observe(this) {
            if (it == 0) return@observe
            if (it == 2000) observeCommentHeartClicked()
            else {
                it.toString().showAlert()
                return@observe
            }
        }
    }

    /** Observe **/

    private fun observeCommentHeartClicked() {
        commentViewModel.commentHeartResult.observe(this) {
            commentAdapter.setHeartAction(
                commentAdapter.getItemIndex(it.result.commentId),
                it.result.newLikes,
                it.result.isLiked
            )
        }
    }

    private fun observeHeartClicked() {
        viewModel.heartResult.observe(this) {
            viewModel.setEffectHeart()
        }
    }

    private fun observeCommentStatus() {
        commentViewModel.postCommentState.observe(this) {
            if (it == 2000) {
                requestComment()
                requestData()
            }
        }
    }

    /** Etc Detail **/

    fun onOptionClicked() {
        if (viewModel.certificateData.value!!.userInfo.ownerId == mSharedPreferences.getString(
                "userId",
                "0"
            )?.toInt()
        ) {
            ComponentBottomDialogSelectReportDelete(::modifyCertificate, ::checkReallyDelete).show(
                supportFragmentManager,
                "MINE"
            )
        } else {
            ComponentBottomDialogSelectReportDelete(::reportPost, ::reportRecordUser).show(
                supportFragmentManager,
                "NOT_MINE"
            )
        }
    }

    private fun modifyCertificate(index: Int?) {
        startActivity(
            Intent(
                this,
                WriteOrModifyCertificateActivity::class.java
            ).setType("${viewModel.certificateData.value?.recordId}")
        )
        finish()
    }

    private fun checkReallyDelete(index: Int?) {
        ComponentDialogYesNo(::requestDeleteCertificate).show(
            supportFragmentManager,
            "MY_CERTIFICATE_ARTICLE"
        )
    }

    private fun reportPost(noinline: Int?) {
        viewModel.requestReportRecord(viewModel.certificateData.value!!.recordId)
        observeReportRecordStatus()
    }

    private fun observeReportRecordStatus() {
        viewModel.reportRecordStatus.observe(this) {
            if (it == 2000) {
                CustomSnackBar.makeSnackBar(binding.root, "운동인증 신고가 완료되었습니다.")
                finish()
            } else {
                CustomSnackBar.makeSnackBar(binding.root, it.toString())
            }
        }
    }

    private fun reportRecordUser(index: Int?) {
        viewModel.requestReportUser(viewModel.certificateData.value!!.userInfo.ownerId)
        observeReportUserStatus("Record")
    }

    private fun observeReportUserStatus(type: String) {
        viewModel.reportUserStatus.observe(this) {
            if (it == 2000) {
                CustomSnackBar.makeSnackBar(binding.root, "유저 신고가 완료되었습니다.")
                if (type == "Comment") commentAdapter.refresh() else finish()
            } else {
                CustomSnackBar.makeSnackBar(binding.root, it.toString())
            }
        }
    }

    /** Etc Comment **/

    private fun onCommentOptionClicked(userIndex: Int, commentIndex: Int) {
        if (userIndex == mSharedPreferences.getString("userId", "0")?.toInt()) {
            ComponentBottomDialogSelectReportDelete(
                ::modifyComment,
                ::checkCommentReallyDelete
            ).also { it.getIndexData(userIndex, commentIndex) }
                .show(supportFragmentManager, "MINE_COMMENT")
        } else {
            ComponentBottomDialogSelectReportDelete(
                ::reportComment,
                ::reportCommentUser
            ).also { it.getIndexData(userIndex, commentIndex) }
                .show(supportFragmentManager, "NOT_MINE_COMMENT")
        }
    }

    private fun checkCommentReallyDelete(index: Int?) {
        ComponentDialogYesNoWithParam(::deleteComment).also { it.setIndex(index!!) }
            .show(supportFragmentManager, "MY_COMMENT")
    }


    private fun deleteComment(index: Int?) {
        commentViewModel.requestDeleteComment(
            "records",
            viewModel.certificateData.value!!.recordId,
            index!!
        )
        requestComment()
        requestData()
    }

    private fun modifyComment(index: Int?) {
        //: TODO 기획 축소로 삭제
    }

    private fun reportComment(index: Int?) {
        viewModel.requestReportComment(index!!)
        observeReportCommentStatus()
    }

    private fun observeReportCommentStatus() {
        viewModel.reportCommentStatus.observe(this) {
            if (it == 2000) {
                CustomSnackBar.makeSnackBar(binding.root, "유저 신고가 완료되었습니다.")
                commentAdapter.refresh()
            } else {
                CustomSnackBar.makeSnackBar(binding.root, it.toString())
            }
        }
    }

    private fun reportCommentUser(user: Int?) {
        viewModel.requestReportUser(user!!)
        observeReportUserStatus("Comment")
    }

    private fun commentUserProfileClicked(index: Int) {
        if (index != mSharedPreferences.getString("userId", "0")?.toInt()) {
            startActivity(
                Intent(
                    this,
                    OtherUserProfileActivity::class.java
                ).setType(index.toString())
            )
        } else {
            startActivity(Intent(this, MainActivity::class.java).setType("MY_PAGE"))
        }
    }

    private fun String.showAlert() {
        ComponentAlertToast().show(supportFragmentManager, this)
    }

    private var loadingDialog = LoadingDialog()
    private fun showLoadingDialog() = loadingDialog.show(supportFragmentManager, null)
    private fun dismissLoadingDialog() = loadingDialog.dismiss()
}