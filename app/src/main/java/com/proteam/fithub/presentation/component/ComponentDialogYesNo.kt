package com.proteam.fithub.presentation.component

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
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
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params as WindowManager.LayoutParams

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initUi() {
        when(tag) {
            "NO_USER_INFO" -> case_NO_USER_INFO()
            "NO_ACCOUNT_INFO" -> case_NO_ACCOUNT_INFO()
            "4018" -> case_4018()
            "BACK_PRESSED_WHILE_WRITE" -> case_BACK_PRESSED_WHILE_WRITE()
            "MY_CERTIFICATE_ARTICLE" -> case_MY_CERTIFICATE_ARTICLE()
            "OTHER_USER_REPORT" -> case_OTHER_USER_REPORT()
            "MY_COMMENT" -> case_MY_COMMENT()
        }
    }

    private fun case_NO_USER_INFO() {
        binding.componentDialogTvTitle.text = "회원 정보가 없습니다."
        binding.componentDialogTvNotice.text = "입력하신 회원 정보는 존재하지 않아요.\n회원가입을 진행할까요?"
        binding.componentDialogTvAction.text = "회원가입 하기"
    }

    private fun case_NO_ACCOUNT_INFO() {
        binding.componentDialogTvTitle.text = "미가입 계정"
        binding.componentDialogTvNotice.text = "해당 번호로 가입된 계정이 없습니다."
        binding.componentDialogTvAction.text = "회원가입 하기"
    }

    private fun case_4018() {
        binding.componentDialogTvTitle.text = "안내"
        binding.componentDialogTvNotice.text = "이미 가입된 휴대폰번호 입니다.\n로그인을 진행할까요?"
        binding.componentDialogTvAction.text = "로그인 하러가기"
    }

    private fun case_BACK_PRESSED_WHILE_WRITE() {
        binding.componentDialogTvTitle.text = "작성을 종료하시겠습니까?"
        binding.componentDialogTvNotice.text = "작성하신 내용이 저장되지 않습니다."
        binding.componentDialogTvAction.text = "확인"
    }

    private fun case_MY_CERTIFICATE_ARTICLE() {
        binding.componentDialogTvTitle.text = "게시글을 삭제하시겠어요?"
        binding.componentDialogTvNotice.text = "해당 게시글은 영구 삭제됩니다."
        binding.componentDialogTvAction.text = "삭제"
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
        onActionClick.invoke()
        dismissDialog()
    }

    fun onDismissClick() = dismissDialog()

    private fun dismissDialog() = dismiss()
}