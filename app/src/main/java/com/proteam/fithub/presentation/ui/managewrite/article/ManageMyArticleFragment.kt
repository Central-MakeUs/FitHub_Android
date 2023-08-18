package com.proteam.fithub.presentation.ui.managewrite.article

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
import com.proteam.fithub.databinding.FragmentMyArticleBinding
import com.proteam.fithub.presentation.ui.detail.board.BoardDetailActivity
import com.proteam.fithub.presentation.ui.detail.certificate.ExerciseCertificateDetailActivity
import com.proteam.fithub.presentation.ui.managewrite.article.adapter.ManageMyArticleAdapter
import com.proteam.fithub.presentation.ui.managewrite.article.viewmodel.ManageMyArticleViewModel
import com.proteam.fithub.presentation.ui.managewrite.viewmodel.ManageMyWriteViewModel
import com.proteam.fithub.presentation.ui.write.board.WriteOrModifyBoardActivity
import com.proteam.fithub.presentation.ui.write.certificate.WriteOrModifyCertificateActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class ManageMyArticleFragment : Fragment() {

    private lateinit var binding : FragmentMyArticleBinding
    private val viewModel : ManageMyArticleViewModel by viewModels()
    private val manageViewModel : ManageMyWriteViewModel by activityViewModels()

    private val articleAdapter by lazy {
        ManageMyArticleAdapter(::onItemClicked, ::onCheckClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_article, container, false)

        requestMyArticle()
        initBinding()
        initUi()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        articleAdapter.refresh()
    }

    private fun requestMyArticle() {
        manageViewModel.selectedFilter.observe(viewLifecycleOwner) {
            requestData(it)
        }
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initRv()
    }

    private fun initRv() {
        binding.fgMyArticleRvContents.adapter = articleAdapter
        binding.fgMyArticleRvContents.itemAnimator = null
    }

    private fun requestData(filter : Int) {
        lifecycleScope.launch {
            viewModel.requestMyArticleData(filter).collect {
                articleAdapter.submitData(it)
            }
        }

        articleAdapter.addLoadStateListener {
            binding.fgMyArticleLayoutNone.visibility = if(it.append.endOfPaginationReached && articleAdapter.itemCount == 0) View.VISIBLE else View.GONE
        }
    }


    private fun onItemClicked(index : Int) {
        startActivity(Intent(requireActivity(), BoardDetailActivity::class.java).setType(index.toString()))
    }

    private fun onCheckClicked(index : Int, selected : Boolean) {
        viewModel.manageSelectItems(index, selected)
    }

    fun onAllSelectClicked() {
        viewModel.manageAllSelectedItems(articleAdapter.checkAll(viewModel.isAllClicked.value!!))
    }

    fun onSelectDeleteClicked() {
        viewModel.requestDeleteMyArticles()
        observeDeleteStatus()
    }

    fun onWriteArticle() {
        startActivity(Intent(requireActivity(), WriteOrModifyBoardActivity::class.java).setType("Write"))
    }

    private fun observeDeleteStatus() {
        viewModel.deleteStatus.observe(viewLifecycleOwner) {
            if(it == 2000) articleAdapter.refresh()
        }
    }

}