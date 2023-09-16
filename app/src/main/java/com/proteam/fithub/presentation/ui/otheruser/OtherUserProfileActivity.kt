package com.proteam.fithub.presentation.ui.otheruser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.databinding.ActivityOtherUserProfileBinding
import com.proteam.fithub.presentation.component.ComponentBottomDialogSelectReportDelete
import com.proteam.fithub.presentation.component.ComponentDialogOneButton
import com.proteam.fithub.presentation.component.ComponentDialogYesNo
import com.proteam.fithub.presentation.component.ComponentDialogYesNoWithParam
import com.proteam.fithub.presentation.ui.detail.board.BoardDetailActivity
import com.proteam.fithub.presentation.ui.otheruser.adapter.OtherUserArticleAdapter
import com.proteam.fithub.presentation.ui.otheruser.viewmodel.OtherUserProfileViewModel
import com.proteam.fithub.presentation.util.AnalyticsHelper
import com.proteam.fithub.presentation.util.CustomSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OtherUserProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOtherUserProfileBinding
    private val viewModel : OtherUserProfileViewModel by viewModels()
    private val articleAdapter by lazy {
        OtherUserArticleAdapter(::onArticleClicked)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_user_profile)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        requestUserData()
        initBinding()
        initUi()
        observeFilterExercises()
    }

    private fun initBinding() {
        binding.activity = this
        binding.lifecycleOwner = this
    }

    private fun initUi() {
        initChipGroup()
        initArticleRv()
    }

    private fun initArticleRv() {
        binding.otherUserProfileRvArticles.adapter = articleAdapter
    }

    private fun requestUserData() {
        intent.type?.let { viewModel.requestOtherUserProfile(it.toInt())}
        observeOtherUserProfileData()
    }

    private fun observeOtherUserProfileData() {
        viewModel.otherUserProfile.observe(this) {
            binding.data = it
            binding.otherUserProfileExercisesComponentExercise.getExercise(it.mainExerciseInfo.category)
            binding.otherUserProfileExercisesComponentLevel.getLevel(it.mainExerciseInfo.level, it.mainExerciseInfo.gradeName)
        }

        viewModel.otherUserProfileStatus.observe(this) {
            if(it == 4064 || it == 4013) {
                ComponentDialogOneButton(::onBackPress).show(supportFragmentManager, "4064")
            }
        }
    }

    private fun observeFilterExercises() {
        viewModel.exerciseFilters.observe(this) {
            addChips(it)
        }
    }

    private fun initChipGroup() {
        binding.otherUserProfileChipgroupExerciseFilter.apply {
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
        observeSelectedFilter()
    }

    private fun getChipList(items : MutableList<ResponseExercises.ExercisesList>) : List<Chip> {
        return mutableListOf<Chip>().apply {
            for(item in items) {
                add(Chip(this@OtherUserProfileActivity).apply {
                    id = item.id
                    text = item.name
                    setChipStyles()
                })
            }
            add(0, Chip(this@OtherUserProfileActivity).apply {
                id = 0
                text = "전체"
                setChipStyles()
            })
        }
    }

    private fun addChips(items : MutableList<ResponseExercises.ExercisesList>) {
        binding.otherUserProfileChipgroupExerciseFilter.apply {
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

    private fun observeSelectedFilter() {
        viewModel.selectedFilter.observe(this) {
            requestArticles(it)
        }
    }

    private fun requestArticles(filter : Int) {
        lifecycleScope.launch {
            viewModel.requestOtherUserArticles(intent.type!!.toInt(), filter).collect {
                articleAdapter.submitData(it)
            }
        }

        articleAdapter.addLoadStateListener {
            binding.otherUserProfileLayoutArticleNone.visibility = if(it.append.endOfPaginationReached && articleAdapter.itemCount == 0) View.VISIBLE else View.GONE
        }
    }

    fun onOptionClicked() {
        ComponentBottomDialogSelectReportDelete(::none, ::reportUserDialog).also { it.getIndexData(intent.type!!.toInt(), 0) }.show(supportFragmentManager, "NOT_MY_PROFILE")
    }

    private fun onArticleClicked(index : Int) {
        startActivity(Intent(this, BoardDetailActivity::class.java).setType(index.toString()))
    }

    fun onBackPress() {
        finish()
    }

    private fun none(noinline : Int?) {}
    private fun reportUserDialog(index : Int?) {
        ComponentDialogYesNoWithParam(::reportUser).also { it.setIndex(index!!) }.show(supportFragmentManager, "OTHER_USER_REPORT")
    }

    private fun reportUser(index : Int?) {
        viewModel.requestReportUser(index!!)
        observeReportStatus()
    }

    private fun observeReportStatus() {
        viewModel.reportStatus.observe(this) {
            if(it == 2000) finish()
            else CustomSnackBar.makeSnackBar(binding.root, it.toString()).show()
        }
    }
}