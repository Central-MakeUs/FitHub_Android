package com.proteam.fithub.presentation.ui.main.community

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayoutMediator
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.databinding.FragmentCommunityBinding
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.main.community.adapter.CommunityPagerAdapter
import com.proteam.fithub.presentation.ui.main.community.dialog.DialogCommunityFloating
import com.proteam.fithub.presentation.ui.main.community.viewmodel.CommunityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment : Fragment() {
    private lateinit var binding : FragmentCommunityBinding
    private val viewModel : CommunityViewModel by activityViewModels()
    private val pagerAdapter by lazy {
        CommunityPagerAdapter(requireActivity()).also {
            it.setFragments()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_community, container, false)

        initBinding()
        initUi()
        observeFilterExercises()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initPager()
        initFab()
        initChipGroup()
    }

    private fun initPager() {
        binding.fgCommunityVpContainer.apply {
            adapter = pagerAdapter
            this.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            TabLayoutMediator(binding.fgCommunityTabCertAndBoard, this) { mTab, position ->
                mTab.text = setTabTitles()[position]
            }.attach()
            offscreenPageLimit = 1
        }

        binding.fgCommunityVpContainer.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.notifyChangeFragment(position)
            }
        })
    }

    private fun initFab() {
        observeForFab()
    }

    private fun observeForFab() {
        viewModel.isFabClicked.observe(viewLifecycleOwner) {
            val anim = ObjectAnimator.ofFloat(binding.fgCommunityFabWrite as View, "rotation", 0f, 45f).setDuration(100)
            anim.doOnEnd { _ ->
                DialogCommunityFloating(::onCertificateClicked, ::onBoardClicked).show(requireActivity().supportFragmentManager, "!")
            }
            if(it) anim.start() else initAnim()
        }
    }

    private fun initAnim() {
        val anim = ObjectAnimator.ofFloat(binding.fgCommunityFabWrite as View, "rotation", 45f, 0f).setDuration(1)
        anim.start()
    }

    private fun onCertificateClicked() {
        (requireActivity() as MainActivity).openWriteOrModifyCertificate("Write")
    }

    private fun onBoardClicked() {
        (requireActivity() as MainActivity).openWriteOrModifyBoard("Write")
    }

    private fun initChipGroup() {
        binding.fgCommunityChipgroupExerciseFilter.apply {
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
        viewModel.exerciseFilters.observe(viewLifecycleOwner) {
            addChips(it)
        }
    }

    private fun addChips(items : MutableList<ResponseExercises.ExercisesList>) {
        binding.fgCommunityChipgroupExerciseFilter.apply {
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
                add(Chip(requireContext()).apply {
                    id = item.id
                    text = item.name
                    setChipStyles()
                })
            }
            add(0, Chip(requireContext()).apply {
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

    /** Dummy **/
    private fun setTabTitles() : List<String> = listOf("운동 인증", "핏사이트")
}