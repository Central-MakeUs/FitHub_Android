package com.proteam.fithub.presentation.ui.detail.board

import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
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
import com.proteam.fithub.presentation.ui.write.board.WriteOrModifyBoardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.Serializable

@AndroidEntryPoint
class BoardDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardDetailBinding
    private val viewModel : BoardDetailViewModel by viewModels()
    private val commentViewModel : CommentViewModel by viewModels()

    private val boardImageAdapter by lazy {
        BoardImageAdapter(::onImageClicked)
    }

    private val commentAdapter by lazy {
        CommunityDetailCommentAdapter(::onCommentHeartClicked, ::onCommentOptionClicked)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_detail)

        requestData()
        initBinding()
        initUi()

    }

    private fun requestData() {
        intent.type?.let{ viewModel.requestData(it.toInt()) }
        observeDetailData()
    }

    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun observeDetailData() {
        viewModel.articleData.observe(this) { it ->
            binding.boardDetailLayoutUser.getUserData(it.userInfo, it.createdAt)
            binding.data = it
            binding.tag = it.hashtags.hashtags?.map { "#${it.name}" }?.joinToString(" ")
            requestComment()
            setImages(it.articlePictureList.pictureList.filterNotNull())
        }
    }

    private fun requestComment() {
        lifecycleScope.launch {
            commentViewModel.requestCommentList("articles", viewModel.articleData.value!!.articleId).collect {
                commentAdapter.submitData(it)
            }
        }
    }

    private fun setImages(items : List<ResponseArticleDetailData.ArticlePictureResult>) {
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

    private fun onImageClicked(position : Int) {
        supportFragmentManager.beginTransaction().add(R.id.board_detail_layout_container, FullSizeImageFragment(), position.toString()).commit()
    }

    fun onPostComment() {
        commentViewModel.requestPostComment("articles", viewModel.articleData.value!!.articleId, viewModel.userInputComment.value!!)
        observeCommentStatus()
    }

    private fun observeCommentStatus() {
        commentViewModel.postCommentState.observe(this) {
            if(it == 2000) {
                requestComment()
                initCommentInput()
                requestData()
            }
        }
    }

    private fun initCommentInput() {
        binding.boardDetailTvComment.setText("")
    }

    private fun onCommentHeartClicked(position : Int, index : Int) {
        commentViewModel.requestCommentHeartClicked("articles", viewModel.articleData.value!!.articleId, index)
        observeHeartStatus(position)
    }

    private fun observeHeartStatus(position : Int) {
        commentViewModel.heartStatus.observe(this) {
            if (it == 0) return@observe
            if(it == 2000) observeCommentHeartClicked()
            else { it.toString().showAlert() }
        }
    }

    private fun observeCommentHeartClicked() {
        //:TODO 수정!
        commentViewModel.commentHeartResult.observe(this) {
            commentAdapter.setHeartAction(commentAdapter.getItemIndex(it.result.commentId), it.result.newLikes)
        }
    }

    private fun onCommentOptionClicked(userIndex : Int, commentIdx : Int) {
        if(userIndex == FitHub.mSharedPreferences.getString("userId", "0")?.toInt()) {
            ComponentBottomDialogSelectReportDelete(::modifyComment, ::checkCommentReallyDelete).also { it.getIndexData(userIndex, commentIdx) }.show(supportFragmentManager, "MINE_COMMENT")
        } else {
            ComponentBottomDialogSelectReportDelete(::reportComment, ::reportCommentUser).also { it.getIndexData(userIndex, commentIdx) }.show(supportFragmentManager, "NOT_MINE_COMMENT")
        }
    }



    private fun observeHeartClicked() {
        viewModel.heartResult.observe(this) {
            viewModel.setEffectHeart()
        }
    }

    private fun observeScrapClicked() {
        viewModel.scrapResult.observe(this) {
            viewModel.setEffectScrap()
        }
    }

    fun onOptionClicked() {
        if(viewModel.articleData.value!!.userInfo.ownerId == FitHub.mSharedPreferences.getString("userId", "0")?.toInt()) {
            ComponentBottomDialogSelectReportDelete(::modifyCertificate, ::checkReallyDelete).show(supportFragmentManager, "MINE")
        } else {
            ComponentBottomDialogSelectReportDelete(::reportPost, ::reportUser).show(supportFragmentManager, "NOT_MINE")
        }
    }

    private fun checkReallyDelete(noinline : Int?) {
        ComponentDialogYesNo(::deleteCertificate).show(supportFragmentManager, "MY_CERTIFICATE_ARTICLE")
    }
    private fun deleteCertificate() {
        viewModel.requestDeleteArticle()
        finish()
    }

    private fun modifyCertificate(noinline : Int?) {
        startActivity(Intent(this, WriteOrModifyBoardActivity::class.java).setType("${viewModel.articleData.value?.articleId}"))
        finish()
    }

    private fun reportPost(noinline : Int?) {
        //:TODO 게시글 신고로직
    }

    private fun reportUser(noinline : Int?) {
        //:TODO 유저 신고로직
    }

    private fun modifyComment(index : Int?) {
        //:TODO 댓글 수정로직
    }

    private fun checkCommentReallyDelete(index : Int?) {
        ComponentDialogYesNoWithParam(::deleteComment).also { it.setIndex(index!!) }.show(supportFragmentManager, "MY_COMMENT")
    }

    private fun deleteComment(index : Int?) {
        commentViewModel.requestDeleteComment("articles", viewModel.articleData.value!!.articleId, index!!)
        requestComment()
    }

    private fun reportComment(index : Int?) {
        //:TODO 댓글 신고로직
    }

    private fun reportCommentUser(user : Int?) {
        //:TODO 댓글 유저 신고로직
    }

    private fun String.showAlert() {
        ComponentAlertToast().show(supportFragmentManager, this)
    }
}