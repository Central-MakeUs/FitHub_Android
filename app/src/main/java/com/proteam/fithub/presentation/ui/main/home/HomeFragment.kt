package com.proteam.fithub.presentation.ui.main.home

import android.graphics.Rect
import android.os.Bundle
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
import com.proteam.fithub.databinding.FragmentHomeBinding
import com.proteam.fithub.presentation.ui.main.MainViewModel
import com.proteam.fithub.presentation.ui.main.home.adapter.HomeNearGymAdapter
import com.proteam.fithub.presentation.ui.main.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private val viewModel : HomeViewModel by viewModels()
    private val mainViewModel : MainViewModel by activityViewModels()
    private val gymAdapter : HomeNearGymAdapter by lazy {
        HomeNearGymAdapter(::onGymClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)


        binding.fgHomeCardCertificatePercentLayoutExercise.getExercise("폴댄스")

        initUi()
        requestData()

        return binding.root
    }

    private fun initUi() {
        initGymRV()
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
        viewModel.requestExerciseCategory()
        observeSports()
    }

    private fun observeSports() {
        viewModel.exercises.observe(viewLifecycleOwner) {
            gymAdapter.exercises = it
            gymAdapter.notifyItemRangeChanged(0, it.size)
        }
    }

    private fun onGymClicked(index : Int) {
        Toast.makeText(requireContext(), index, Toast.LENGTH_SHORT).show()
    }
}