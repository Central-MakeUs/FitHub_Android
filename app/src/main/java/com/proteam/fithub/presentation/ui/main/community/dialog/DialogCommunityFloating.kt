package com.proteam.fithub.presentation.ui.main.community.dialog

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.DialogCommunityFloatingBinding
import com.proteam.fithub.presentation.ui.main.community.viewmodel.CommunityViewModel

class DialogCommunityFloating : DialogFragment() {
    private lateinit var binding: DialogCommunityFloatingBinding
    private val viewModel : CommunityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_community_floating, container, false)

        initBinding()

        return binding.root
    }

    private fun initBinding() {
        binding.dialog = this
        binding.viewModel = viewModel
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun setDismiss() {
        viewModel.closeFabDialog()
        val anim = ObjectAnimator.ofFloat(binding.dialogCommunityFloatingFabWriteOnDialog as View, "rotation", -45f, 0f).setDuration(200)
        anim.doOnEnd {
            dismiss()
            initAnim()
        }
        anim.start()
    }

    private fun initAnim() {
        val anim = ObjectAnimator.ofFloat(
            binding.dialogCommunityFloatingFabWriteOnDialog as View,
            "rotation",
            0f,
            -45f
        ).setDuration(1)
        anim.start()
    }

}