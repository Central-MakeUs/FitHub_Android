package com.proteam.fithub.presentation.component

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.proteam.fithub.R
import com.proteam.fithub.databinding.DialogBottomSelectReportDeleteBinding

class ComponentBottomDialogSelectReportDelete(
    private val firstClick: () -> Unit,
    private val secondClick: () -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogBottomSelectReportDeleteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_bottom_select_report_delete,
            container,
            false
        )

        initUi()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initUi() {
        validateWithTag()

        binding.dialogBottomSelectReportDeleteTvFirst.setOnClickListener {
            firstClick.invoke()
            dismiss()
        }
        binding.dialogBottomSelectReportDeleteTvSecond.setOnClickListener {
            secondClick.invoke()
            dismiss()
        }
    }

    private fun validateWithTag() {
        when (tag) {
            "MINE" -> case_MINE()
            "NOT_MINE" -> case_NOT_MINE()
        }
    }

    private fun case_MINE() {
        binding.dialogBottomSelectReportDeleteTvFirst.apply {
            text = "수정하기"
            setTextColor(resources.getColor(R.color.text_default, null))
        }

        binding.dialogBottomSelectReportDeleteTvSecond.apply {
            text = "삭제하기"
            setTextColor(resources.getColor(R.color.color_error, null))
        }
    }

    private fun case_NOT_MINE() {
        binding.dialogBottomSelectReportDeleteTvFirst.apply {
            text = "게시글 신고하기"
            setTextColor(resources.getColor(R.color.color_error, null))
        }

        binding.dialogBottomSelectReportDeleteTvSecond.apply {
            text = "사용자 신고하기"
            setTextColor(resources.getColor(R.color.color_error, null))
        }
    }

}