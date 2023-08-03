package com.proteam.fithub.presentation.util

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.proteam.fithub.R
import com.proteam.fithub.databinding.TestBinding

object CustomSnackBar {

    fun makeSnackBar(view: View, message: String): Snackbar {
        val snackBar = Snackbar.make(view, message, 3500)

        val binding = DataBindingUtil.inflate<TestBinding>(
            LayoutInflater.from(view.context),
            R.layout.test,
            view.parent as ViewGroup,
            false
        )

        binding.snackbarText.setTextColor(Color.WHITE)
        binding.snackbarText.text = when (message) {
            "4013" -> case_4013()
            "4014" -> case_4014()
            "4015" -> case_4015()
            "4016" -> case_4016()
            "4017" -> case_4017()
            "4019" -> case_4019()
            "4020" -> case_4020()
            "4041" -> case_4041()
            "4042" -> case_4042()
            "4054" -> case_4054()
            else -> message
        }

        val params = snackBar.view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.CENTER
        snackBar.view.layoutParams = params

        val snackbarLayout = snackBar.view as Snackbar.SnackbarLayout
        snackbarLayout.setBackgroundColor(Color.TRANSPARENT)
        snackbarLayout.removeAllViews()
        snackbarLayout.addView(binding.root)

        return snackBar
    }

    private fun case_4013() = "존재하지 않는 토큰입니다"
    private fun case_4014() = "인증번호가 일치하지 않습니다"
    private fun case_4015() = "유효시간이 초과되었습니다"
    private fun case_4016() = "존재하지 않는 휴대폰번호입니다"
    private fun case_4017() = "운동 카테고리가 잘못되었습니다"
    private fun case_4019() = "존재하지 않는 유저입니다"
    private fun case_4020() = "비밀번호가 일치하지 않습니다"
    private fun case_4041() = "존재하지 않는 운동인증입니다."
    private fun case_4042() = "다른 사람의 운동 인증입니다"
    private fun case_4054() = "자신의 댓글에는 좋아요를 누를 수 없습니다"

}