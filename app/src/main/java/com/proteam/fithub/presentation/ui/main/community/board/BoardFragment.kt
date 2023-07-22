package com.proteam.fithub.presentation.ui.main.community.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.proteam.fithub.R
import com.proteam.fithub.data.data.ComponentUserData
import com.proteam.fithub.data.data.dummy.DummyCommunityData
import com.proteam.fithub.databinding.FragmentCommunityBoardBinding
import com.proteam.fithub.databinding.FragmentCommunityCertifiateBinding
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.main.community.board.adapter.BoardAdapter
import com.proteam.fithub.presentation.ui.main.community.viewmodel.CommunityViewModel
import kotlinx.coroutines.launch

class BoardFragment : Fragment() {
    private lateinit var binding : FragmentCommunityBoardBinding
    private val viewModel : CommunityViewModel by activityViewModels()

    private val boardAdapter by lazy {
        BoardAdapter(::onHeartClicked ,::onBoardClicked)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_board, container, false)

        initBinding()
        initUi()
        requestData()

        return binding.root
    }

    private fun initBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initRV()

    }

    override fun onResume() {
        super.onResume()
        observeData(if(viewModel.isBoardRecentSort.value == true) "date" else "like", viewModel.selectedFilter.value ?: 0)
    }

    private fun requestData() {
        viewModel.selectedFilter.observe(viewLifecycleOwner) {
            observeData(if(viewModel.isBoardRecentSort.value == true)"date" else "like", it)
        }
        viewModel.isBoardRecentSort.observe(viewLifecycleOwner) {
            viewModel.selectedFilter.value?.let { it1 -> observeData(if(it == true)"date" else "like", it1) }
        }

        boardAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    private fun observeData(type : String, category : Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.requestArticleItems(type, category).apply {
                collect {
                    boardAdapter.submitData(it)
                }
            }
        }
    }
    private fun initRV() {
        binding.fgCommunityBoardRvBoard.adapter = boardAdapter
        binding.fgCommunityBoardRvBoard.itemAnimator = null
    }

    private fun onBoardClicked(position : Int) {
        (requireActivity() as MainActivity).openBoardDetailActivity(position)
    }

    private fun onHeartClicked(position : Int) {

    }

}