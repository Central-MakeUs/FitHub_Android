package com.proteam.fithub.presentation.ui.main.community.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.proteam.fithub.R
import com.proteam.fithub.data.data.ComponentUserData
import com.proteam.fithub.data.data.dummy.DummyCommunityData
import com.proteam.fithub.databinding.FragmentCommunityBoardBinding
import com.proteam.fithub.databinding.FragmentCommunityCertifiateBinding
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.main.community.board.adapter.BoardAdapter

class BoardFragment : Fragment() {
    private lateinit var binding : FragmentCommunityBoardBinding

    private val boardAdapter by lazy {
        BoardAdapter(returnBoardData(), ::onBoardClicked)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_board, container, false)

        initUi()

        return binding.root
    }

    private fun initUi() {
        binding.fgCommunityBoardRvBoard.adapter = boardAdapter
    }

    /** Dummy **/
    private fun returnBoardData() : List<DummyCommunityData> =
        mutableListOf<DummyCommunityData>().apply {
            for(i in 1 until 6) {
                add(
                    DummyCommunityData(
                    ComponentUserData(R.drawable.ic_launcher_background,
                    "춘배",
                    "10분 전",
                    "클라이밍",
                        "$i"
                    ),
                        "글제목입니다!롷먕랴ㅐㅁㅇ너럄ㄴ어랴ㅐㅇ너랴ㅐㅓㄴ랴ㅐㅓㅁ애ㅑ럼야ㅐ러ㅑㅐㅁㄴ어랴ㅐㅇ머랴ㅐㅓㅁ야래ㅓㅁㄴㅇ렁ㄴ먀ㅓ랴ㅐㅁ너랴ㅐㅁㅇ러ㅑㅐㅇ러내ㅠㄴ",
                        "글내용인데요 이게 길어지면요 큰일납니다요",
                        "#클라이밍",
                        "123",
                        "12",
                        R.drawable.ic_launcher_background
                )
                )
            }
        }

    private fun onBoardClicked(position : Int) {
        (requireActivity() as MainActivity).openBoardDetailActivity(position)
    }

}