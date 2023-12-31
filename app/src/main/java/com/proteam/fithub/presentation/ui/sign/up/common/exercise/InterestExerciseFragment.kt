package com.proteam.fithub.presentation.ui.sign.up.common.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentInterestExercisesBinding
import com.proteam.fithub.presentation.util.LoadingDialog
import com.proteam.fithub.presentation.ui.sign.up.common.exercise.adapter.InterestExerciseAdapter
import com.proteam.fithub.presentation.ui.sign.up.common.exercise.viewmodel.InterestExerciseViewModel
import com.proteam.fithub.presentation.ui.sign.up.number.NumberSignUpActivity
import com.proteam.fithub.presentation.ui.sign.up.number.viewmodel.NumberSignUpViewModel
import com.proteam.fithub.presentation.ui.sign.up.social.SocialSignUpActivity
import com.proteam.fithub.presentation.ui.sign.up.social.viewmodel.SocialSignUpViewModel
import com.proteam.fithub.presentation.util.AnalyticsHelper
import com.proteam.fithub.presentation.util.CustomSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterestExerciseFragment : Fragment() {
    private lateinit var binding : FragmentInterestExercisesBinding
    private val viewModel : InterestExerciseViewModel by viewModels()
    private val socialViewModel : SocialSignUpViewModel by activityViewModels()
    private val numberViewModel : NumberSignUpViewModel by activityViewModels()
    private val adapter by lazy {
        InterestExerciseAdapter(::onSelectSports)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_interest_exercises, container ,false)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        requestExercises()

        initBinding()
        initUi()

        return binding.root
    }

    private fun requestExercises() {
        viewModel.requestExercises().also { showLoadingDialog() }
        observeExercises()
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initExerciseRV()
        initLegacy()
    }

    private fun observeExercises() {
        viewModel.exercises.observe(viewLifecycleOwner) {
            dismissLoadingDialog()
            adapter.sports = it
            adapter.notifyItemRangeChanged(0, it.size)
        }
    }

    private fun initExerciseRV() {
        binding.fgSignUpSelectInterestSportsRvContents.adapter = adapter
        binding.fgSignUpSelectInterestSportsRvContents.itemAnimator = null
        adapter.selected = select() as MutableList
    }

    private fun onSelectSports(item : Int) {
        viewModel.setInterestSports(item)
    }

    fun onRequestSignUp() {
        when(tag) {
            "Social" -> {
                socialViewModel.setUserInterestExercise(viewModel.selectExercises.value!!)
                (requireActivity() as SocialSignUpActivity).requestSocialSignUp()
            }
            "Number" -> {
                numberViewModel.setUserInterestExercise(viewModel.selectExercises.value!!)
                (requireActivity() as NumberSignUpActivity).requestNumberSignUp()
            }
            "CHANGE" -> {
                viewModel.requestModifyExercise()
                observeModifyStatus()
            }
        }
    }

    private fun initLegacy() {
        if(tag == "CHANGE") {
            binding.fgSignUpSelectInterestSportsBtnFinish.text = "변경 하기"
            viewModel.requestMainExercise()
            setLegacyWhenModify()
        }
    }

    private fun setLegacyWhenModify() {
        viewModel.selectExercises.observe(viewLifecycleOwner) {
            val position = adapter.sports.indexOf(adapter.sports.find { it2 -> it2.id == it })
            adapter.resetSelected(position)
        }
    }

    private fun observeModifyStatus() {
        viewModel.modifyStatue.observe(viewLifecycleOwner) {
            if(it == 2000) requireActivity().finish()
            else CustomSnackBar.makeSnackBar(binding.root, it.toString())
        }
    }

    private var loadingDialog = LoadingDialog()
    private fun showLoadingDialog() = loadingDialog.show(requireActivity().supportFragmentManager, null)
    private fun dismissLoadingDialog() = loadingDialog.dismiss()

    private fun select() : List<Boolean> = listOf(false, false, false, false,false, false)
}