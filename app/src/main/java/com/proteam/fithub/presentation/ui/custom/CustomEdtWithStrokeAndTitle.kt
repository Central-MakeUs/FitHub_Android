package com.proteam.fithub.presentation.ui.custom

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.proteam.fithub.R
import com.proteam.fithub.databinding.CustomEdtWithStrokeAndTitleBinding

class CustomEdtWithStrokeAndTitle(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private lateinit var binding: CustomEdtWithStrokeAndTitleBinding

    var status = MutableLiveData<Boolean>()

    init {
        initBinding()
        setNormalState()
    }
    fun getAttr(type : String) {
        when(type) {
            "phone" -> setAsPhone()
            "password" -> setAsPassword()
        }
    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_edt_with_stroke_and_title, this, false)
        binding.layout = this
        addView(binding.root)
    }

    fun setNormalState() {
        binding.edtWithStrokeTitleCardContainer.strokeColor = context.resources.getColor(R.color.gray_400, null)
        binding.edtWithStrokeTitleBtnDeleteOrError.setImageResource(com.google.android.material.R.color.mtrl_btn_transparent_bg_color)
    }

    fun setDoneState() {
        binding.edtWithStrokeTitleCardContainer.strokeColor = context.resources.getColor(R.color.gray_600, null)
        binding.edtWithStrokeTitleBtnDeleteOrError.setImageResource(R.drawable.ic_edt_delete)
    }

    private fun setAsPhone() {
        binding.edtWithStrokeTitleTvTitle.text = "휴대폰 번호"
        binding.edtWithStrokeTitleEdtContent.hint = "01012345678"
        binding.edtWithStrokeTitleEdtContent.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(11))
        binding.edtWithStrokeTitleEdtContent.inputType = InputType.TYPE_CLASS_NUMBER

        binding.edtWithStrokeTitleEdtContent.addTextChangedListener {
            if(it.isNullOrEmpty()) {
                setNormalState()
            } else {
                setDoneState()
                status.value = it.length == 11
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
        binding.edtWithStrokeTitleEdtContent.setText("")
    }
}