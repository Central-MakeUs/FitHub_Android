package com.proteam.fithub.presentation.ui.search

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivitySearchBinding
import com.proteam.fithub.presentation.ui.search.default.SearchDefaultFragment
import com.proteam.fithub.presentation.ui.search.result.SearchResultFragment
import com.proteam.fithub.presentation.ui.search.viewmodel.SearchViewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchBinding
    private val viewModel : SearchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

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
        SearchDefaultFragment().changeFragments()
    }

    private fun setResultFragment() {
        SearchResultFragment().changeFragments()
    }

    private fun Fragment.changeFragments() {
        supportFragmentManager.beginTransaction().replace(R.id.search_layout_container, this).commit()
    }

    private fun observeUserKeywords() {
        binding.searchEdtKeyword.setOnEditorActionListener { text, i, keyEvent ->
            if(text.text.isNotEmpty() && i == EditorInfo.IME_NULL && keyEvent.action == MotionEvent.ACTION_DOWN) {

                setResultFragment()
            }
            return@setOnEditorActionListener true
        }

        binding.searchEdtKeyword.addTextChangedListener {
            setDefaultFragment()
        }
    }

    fun clickRecommends(tag : String) {
        binding.searchEdtKeyword.setText(tag)

        setResultFragment()
        //:TODO 검색로직 추가하기
    }

    fun onBackPress() {
        finish()
    }

    fun initUserKeyword() {
        binding.searchEdtKeyword.setText("")
        viewModel.initUserInputKeyword()
    }
}