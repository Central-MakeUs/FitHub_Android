package com.proteam.fithub.presentation.ui.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.databinding.ActivityBookmarkBinding
import com.proteam.fithub.presentation.ui.bookmark.adapter.BookmarkAdapter
import com.proteam.fithub.presentation.ui.bookmark.viewmodel.BookMarkViewModel
import com.proteam.fithub.presentation.ui.detail.board.BoardDetailActivity
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.sign.`in`.social.SocialSignInActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookMarkActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBookmarkBinding
    private val viewModel : BookMarkViewModel by viewModels()
    private val bookmarkAdapter by lazy {
        BookmarkAdapter(::onBookmarkClicked)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bookmark)

        observeFilterExercises()
        observeSelectedFilters()

        initBinding()
        initUi()
    }

    override fun onResume() {
        super.onResume()
        requestBookmark(viewModel.selectedFilter.value ?: 0)
    }

    private fun initBinding() {
        binding.activity = this
    }

    private fun initUi() {
        initRV()
        initChipGroup()
    }

    private fun initRV() {
        binding.bookmarkRvContents.adapter = bookmarkAdapter
    }

    private fun initChipGroup() {
        binding.bookmarkChipgroupExerciseFilter.apply {
            isSingleSelection = true

            setOnCheckedStateChangeListener { _, _ ->
                if(checkedChipId == -1) return@setOnCheckedStateChangeListener

                for(i in 0 until this.childCount) {
                    this[i].isClickable = true
                }
                this[checkedChipId].isClickable = false
                requestAPI(checkedChipId)
            }
        }
    }

    private fun observeFilterExercises() {
        viewModel.exerciseFilters.observe(this) {
            addChips(it)
        }
    }

    private fun addChips(items : MutableList<ResponseExercises.ExercisesList>) {
        binding.bookmarkChipgroupExerciseFilter.apply {
            for(i in getChipList(items)) {
                this.addView(i)
            }
            this[0].id.apply {
                check(this)
                requestAPI(this)
            }
        }
    }

    private fun requestAPI(checkedId : Int) {
        viewModel.setSelectedFilter(checkedId)
    }

    private fun getChipList(items : MutableList<ResponseExercises.ExercisesList>) : List<Chip> {
        return mutableListOf<Chip>().apply {
            for(item in items) {
                add(Chip(this@BookMarkActivity).apply {
                    id = item.id
                    text = item.name
                    setChipStyles()
                })
            }
            add(0, Chip(this@BookMarkActivity).apply {
                id = 0
                text = "전체"
                setChipStyles()
            })
        }
    }

    private fun Chip.setChipStyles() {
        this.apply {
            setTextAppearance(R.style.Certificate_Chip_Text_Style)
            setChipBackgroundColorResource(R.color.selector_bg_chip_selected)
            setChipStrokeColorResource(R.color.selector_stroke_chip_selected)
            chipStrokeWidth = 0.5F
            isCheckable = true
            isCheckedIconVisible = false
        }
    }

    private fun observeSelectedFilters() {
        viewModel.selectedFilter.observe(this) {
            requestBookmark(it)
        }
    }

    private fun requestBookmark(filter : Int) {
        lifecycleScope.launch {
            viewModel.requestBookmarkData(filter).collect {
                bookmarkAdapter.submitData(it)
            }
        }

        bookmarkAdapter.addLoadStateListener {
            binding.bookmarkLayoutNone.visibility = if(it.append.endOfPaginationReached && bookmarkAdapter.itemCount == 0) View.VISIBLE else View.GONE
        }
    }

    private fun onBookmarkClicked(index : Int) {
        startActivity(Intent(this, BoardDetailActivity::class.java).setType(index.toString()))
    }

    fun onSeeArticleClicked() {
        this.setResult(1001, Intent(this, MainActivity::class.java))
        finish()
    }

    fun onBackPress() {
        finish()
    }
}