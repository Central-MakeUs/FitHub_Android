package com.proteam.fithub.presentation.component

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.kakao.sdk.common.KakaoSdk.type
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ComponentDialogYesNoBinding

class ComponentDialogYesNo(private val onActionClick : () -> Unit) : DialogFragment() {
    private lateinit var binding : ComponentDialogYesNoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.component_dialog_yes_no, container ,false)

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
            "NO_ACCOUNT_INFO" -> case_NO_ACCOUNT_INFO()
        }
    }

    private fun case_NO_ACCOUNT_INFO() {
        binding.componentDialogTvTitle.text = "미가입 계정"
        binding.componentDialogTvNotice.text = "해당 번호로 가입된 계정이 없습니다."
        binding.componentDialogTvAction.text = "회원가입 하기"
    }

    fun onActionClick() {
        onActionClick.invoke()
        dismissDialog()
    }

    fun onDismissClick() = dismissDialog()

    private fun dismissDialog() = dismiss()
}