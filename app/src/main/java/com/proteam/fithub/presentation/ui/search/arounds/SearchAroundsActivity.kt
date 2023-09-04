package com.proteam.fithub.presentation.ui.search.arounds

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
import com.proteam.fithub.databinding.ActivitySearchAroundsBinding
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.search.arounds.none.SearchAroundNoneFragment
import com.proteam.fithub.presentation.ui.search.arounds.normal.SearchAroundDefaultFragment
import com.proteam.fithub.presentation.ui.search.arounds.viewmodel.SearchAroundsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable

@AndroidEntryPoint
class SearchAroundsActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchAroundsBinding
    private val viewModel : SearchAroundsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_arounds)

        receiveIntent()
        initBinding()
        initUi()
    }

    private fun receiveIntent() {
        viewModel.setCurrentLocation(Pair(intent.getDoubleExtra("Latitude", 123.0), intent.getDoubleExtra("Longitude", 37.0)))
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

    private fun observeUserKeywords() {
        binding.searchAroundsToolbarEdtKeyword.setOnEditorActionListener { text, i, keyEvent ->
            if(text.text.isNotEmpty() && i == EditorInfo.IME_NULL && keyEvent.action == MotionEvent.ACTION_DOWN) {
                requestSearch()
            }
            return@setOnEditorActionListener true
        }

        binding.searchAroundsToolbarEdtKeyword.addTextChangedListener {
            setDefaultFragment()
        }
    }

    private fun setDefaultFragment() {
        SearchAroundDefaultFragment().changeFragment()
    }

    private fun setNoneFragment() {
        SearchAroundNoneFragment().changeFragment()
    }

    private fun Fragment.changeFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.search_arounds_layout_container, this).commit()
    }

    fun initInputKeywords() {
        binding.searchAroundsToolbarEdtKeyword.setText("")
        viewModel.initUserInputKeyword()
    }

    fun clickRecommends(tag : String) {
        binding.searchAroundsToolbarEdtKeyword.setText(tag)
        requestSearch()
    }

    private fun requestSearch() {
        viewModel.requestSearchData()
        observeSearchCode()
    }

    private fun observeSearchCode() {
        viewModel.resultCount.observe(this) {
            setResult(RESULT_OK, Intent(this, MainActivity::class.java)
                .putExtra("keyword", viewModel.userInputKeyword.value!!)
                .putExtra("list", viewModel.currentMarkerItems.value as Serializable)).also { finish() }
            //else setNoneFragment()
        }
    }

    fun onBackPress() {
        finish()
    }
}