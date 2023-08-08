package com.proteam.fithub.presentation.ui.managewrite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.databinding.ActivityManageMyWriteBinding
import com.proteam.fithub.presentation.ui.managewrite.adapter.ManageMyWritePagerAdapter
import com.proteam.fithub.presentation.ui.managewrite.viewmodel.ManageMyWriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageMyWriteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityManageMyWriteBinding
    private val viewModel : ManageMyWriteViewModel by viewModels()
    private val pagerAdapter by lazy {
        ManageMyWritePagerAdapter(this).also { it.setFragments() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_my_write)

        initBinding()
        initUi()
        observeFilterExercises()
    }

    private fun initBinding() {
        binding.activity = this
    }

    private fun initUi() {
        initPager()
        initChipGroup()
    }

    private fun initPager() {
        binding.manageMyWriteVpCertAndBoard.apply {
            adapter = pagerAdapter
            this.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            TabLayoutMediator(binding.manageMyWriteTabCertAndBoard, this) { mTab, position ->
                mTab.text = setTabTitles()[position]
            }.attach()
            offscreenPageLimit = 1
        }
    }

    private fun observeFilterExercises() {
        viewModel.exerciseFilters.observe(this) {
            addChips(it)
        }
    }

    private fun initChipGroup() {
        binding.manageMyWriteChipgroupExerciseFilter.apply {
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

    private fun getChipList(items : MutableList<ResponseExercises.ExercisesList>) : List<Chip> {
        return mutableListOf<Chip>().apply {
            for(item in items) {
                add(Chip(this@ManageMyWriteActivity).apply {
                    id = item.id
                    text = item.name
                    setChipStyles()
                })
            }
            add(0, Chip(this@ManageMyWriteActivity).apply {
                id = 0
                text = "전체"
                setChipStyles()
            })
        }
    }

    private fun addChips(items : MutableList<ResponseExercises.ExercisesList>) {
        binding.manageMyWriteChipgroupExerciseFilter.apply {
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

    fun onBackPress() {
        finish()
    }

    /** Dummy **/
    private fun setTabTitles() : List<String> = listOf("운동 인증", "핏사이트")
}