package com.proteam.fithub.presentation.component

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.kakao.sdk.common.KakaoSdk.type
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ComponentDialogYesNoBinding
import com.proteam.fithub.databinding.ComponentDialogYesNoWithParamBinding

class ComponentDialogYesNoWithParam(private val onActionClick : (Int?) -> Unit) : DialogFragment() {
    private lateinit var binding : ComponentDialogYesNoWithParamBinding

    private var index : Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.component_dialog_yes_no_with_param, container ,false)

        initBinding()
        initUi()

        return binding.root
    }

    private fun initBinding() {
        binding.dialog = this
    }

    override fun onResume() {
        super.onResume()
        val windowManager = requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params as WindowManager.LayoutParams

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun setIndex(index : Int) {
        this.index = index
    }

    private fun initUi() {
        when(tag) {
            "OTHER_USER_REPORT" -> case_OTHER_USER_REPORT()
            "MY_COMMENT" -> case_MY_COMMENT()
        }
    }

    private fun case_OTHER_USER_REPORT() {
        binding.componentDialogTvTitle.text = "사용자를 신고하시겠습니까?"
        binding.componentDialogTvNotice.text = "신고된 사용자는 차단되어 글과 댓글이\n 숨겨지고, 차단은 취소할 수 없습니다."
        binding.componentDialogTvAction.text = "신고"
    }

    private fun case_MY_COMMENT() {
        binding.componentDialogTvTitle.text = "댓글을 삭제하시겠어요?"
        binding.componentDialogTvNotice.text = "해당 댓글은 영구 삭제됩니다."
        binding.componentDialogTvAction.text = "삭제"
    }

    fun onActionClick() {
        onActionClick.invoke(index)
        dismissDialog()
    }

    fun onDismissClick() = dismissDialog()

    private fun dismissDialog() = dismiss()
}