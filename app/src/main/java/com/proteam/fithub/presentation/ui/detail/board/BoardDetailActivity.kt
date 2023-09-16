package com.proteam.fithub.presentation.ui.detail.board

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseArticleDetailData
import com.proteam.fithub.databinding.ActivityBoardDetailBinding
import com.proteam.fithub.presentation.component.ComponentAlertToast
import com.proteam.fithub.presentation.component.ComponentBottomDialogSelectReportDelete
import com.proteam.fithub.presentation.component.ComponentDialogYesNo
import com.proteam.fithub.presentation.component.ComponentDialogYesNoWithParam
import com.proteam.fithub.presentation.ui.FitHub
import com.proteam.fithub.presentation.ui.detail.adapter.CommunityDetailCommentAdapter
import com.proteam.fithub.presentation.ui.detail.board.adapter.BoardImageAdapter
import com.proteam.fithub.presentation.ui.detail.board.image.FullSizeImageFragment
import com.proteam.fithub.presentation.ui.detail.board.viewmodel.BoardDetailViewModel
import com.proteam.fithub.presentation.ui.detail.common.CommentViewModel
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.otheruser.OtherUserProfileActivity
import com.proteam.fithub.presentation.ui.write.board.WriteOrModifyBoardActivity
import com.proteam.fithub.presentation.util.AnalyticsHelper
import com.proteam.fithub.presentation.util.CustomSnackBar
import com.proteam.fithub.presentation.util.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BoardDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBoardDetailBinding
    private val viewModel: BoardDetailViewModel by viewModels()
    private val commentViewModel: CommentViewModel by viewModels()

    private val boardImageAdapter by lazy {
        BoardImageAdapter(::onImageClicked)
    }

    private val commentAdapter by lazy {
        CommunityDetailCommentAdapter(
            ::onCommentHeartClicked,
            ::onCommentOptionClicked,
            ::commentUserProfileClicked
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_detail)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        requestData()
        initBinding()
        initUi()

    }

    private fun requestData() {
        intent.type?.let { viewModel.requestData(it.toInt()).also { showLoadingDialog() } }
        observeDetailData()
    }

    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun observeDetailData() {
        viewModel.articleData.observe(this) { it ->

            dismissLoadingDialog()

            binding.boardDetailLayoutUser.apply {
                getUserData(it.userInfo, it.createdAt)
                userProfileImage().setOnClickListener { it1 ->
                    if (it.userInfo.ownerId != FitHub.mSharedPreferences.getString("userId", "0")
                            ?.toInt()
                    ) {
                        startActivity(
                            Intent(
                                this@BoardDetailActivity,
                                OtherUserProfileActivity::class.java
                            ).setType(it.userInfo.ownerId.toString())
                        )
                    } else {
                        setResult(RESULT_OK, Intent(this@BoardDetailActivity, MainActivity::class.java).putExtra("state", true))
                        finish()
                    }
                }
            }
            binding.data = it
            binding.tag = it.hashtags.hashtags?.map { "#${it.name}" }?.joinToString(" ")
            requestComment()
            setImages(it.articlePictureList.pictureList.filterNotNull())
        }
    }

    private fun requestComment() {
        lifecycleScope.launch {
            commentViewModel.requestCommentList("articles", viewModel.articleData.value!!.articleId)
                .collect {
                    commentAdapter.submitData(it)
                }
        }
    }

    private fun setImages(items: List<ResponseArticleDetailData.ArticlePictureResult>) {
        boardImageAdapter.images = items as MutableList
        boardImageAdapter.notifyItemRangeChanged(0, items.size)
    }

    private fun initUi() {
        initCommentRV()
        initImageRV()
        observeHeartClicked()
        observeScrapClicked()
    }

    private fun initCommentRV() {
        binding.boardDetailRvComment.adapter = commentAdapter
        binding.boardDetailRvComment.itemAnimator = null
    }

    private fun initImageRV() {
        binding.boardDetailRvImages.adapter = boardImageAdapter
    }

    private fun onImageClicked(path: String) {
        supportFragmentManager.beginTransaction()
            .add(R.id.board_detail_layout_container, FullSizeImageFragment(), path)
            .commit()
    }

    fun onPostComment() {
        commentViewModel.requestPostComment(
            "articles",
            viewModel.articleData.value!!.articleId,
            viewModel.userInputComment.value!!
        )
        observeCommentStatus()
    }

    private fun observeCommentStatus() {
        commentViewModel.postCommentState.observe(this) {
            if (it == 2000) {
                requestComment()
                initCommentInput()
                requestData()
            }
        }
    }

    private fun initCommentInput() {
        binding.boardDetailTvComment.setText("")
    }

    fun onArticleHeartClicked() {
        viewModel.requestHeartClicked(viewModel.articleData.value!!.articleId).also { showLoadingDialog() }
    }

    private fun onCommentHeartClicked(position: Int, index: Int) {
        commentViewModel.requestCommentHeartClicked(
            "articles",
            viewModel.articleData.value!!.articleId,
            index
        ).also { showLoadingDialog() }
        observeCommentHeartStatus(position)
    }

    private fun observeCommentHeartStatus(position: Int) {
        commentViewModel.heartStatus.observe(this) {
            dismissLoadingDialog()

            if (it == 0) return@observe
            if (it == 2000) observeCommentHeartClicked()
            else {
                it.toString().showAlert()
            }
        }
    }

    private fun observeCommentHeartClicked() {
        commentViewModel.commentHeartResult.observe(this) {
            commentAdapter.setHeartAction(
                commentAdapter.getItemIndex(it.result.commentId),
                it.result.newLikes,
                it.result.isLiked
            )
        }
    }

    private fun onCommentOptionClicked(userIndex: Int, commentIdx: Int) {
        if (userIndex == FitHub.mSharedPreferences.getString("userId", "0")?.toInt()) {
            ComponentBottomDialogSelectReportDelete(
                ::modifyComment,
                ::checkCommentReallyDelete
            ).also { it.getIndexData(userIndex, commentIdx) }
                .show(supportFragmentManager, "MINE_COMMENT")
        } else {
            ComponentBottomDialogSelectReportDelete(
                ::reportComment,
                ::reportCommentUser
            ).also { it.getIndexData(userIndex, commentIdx) }
                .show(supportFragmentManager, "NOT_MINE_COMMENT")
        }
    }


    private fun observeHeartClicked() {
        viewModel.heartResult.observe(this) {
            viewModel.setEffectHeart(it).also { dismissLoadingDialog() }
        }
    }

    private fun observeScrapClicked() {
        viewModel.scrapResult.observe(this) {
            viewModel.setEffectScrap().also { dismissLoadingDialog() }
        }
    }

    fun onOptionClicked() {
        if (viewModel.articleData.value!!.userInfo.ownerId == FitHub.mSharedPreferences.getString(
                "userId",
                "0"
            )?.toInt()
        ) {
            ComponentBottomDialogSelectReportDelete(::modifyCertificate, ::checkReallyDelete).show(
                supportFragmentManager,
                "MINE"
            )
        } else {
            ComponentBottomDialogSelectReportDelete(::reportPost, ::reportUser).show(
                supportFragmentManager,
                "NOT_MINE"
            )
        }
    }

    private fun checkReallyDelete(noinline: Int?) {
        ComponentDialogYesNo(::deleteCertificate).show(
            supportFragmentManager,
            "MY_CERTIFICATE_ARTICLE"
        )
    }

    private fun deleteCertificate() {
        viewModel.requestDeleteArticle()
        finish()
    }

    private fun modifyCertificate(noinline: Int?) {
        startActivity(
            Intent(
                this,
                WriteOrModifyBoardActivity::class.java
            ).setType("${viewModel.articleData.value?.articleId}")
        )
        finish()
    }

    private fun reportPost(noinline: Int?) {
        viewModel.requestReportArticle(viewModel.articleData.value!!.articleId)
        observeReportArticleStatus()
    }

    private fun observeReportArticleStatus() {
        viewModel.reportArticleStatus.observe(this) {
            if (it == 2000) {
                CustomSnackBar.makeSnackBar(binding.root, "게시글 신고가 완료되었습니다.")
                finish()
            } else {
                CustomSnackBar.makeSnackBar(binding.root, it.toString())
            }
        }
    }

    private fun reportUser(noinline: Int?) {
        viewModel.requestReportUser(viewModel.articleData.value!!.userInfo.ownerId)
        observeReportUserStatus("Article")
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

    private fun modifyComment(index: Int?) {
        //:TODO 기획 축소로 인해 삭제
    }

    private fun checkCommentReallyDelete(index: Int?) {
        ComponentDialogYesNoWithParam(::deleteComment).also { it.setIndex(index!!) }
            .show(supportFragmentManager, "MY_COMMENT")
    }

    private fun deleteComment(index: Int?) {
        commentViewModel.requestDeleteComment(
            "articles",
            viewModel.articleData.value!!.articleId,
            index!!
        )
        requestComment()
    }

    private fun reportComment(index: Int?) {
        viewModel.requestReportComment(index!!)
        observeReportCommentStatus()
    }

    private fun reportCommentUser(user: Int?) {
        viewModel.requestReportUser(user!!)
        observeReportUserStatus("Comment")
    }

    private fun commentUserProfileClicked(index: Int) {
        if (index != FitHub.mSharedPreferences.getString("userId", "0")?.toInt()) {
            startActivity(
                Intent(
                    this,
                    OtherUserProfileActivity::class.java
                ).setType(index.toString())
            )
        } else {
            setResult(RESULT_OK, Intent(this, MainActivity::class.java).putExtra("state", true))
            finish()
        }
    }

    private fun observeReportCommentStatus() {
        viewModel.reportCommentStatus.observe(this) {
            if (it == 2000) {
                CustomSnackBar.makeSnackBar(binding.root, "댓글 신고가 완료되었습니다.")
                commentAdapter.refresh()
            } else {
                CustomSnackBar.makeSnackBar(binding.root, it.toString())
            }
        }
    }

    private fun String.showAlert() {
        ComponentAlertToast().show(supportFragmentManager, this)
    }

    fun onBackPress() {
        finish()
    }


    private var loadingDialog = LoadingDialog()
    private fun showLoadingDialog() = loadingDialog.show(supportFragmentManager, null)
    private fun dismissLoadingDialog() = loadingDialog.dismiss()
}