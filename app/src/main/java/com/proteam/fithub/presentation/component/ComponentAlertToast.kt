package com.proteam.fithub.presentation.component

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ComponentToastBinding

class ComponentAlertToast() : DialogFragment() {
    private lateinit var binding: ComponentToastBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.component_toast, container, false)

        initBinding()
        initUi()

        return binding.root
    }

    private fun initBinding() {
    }

    override fun onResume() {
        super.onResume()
        val windowManager =
            requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        params?.width = (deviceWidth * 0.8).toInt()
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setDimAmount(0F)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initUi() {
        binding.componentToastText.text = when (tag) {
            "4013" -> case_4013()
            "4014" -> case_4014()
            "4015" -> case_4015()
            "4016" -> case_4016()
            "4017" -> case_4017()
            "4019" -> case_4019()
            "4020" -> case_4020()
            else -> tag
        }
        Handler().postDelayed(Runnable {
            dismissDialog()
        }, 3000)
    }

    private fun case_4013() = "존재하지 않는 토큰입니다"

    private fun case_4014() = "인증번호가 일치하지 않습니다"
    private fun case_4015() = "유효시간이 초과되었습니다"
    private fun case_4016() = "존재하지 않는 휴대폰번호입니다"
    private fun case_4017() = "운동 카테고리가 잘못되었습니다"
    private fun case_4019() = "존재하지 않는 유저입니다"
    private fun case_4020()  = "비밀번호가 일치하지 않습니다"

    private fun dismissDialog() = dismiss()
}