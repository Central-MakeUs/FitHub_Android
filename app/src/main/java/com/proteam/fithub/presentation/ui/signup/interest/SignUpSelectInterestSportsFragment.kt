package com.proteam.fithub.presentation.ui.signup.interest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.data.data.SignUpInterestSports
import com.proteam.fithub.databinding.FragmentSignUpSelectInterestSportsBinding
import com.proteam.fithub.presentation.ui.signup.interest.adapter.SignUpSelectInterestSportsAdapter
import com.proteam.fithub.presentation.ui.signup.viewmodel.SignUpViewModel

class SignUpSelectInterestSportsFragment : Fragment() {
    private lateinit var binding : FragmentSignUpSelectInterestSportsBinding
    private val viewModel : SignUpViewModel by activityViewModels()
    private val adapter by lazy {
        SignUpSelectInterestSportsAdapter(getSportsData(), ::onSelectSports)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up_select_interest_sports, container ,false)

        initRV()
        observeSelectedSports()

        return binding.root
    }

    private fun initRV() {
        binding.fgSignUpSelectInterestSportsRvContents.adapter = adapter
    }

    private fun onSelectSports(item : SignUpInterestSports, checked : Boolean) {
        if(checked) viewModel.addSelectInterestSports(item) else viewModel.removeSelectInterestSports(item)
    }

    private fun observeSelectedSports() {
        viewModel.selectInterestSports.observe(viewLifecycleOwner) {
            binding.fgSignUpSelectInterestSportsBtnFinish.isEnabled = it.isNotEmpty()
        }
    }

    /** Dummy **/
    private fun getSportsData() : List<SignUpInterestSports> = listOf(
        SignUpInterestSports(R.drawable.ic_tennis, "테니스", false),
        SignUpInterestSports(R.drawable.ic_crossfit, "크로스핏", false),
        SignUpInterestSports(R.drawable.ic_poledance, "폴댄스", false),
        SignUpInterestSports(R.drawable.ic_swim, "수영", false),
        SignUpInterestSports(R.drawable.ic_skate, "스케이트", false),
        SignUpInterestSports(R.drawable.ic_climbing, "클라이밍", false)
    )
}