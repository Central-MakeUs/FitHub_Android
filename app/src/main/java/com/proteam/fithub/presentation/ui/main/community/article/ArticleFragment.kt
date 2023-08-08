package com.proteam.fithub.presentation.ui.main.community.article

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentCommunityArticleBinding
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.main.community.article.adapter.BoardAdapter
import com.proteam.fithub.presentation.ui.main.community.article.viewmodel.ArticleViewModel
import com.proteam.fithub.presentation.ui.main.community.viewmodel.CommunityViewModel
import com.proteam.fithub.presentation.ui.otheruser.OtherUserProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleFragment : Fragment() {
    private lateinit var binding : FragmentCommunityArticleBinding
    private val viewModel : ArticleViewModel by viewModels()
    private val communityViewModel : CommunityViewModel by activityViewModels()

    private val boardAdapter by lazy {
        BoardAdapter(::onBoardClicked, ::onProfileClicked)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_article, container, false)

        initBinding()
        initUi()
        requestData()

        return binding.root
    }

    /** LifeCycle **/

    override fun onResume() {
        super.onResume()
        observeData(if(viewModel.isArticleRecentSort.value == true) "date" else "like", communityViewModel.selectedFilter.value ?: 0)
    }

    /** Init **/

    private fun initBinding() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initRV()

    }

    private fun initRV() {
        binding.fgCommunityBoardRvBoard.adapter = boardAdapter
        binding.fgCommunityBoardRvBoard.itemAnimator = null
    }

    /** Request **/

    private fun requestData() {
        communityViewModel.selectedFilter.observe(viewLifecycleOwner) {
            observeData(if(viewModel.isArticleRecentSort.value == true)"date" else "like", it)
        }
        viewModel.isArticleRecentSort.observe(viewLifecycleOwner) {
            observeData(if(it) "date" else "like", communityViewModel.selectedFilter.value ?: 0)
        }
    }

    /** Observe **/

    private fun observeData(type : String, category : Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.requestArticleItems(type, category).apply {
                collect {
                    boardAdapter.submitData(it)
                }
            }
        }
    }

    /** Click **/

    private fun onBoardClicked(position : Int) {
        (requireActivity() as MainActivity).openBoardDetailActivity(position)
    }

    private fun onProfileClicked(index : Int) {
        startActivity(Intent(requireActivity(), OtherUserProfileActivity::class.java).setType(index.toString()))
    }
}