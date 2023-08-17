package com.proteam.fithub.presentation.component

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
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
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params as WindowManager.LayoutParams

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initUi() {
        when(tag) {
            "RESET_PASSWORD" -> case_RESET_PASSWORD()
            "SIGN_OUT" -> case_SIGN_OUT()
            "4064" -> case_4064()
        }
    }

    private fun case_RESET_PASSWORD() {
        binding.componentDialogOneBtnTvTitle.text = "비밀번호 설정 완료"
        binding.componentDialogOneBtnTvNotice.text = "비밀번호가 재설정되었습니다.\n로그인을 진행할까요?"
        binding.componentDialogOneBtnTvAction.text = "로그인 하기"
    }

    private fun case_SIGN_OUT() {
        binding.componentDialogOneBtnTvTitle.text = "회원 탈퇴 완료"
        binding.componentDialogOneBtnTvNotice.text = "안전하게 탈퇴가 완료되었습니다.\n다음에 또 만나요!"
        binding.componentDialogOneBtnTvAction.text = "확인"
    }

    private fun case_4064() {
        binding.componentDialogOneBtnTvTitle.text = "조회 할 수 없는 사용자"
        binding.componentDialogOneBtnTvNotice.text = "프로필을 조회할 수 없는 사용자입니다."
        binding.componentDialogOneBtnTvAction.text = "뒤로가기"
    }

    fun onActionClick() {
        onActionClick.invoke()
        dismissDialog()
    }

    fun onDismissClick() = dismissDialog()

    private fun dismissDialog() = dismiss()
}