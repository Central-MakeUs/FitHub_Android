package com.proteam.fithub.presentation.ui.search.community

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivitySearchBinding
import com.proteam.fithub.presentation.ui.detail.board.BoardDetailActivity
import com.proteam.fithub.presentation.ui.detail.certificate.ExerciseCertificateDetailActivity
import com.proteam.fithub.presentation.ui.search.community.normal.SearchDefaultFragment
import com.proteam.fithub.presentation.ui.search.community.none.SearchNoneFragment
import com.proteam.fithub.presentation.ui.search.community.result.SearchResultFragment
import com.proteam.fithub.presentation.ui.search.community.viewmodel.SearchViewModel
import com.proteam.fithub.presentation.util.AnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchBinding
    private val viewModel : SearchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        initBinding()
        initUi()
    }

    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initUi() {
        setDefaultFragment()
        observeUserKeywords()
    }

    private fun setDefaultFragment() {
        SearchDefaultFragment().changeFragments(null)
        clearBackStack()
    }

    private fun setResultFragment() {
        SearchResultFragment().changeFragments(null)
        clearBackStack()
    }

    private fun setNoneFragment() {
        SearchNoneFragment().changeFragments(viewModel.userInputKeyword.value)
        clearBackStack()
    }

    private fun clearBackStack() {
        supportFragmentManager
    }

    private fun Fragment.changeFragments(tag : String?) {
        supportFragmentManager.beginTransaction().replace(R.id.search_layout_container, this, tag).commit()
    }

    private fun observeUserKeywords() {
        binding.searchEdtKeyword.setOnEditorActionListener { text, i, keyEvent ->
            if(text.text.isNotEmpty() && i == EditorInfo.IME_NULL && keyEvent.action == MotionEvent.ACTION_DOWN) {
                requestSearch()
            }
            return@setOnEditorActionListener true
        }

        binding.searchEdtKeyword.addTextChangedListener {
            setDefaultFragment()
        }
    }

    fun clickRecommends(tag : String) {
        binding.searchEdtKeyword.setText(tag)
        requestSearch()
    }

    private fun requestSearch() {
        viewModel.requestSearchTotalData()
        observeSearchCode()
    }

    private fun observeSearchCode() {
        viewModel.searchCode.observe(this) {
            if(it == 2000 || it == 2021) setResultFragment()
        }
    }

    fun onBackPress() {
        finish()
    }

    fun initUserKeyword() {
        binding.searchEdtKeyword.setText("")
        viewModel.initUserInputKeyword()
    }

    fun openCertificateDetailActivity(index : Int) {
        startActivity(Intent(this, ExerciseCertificateDetailActivity::class.java).setType(index.toString()))
    }

    fun openArticleDetailActivity(index : Int) {
        startActivity(Intent(this, BoardDetailActivity::class.java).setType(index.toString()))
    }

    fun changeTab(index : Int) {
        (supportFragmentManager.findFragmentById(R.id.search_layout_container) as SearchResultFragment).changeCurrentTab(index)
    }
}