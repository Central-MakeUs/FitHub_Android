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
    private val firstClick: (Int?) -> Unit,
    private val secondClick: (Int?) -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogBottomSelectReportDeleteBinding

    private var user : Int = 0
    private var content : Int = 0

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

    fun getIndexData(user : Int, content : Int) {
        this.user = user
        this.content = content
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initUi() {
        validateWithTag()

        if(tag?.contains("NOT") == true) notMineClick() else mineClick()
    }

    private fun mineClick() {
        binding.dialogBottomSelectReportDeleteTvFirst.setOnClickListener {
            firstClick.invoke(content)
            dismiss()
        }
        binding.dialogBottomSelectReportDeleteTvSecond.setOnClickListener {
            secondClick.invoke(content)
            dismiss()
        }
    }

    private fun notMineClick() {
        binding.dialogBottomSelectReportDeleteTvFirst.setOnClickListener {
            firstClick.invoke(content)
            dismiss()
        }
        binding.dialogBottomSelectReportDeleteTvSecond.setOnClickListener {
            secondClick.invoke(user)
            dismiss()
        }
    }

    private fun validateWithTag() {
        when (tag) {
            "MINE" -> case_MINE()
            "NOT_MINE" -> case_NOT_MINE()
            "MINE_COMMENT" -> case_MINE_COMMENT()
            "NOT_MINE_COMMENT" -> case_NOT_MINE_COMMENT()
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

    private fun case_MINE_COMMENT() {
        binding.dialogBottomSelectReportDeleteTvFirst.visibility = View.GONE
        binding.dialogBottomSelectReportDeleteViewDivider.visibility = View.GONE

        binding.dialogBottomSelectReportDeleteTvSecond.apply {
            text = "댓글 삭제하기"
            setTextColor(resources.getColor(R.color.color_error, null))
        }
    }

    private fun case_NOT_MINE_COMMENT() {
        binding.dialogBottomSelectReportDeleteTvFirst.apply {
            text = "댓글 신고하기"
            setTextColor(resources.getColor(R.color.color_error, null))
        }

        binding.dialogBottomSelectReportDeleteTvSecond.apply {
            text = "사용자 신고하기"
            setTextColor(resources.getColor(R.color.color_error, null))
        }
    }

}