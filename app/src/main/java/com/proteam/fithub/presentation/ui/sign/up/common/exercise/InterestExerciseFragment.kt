package com.proteam.fithub.presentation.ui.sign.up.common.exercise

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentInterestExercisesBinding
import com.proteam.fithub.presentation.ui.sign.up.common.exercise.viewmodel.InterestExerciseViewModel
import com.proteam.fithub.presentation.ui.sign.up.social.SocialSignUpActivity
import com.proteam.fithub.presentation.ui.sign.up.social.viewmodel.SocialSignUpViewModel
import com.proteam.fithub.presentation.ui.signup.interest.adapter.SignUpSelectInterestSportsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterestExerciseFragment : Fragment() {
    private lateinit var binding : FragmentInterestExercisesBinding
    private val viewModel : InterestExerciseViewModel by viewModels()
    private val socialViewModel : SocialSignUpViewModel by activityViewModels()
    private val adapter by lazy {
        SignUpSelectInterestSportsAdapter(::onSelectSports)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_interest_exercises, container ,false)

        requestExercises()

        initBinding()
        initUi()

        viewModel.selectExercises.observe(viewLifecycleOwner) {
        }

        return binding.root
    }

    private fun requestExercises() {
        viewModel.requestExercises()
        observeExercises()
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initExerciseRV()
    }

    private fun observeExercises() {
        viewModel.exercises.observe(viewLifecycleOwner) {
            adapter.sports = it
            adapter.notifyItemRangeChanged(0, it.size)
        }
    }

    private fun initExerciseRV() {
        binding.fgSignUpSelectInterestSportsRvContents.adapter = adapter
    }

    private fun onSelectSports(item : Int, checked : Boolean) {
        if(checked) viewModel.addSelectInterestSports(item) else viewModel.removeSelectInterestSports(item)
    }

    fun onRequestSignUp() {
        when(tag) {
            "Social" -> {
                socialViewModel.setUserInterestExercise(viewModel.selectExercises.value!!)
                (requireActivity() as SocialSignUpActivity).requestSocialSignUp()
            }
        }
    }
}