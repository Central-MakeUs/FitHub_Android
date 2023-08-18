package com.proteam.fithub.presentation.ui.main.home

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseHomeData
import com.proteam.fithub.databinding.FragmentHomeBinding
import com.proteam.fithub.presentation.LoadingDialog
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.main.MainViewModel
import com.proteam.fithub.presentation.ui.main.home.adapter.HomeBestRankAdapter
import com.proteam.fithub.presentation.ui.main.home.adapter.HomeNearGymAdapter
import com.proteam.fithub.presentation.ui.main.home.viewmodel.HomeViewModel
import com.proteam.fithub.presentation.ui.otheruser.OtherUserProfileActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val gymAdapter: HomeNearGymAdapter by lazy {
        HomeNearGymAdapter(::onGymClicked)
    }
    private val rankingAdapter : HomeBestRankAdapter by lazy {
        HomeBestRankAdapter(::onProfileClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)


        binding.fgHomeCardCertificatePercentLayoutExercise.getExercise("폴댄스")

        initUi()
        initBinding()
        requestData()

        return binding.root
    }

    private fun initUi() {
        initGymRV()
        initRankingRV()
    }

    private fun initBinding() {
        binding.fragment = this
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initGymRV() {
        binding.fgHomeRvLookAroundGymNearMe.adapter = gymAdapter
        binding.fgHomeRvLookAroundGymNearMe.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.right = 20
            }
        })
    }

    private fun requestData() {
        showLoadingDialog()
        viewModel.requestHomeData()
        viewModel.requestExerciseCategory()
        observeHome()
        observeSports()
        observeTotalStatus()
    }

    private fun observeHome() {
        viewModel.homeData.observe(viewLifecycleOwner) {
            if(it.code == 2000) {
                notifyUi(it.result)
            }
        }
    }

    private fun observeSports() {
        viewModel.exercises.observe(viewLifecycleOwner) {
            gymAdapter.exercises = it
            gymAdapter.notifyItemRangeChanged(0, it.size)
        }
    }

    private fun observeTotalStatus() {
        viewModel.totalState.observe(viewLifecycleOwner) {
            if(it) dismissLoadingDialog()
        }
    }

    private fun notifyUi(item: ResponseHomeData.ResultHomeData) {
        binding.homeData = item
        binding.fgHomeTvUserLevel.setTextColor(resources.getColor(
            when (item.userInfo.gradeName) {
                "우주먼지" -> R.color.color_level_1
                "성운" -> R.color.color_level_2
                "태양" -> R.color.color_level_3
                "블랙홀" -> R.color.color_level_4
                "은하" -> R.color.color_level_5
                else -> R.color.transparent
            }, null)
        )
        binding.fgHomeCardCertificatePercentLayoutExercise.getExercise(item.userInfo.category)
        rankingAdapter.apply {
            rankingData = item.bestRecorderList as MutableList
            notifyItemRangeChanged(0, item.bestRecorderList.size)
        }

    }

    private fun initRankingRV() {
        binding.fgHomeRvBestRank.adapter = rankingAdapter
    }

    private fun onGymClicked(index: Int) {
        Toast.makeText(requireContext(), index, Toast.LENGTH_SHORT).show()
    }

    fun onGotoCertificateClicked() {
        (requireActivity() as MainActivity).openWriteOrModifyCertificate("Write")
    }

    fun onMyLevelInfoClicked() {
        (requireActivity() as MainActivity).openMyLevelActivity()
    }

    private fun onProfileClicked(index : Int) {
        startActivity(Intent(requireActivity(), OtherUserProfileActivity::class.java).setType(index.toString()))
    }


    private var loadingDialog = LoadingDialog()
    private fun showLoadingDialog() = loadingDialog.show(requireActivity().supportFragmentManager, null)
    private fun dismissLoadingDialog() = loadingDialog.dismiss()
}