package com.proteam.fithub.presentation.ui.signup.interest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSignUpSelectInterestSportsBinding
import com.proteam.fithub.presentation.ui.signup.interest.adapter.SignUpSelectInterestSportsAdapter
import com.proteam.fithub.presentation.ui.signup.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectInterestSportsFragment : Fragment() {
    private lateinit var binding : FragmentSignUpSelectInterestSportsBinding
    private val viewModel : SignUpViewModel by activityViewModels()
    private val adapter by lazy {
        SignUpSelectInterestSportsAdapter(::onSelectSports)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_select_interest_sports, container ,false)

        initBinding()
        requestExercises()
        initRV()
        observeSelectedSports()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
    }

    private fun requestExercises() {
        viewModel.requestExercises()
        observeExercises()
    }

    private fun observeExercises() {
        viewModel.existExercises.observe(viewLifecycleOwner) {
            adapter.sports = it
            adapter.notifyItemRangeChanged(0, it.size)
        }
    }

    private fun initRV() {
        binding.fgSignUpSelectInterestSportsRvContents.adapter = adapter
    }

    private fun onSelectSports(item : Int, checked : Boolean) {
        if(checked) viewModel.addSelectInterestSports(item) else viewModel.removeSelectInterestSports(item)
    }

    private fun observeSelectedSports() {
        viewModel.selectExercises.observe(viewLifecycleOwner) {
            binding.fgSignUpSelectInterestSportsBtnFinish.isEnabled = it.isNotEmpty()
        }
    }

    fun onFinishSignUp() {
        viewModel.showAllUserInputData()
    }
}