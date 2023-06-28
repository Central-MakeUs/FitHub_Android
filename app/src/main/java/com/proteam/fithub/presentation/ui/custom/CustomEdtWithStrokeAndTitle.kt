package com.proteam.fithub.presentation.ui.custom

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.proteam.fithub.R
import com.proteam.fithub.databinding.CustomEdtWithStrokeAndTitleBinding

class CustomEdtWithStrokeAndTitle(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private lateinit var binding: CustomEdtWithStrokeAndTitleBinding

    var status = MutableLiveData<Boolean>()
    var curState = ""

    init {
        initBinding()
        setNormalState()
    }
    fun getAttr(type : String) {
        when(type) {
            "phone" -> setAsPhone(false)
            "password" -> setAsPassword()
            "phone_error" -> setAsPhone(true)
        }
    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_edt_with_stroke_and_title, this, false)
        binding.layout = this
        addView(binding.root)
    }

    fun setNormalState() {
        curState = "NORMAL"
        binding.edtWithStrokeTitleCardContainer.strokeColor = context.resources.getColor(R.color.gray_400, null)
        binding.edtWithStrokeTitleBtnDeleteOrError.setImageResource(com.google.android.material.R.color.mtrl_btn_transparent_bg_color)

        binding.edtWithStrokeTitleTvTitle.setTextColor(resources.getColor(R.color.gray_400, null))

        binding.edtWithStrokeTitleTvErrorOrCorrect.visibility = View.GONE
    }

    fun setDoneState() {
        curState = "DONE"
        binding.edtWithStrokeTitleCardContainer.strokeColor = context.resources.getColor(R.color.gray_600, null)
        binding.edtWithStrokeTitleBtnDeleteOrError.setImageResource(R.drawable.ic_edt_delete)
        binding.edtWithStrokeTitleTvTitle.setTextColor(resources.getColor(R.color.gray_400, null))

        binding.edtWithStrokeTitleTvErrorOrCorrect.visibility = View.GONE
    }

    fun setErrorState(type : String) {
        curState = "ERROR"
        binding.edtWithStrokeTitleCardContainer.strokeColor = context.resources.getColor(R.color.color_error, null)
        binding.edtWithStrokeTitleBtnDeleteOrError.setImageResource(R.drawable.ic_edt_error)
        binding.edtWithStrokeTitleTvTitle.setTextColor(resources.getColor(R.color.color_error, null))
        binding.edtWithStrokeTitleTvErrorOrCorrect.visibility = View.VISIBLE
        binding.edtWithStrokeTitleTvErrorOrCorrect.setTextColor(resources.getColor(R.color.color_error, null))
        binding.edtWithStrokeTitleTvErrorOrCorrect.text = when(type) {
            "phone" -> "휴대폰 번호가 올바르지 않습니다."
            else -> "1"
        }
    }

    private fun setAsPhone(error : Boolean) {
        binding.edtWithStrokeTitleTvTitle.text = "휴대폰 번호"
        binding.edtWithStrokeTitleEdtContent.hint = "01012345678"
        binding.edtWithStrokeTitleEdtContent.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
        binding.edtWithStrokeTitleEdtContent.inputType = InputType.TYPE_CLASS_NUMBER

        binding.edtWithStrokeTitleEdtContent.addTextChangedListener {
            if(it.isNullOrEmpty()) {
                setNormalState()
            } else {
                if(error) {
                    if(it.length != 11 && (it.length > 3 && it.substring(0, 3) != "010")) {
                        setErrorState("phone")
                    }
                }
                 else {
                    setDoneState()
                }
                status.value = if(!error) it.length == 11 else it.length == 11 && (it.length > 3 && it.substring(0, 3) == "010")
            }
        }
    }

    private fun setAsPassword() {
        binding.edtWithStrokeTitleTvTitle.text = "비밀번호"
        binding.edtWithStrokeTitleEdtContent.hint = "비밀번호 입력"

        binding.edtWithStrokeTitleEdtContent.addTextChangedListener {
            status.value = it?.isNotEmpty()
            if(it.isNullOrEmpty()) {
                setNormalState()
            } else {
                setDoneState()
            }
        }
    }

    fun returnInput() : String = binding.edtWithStrokeTitleEdtContent.text.toString()

    fun onDeleteClicked() {
        try {
            require(curState != "ERROR")
            binding.edtWithStrokeTitleEdtContent.setText("")
        } catch (e : IllegalArgumentException) {
            Log.d("----", "onDeleteClicked: 예외처리")
        }
    }
}