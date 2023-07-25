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
import com.proteam.fithub.presentation.component.ComponentBottomDialogSelectReportDelete
import com.proteam.fithub.presentation.component.ComponentDialogYesNo
import com.proteam.fithub.presentation.ui.detail.adapter.CommunityDetailCommentAdapter
import com.proteam.fithub.presentation.ui.detail.board.adapter.BoardImageAdapter
import com.proteam.fithub.presentation.ui.detail.board.viewmodel.BoardDetailViewModel
import com.proteam.fithub.presentation.ui.write.board.WriteOrModifyBoardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BoardDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardDetailBinding
    private val viewModel : BoardDetailViewModel by viewModels()

    private val boardImageAdapter by lazy {
        BoardImageAdapter()
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
            binding.tag = it.hashtags.hashtags.map { "#${it.name}" }.joinToString(" ")
            requestComment()
            if(it.articlePictureList.pictureList.isNotEmpty()) setImages(it.articlePictureList.pictureList)
        }
    }

    private fun requestComment() {
        lifecycleScope.launch {
            viewModel.requestComment().collect {
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
    }

    private fun initImageRV() {
        binding.boardDetailRvImages.adapter = boardImageAdapter
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
        binding.boardDetailTvComment.setText("")
    }

    private fun onCommentHeartClicked(position : Int, index : Int) {
        //하트추가 로직
    }

    private fun onCommentOptionClicked(position : Int, index : Int) {

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
        if(viewModel.articleData.value!!.userInfo.ownerId == 1) {
            ComponentBottomDialogSelectReportDelete(::modifyCertificate, ::checkReallyDelete).show(supportFragmentManager, "MINE")
        } else {
            ComponentBottomDialogSelectReportDelete(::reportPost, ::reportUser).show(supportFragmentManager, "NOT_MINE")
        }
    }

    private fun checkReallyDelete() {
        ComponentDialogYesNo(::deleteCertificate).show(supportFragmentManager, "MY_CERTIFICATE_ARTICLE")
    }
    private fun deleteCertificate() {
        viewModel.requestDeleteArticle()
        finish()
    }

    private fun modifyCertificate() {
        startActivity(Intent(this, WriteOrModifyBoardActivity::class.java).setType("${viewModel.articleData.value?.articleId}"))
        finish()
    }

    private fun reportPost() {
        //:TODO 게시글 신고로직
    }

    private fun reportUser() {
        //:TODO 유저 신고로직
    }
}