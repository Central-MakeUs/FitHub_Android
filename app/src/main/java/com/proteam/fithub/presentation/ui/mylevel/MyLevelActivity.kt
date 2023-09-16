package com.proteam.fithub.presentation.ui.mylevel

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseMyLevelData
import com.proteam.fithub.databinding.ActivityMyLevelBinding
import com.proteam.fithub.presentation.ui.mylevel.adapter.LevelAdapter
import com.proteam.fithub.presentation.ui.mylevel.viewmodel.MyLevelViewModel
import com.proteam.fithub.presentation.util.AnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.notify

@AndroidEntryPoint
class MyLevelActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMyLevelBinding
    private val viewModel : MyLevelViewModel by viewModels()
    private val levelAdapter by lazy {
        LevelAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_level)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        initBinding()
        initUi()
    }

    private fun initBinding() {
        binding.activity = this
        binding.lifecycleOwner = this
    }

    private fun initUi() {
        observeMyLevelData()
        initLevelRv()
    }

    private fun initLevelRv() {
        binding.levelDetailRvAllLevels.adapter = levelAdapter
    }

    private fun observeMyLevelData() {
        viewModel.myLevelData.observe(this) {
            binding.data = it
            binding.levelDetailComponentMyLevel.getLevel(it.myLevelInfo.level, it.myLevelInfo.levelName)
            it.myLevelInfo.levelSummary.descriptionStyle(it.myLevelInfo.level, it.myLevelInfo.levelName)
            it.fithubLevelInfo.fithubLevelList.setData()
        }
    }

    private fun String.descriptionStyle(level : Int, target : String) {
        val builder = SpannableStringBuilder(this)
        val colorSpan = ForegroundColorSpan(when(level) {
            1 -> resources.getColor(R.color.color_level_1, null)
            2 -> resources.getColor(R.color.color_level_2, null)
            3 -> resources.getColor(R.color.color_level_3, null)
            4 -> resources.getColor(R.color.color_level_4, null)
            5 -> resources.getColor(R.color.color_level_5, null)
            else -> R.color.transparent
        })

        builder.setSpan(colorSpan, this.indexOf(target), this.indexOf(target) + target.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.levelDetailTvMyLevelSummary.text = builder
    }

    private fun List<ResponseMyLevelData.FithubLevelDetail>.setData() {
        levelAdapter.levels = this.toMutableList()
        levelAdapter.notifyItemRangeChanged(0, this.size)
    }

    fun onBackPress() {
        finish()
    }
}