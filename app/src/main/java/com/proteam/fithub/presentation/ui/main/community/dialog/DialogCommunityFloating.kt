package com.proteam.fithub.presentation.ui.main.community.dialog

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.DialogCommunityFloatingBinding
import com.proteam.fithub.presentation.ui.main.community.viewmodel.CommunityViewModel

class DialogCommunityFloating(
    private val certificate: () -> Unit,
    private val board: () -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogCommunityFloatingBinding
    private val viewModel: CommunityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.dialog_community_floating, container, false)

        initBinding()

        dialog?.setOnDismissListener {
            setDismiss()
        }

        return binding.root
    }

    private fun initBinding() {
        binding.dialog = this
        binding.viewModel = viewModel
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
        val anim = ObjectAnimator.ofFloat(
            binding.dialogCommunityFloatingFabWriteOnDialog as View,
            "rotation",
            -45f,
            0f
        ).setDuration(100)
        anim.doOnEnd {
            initAnim()
            dismiss()
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

    fun onClickCertificate() {
        setDismiss()
        certificate.invoke()
    }

    fun onClickBoard() {
        setDismiss()
        board.invoke()
    }
}