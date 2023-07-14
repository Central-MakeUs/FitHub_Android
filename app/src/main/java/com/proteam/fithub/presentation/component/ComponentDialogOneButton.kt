package com.proteam.fithub.presentation.component

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ComponentDialogOneButtonBinding

class ComponentDialogOneButton(private val onActionClick : () -> Unit) : DialogFragment() {
    private lateinit var binding : ComponentDialogOneButtonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.component_dialog_one_button, container ,false)

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
        params?.width = (deviceWidth * 0.95).toInt()
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    private fun initUi() {
        when(tag) {
            "RESET_PASSWORD" -> case_RESET_PASSWORD()
        }
    }

    private fun case_RESET_PASSWORD() {
        binding.componentDialogOneBtnTvTitle.text = "비밀번호 설정 완료"
        binding.componentDialogOneBtnTvNotice.text = "비밀번호가 재설정되었습니다.\n로그인을 진행할까요?"
        binding.componentDialogOneBtnTvAction.text = "로그인 하기"
    }

    fun onActionClick() {
        onActionClick.invoke()
        dismissDialog()
    }

    fun onDismissClick() = dismissDialog()

    private fun dismissDialog() = dismiss()
}