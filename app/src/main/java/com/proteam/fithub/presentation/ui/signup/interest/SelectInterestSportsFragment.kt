package com.proteam.fithub.presentation.ui.signup.interest

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSignUpSelectInterestSportsBinding
import com.proteam.fithub.presentation.ui.signup.SignUpActivity
import com.proteam.fithub.presentation.ui.signup.finish.SignUpFinishFragment
import com.proteam.fithub.presentation.ui.signup.interest.adapter.SignUpSelectInterestSportsAdapter
import com.proteam.fithub.presentation.ui.signup.viewmodel.SignUpViewModel
import com.proteam.fithub.presentation.util.ConvertBitmap.ConvertWhenSingle
import com.proteam.fithub.presentation.util.ConvertBitmap.deletePic
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
        Log.e("----", "initBinding: ${viewModel.userSelectedProfileImage.value}", )
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
        when(tag) {
            "Sign_Up" -> viewModel.requestSignUp(Convert().also { viewModel.setPathForDelete(it) }.getAbsolutePath())
        }
        observeSignUpResult()
    }

    private fun Convert() : Uri {
        val res = (viewModel.userSelectedProfileImage.value!!.toString().toUri().getAbsolutePath()).ConvertWhenSingle(requireActivity())

        return "content://${res.substring(9)}".toUri()
    }

    private fun Uri.getAbsolutePath() : String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c : Cursor = requireActivity().contentResolver.query(this, proj, null, null, null)!!
        val index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()
        return c.getString(index)
    }

    private fun observeSignUpResult() {
        viewModel.signUpState.observe(viewLifecycleOwner) {
            viewModel.imagePaths.value?.deletePic(requireActivity())
            if(it) (requireActivity() as SignUpActivity).changeFragments(SignUpFinishFragment())
        }
    }
}