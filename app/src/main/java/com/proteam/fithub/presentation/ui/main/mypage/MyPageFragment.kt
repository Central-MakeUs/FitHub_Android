package com.proteam.fithub.presentation.ui.main.mypage

import android.app.ActionBar.LayoutParams
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.google.android.material.tabs.TabLayoutMediator
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentMypageBinding
import com.proteam.fithub.presentation.ui.main.mypage.adapter.MyPageExerciseAdapter
import com.proteam.fithub.presentation.ui.main.mypage.adapter.MyPageUpperMenuAdapter
import com.proteam.fithub.presentation.ui.main.mypage.viewmodel.MyPageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private lateinit var binding : FragmentMypageBinding
    private val viewModel : MyPageViewModel by viewModels()
    private val exerciseAdapter by lazy {
        MyPageExerciseAdapter()
    }
    private val upperMenuAdapter by lazy {
        MyPageUpperMenuAdapter(returnUpperMenuTitles())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)

        observeMyPageData()
        initBinding()
        initUi()

        return binding.root
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initExercisePager()
        initUpperMenuRV()
    }

    private fun initExercisePager() {
        binding.fgMypageVpExercises.apply {
            adapter = exerciseAdapter
            TabLayoutMediator(binding.fgMypageTabExerciseIndicator, this) {tab, _ ->
            }.attach()
        }
    }

    private fun initUpperMenuRV() {
        binding.fgMypageRvUpperMenu.adapter = upperMenuAdapter
    }

    private fun observeMyPageData() {
        viewModel.myPageData.observe(viewLifecycleOwner) {
            binding.myPageData = it

            binding.fgMypageComponentLevel.getLevel(it.myInfo.mainExerciseInfo.level, it.myInfo.mainExerciseInfo.gradeName)
            binding.fgMypageComponentExercise.getExercise(it.myInfo.mainExerciseInfo.category)

            exerciseAdapter.apply {
                exercises = it.myExerciseList.toMutableList()
                notifyItemRangeChanged(0, it.myExerciseList.size)
            }
        }
    }

    /** Dummy **/
    private fun returnUpperMenuTitles() : List<String> = listOf("개인 정보 설정", "알림 설정", "학원 등록 요청", "약관 및 정책")
}