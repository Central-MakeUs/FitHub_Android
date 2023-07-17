package com.proteam.fithub.presentation.ui.main.community

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.proteam.fithub.R
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
        Log.d("----", "onBoardClicked: ")
    }

    /** Dummy **/
    private fun setTabTitles() : List<String> = listOf("운동 인증", "핏사이트")
}