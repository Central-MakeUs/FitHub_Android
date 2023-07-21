package com.proteam.fithub.presentation.ui.detail.board

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.data.data.ComponentUserData
import com.proteam.fithub.databinding.ActivityBoardDetailBinding
import com.proteam.fithub.presentation.ui.detail.adapter.CommunityDetailCommentAdapter
import com.proteam.fithub.presentation.ui.detail.board.adapter.BoardImageAdapter

class BoardDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityBoardDetailBinding

    private val boardImageAdapter by lazy {
        BoardImageAdapter()
    }

    private val commentAdapter by lazy {
        CommunityDetailCommentAdapter(::onCommentHeartClicked)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_detail)

        initUi()

    }

    private fun initUi() {
        initCommentRV()
        initImageRV()

        binding.boardDetailLayoutUser.getUserData(
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
        binding.boardDetailRvComment.adapter = commentAdapter
    }

    private fun initImageRV() {
        binding.boardDetailRvImages.adapter = boardImageAdapter
    }

    private fun onCommentHeartClicked(index : Int) {
        //하트추가 로직
    }
}