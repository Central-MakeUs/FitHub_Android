package com.proteam.fithub.presentation.component

import android.content.Context
import android.text.Editable
import android.text.TextUtils.substring
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ComponentEdtInputBirthBinding
import com.proteam.fithub.databinding.ComponentEdtInputPhoneNumberBinding

class ComponentEdtBirthDay(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private lateinit var binding: ComponentEdtInputBirthBinding
    private var isErrorContains: Boolean = false

    private var _doneState = MutableLiveData<Boolean>()
    val doneState: LiveData<Boolean> = _doneState

    init {
        initBinding()
        initUi()
    }

    fun getAttr(type: Boolean) {
        isErrorContains = type
    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.component_edt_input_birth,
            this,
            false
        )
        binding.layout = this
        addView(binding.root)
    }

    private fun initUi() {
        binding.componentEdtInputBirthEdtBirthday.setOnFocusChangeListener { view, b ->
            binding.componentEdtInputBirthCardContainer.strokeColor = strokeWhenNotError(b)
            binding.componentEdtInputBirthTvTitle.setTextColor(normalStroke)
        }

        binding.componentEdtInputBirthEdtBirthday.addTextChangedListener {
            checkWhenError(it)
        }
    }

    private fun checkWhenError(value : Editable?) {
        if(value?.length == 11 && regexError()) {
            setDoneState()
        } else if(value?.length == 11 && !regexError()){
            setError()
        } else {
            resetStroke()
        }
    }

    private fun resetStroke() {
        binding.componentEdtInputBirthCardContainer.strokeColor = doneStroke
        binding.componentEdtInputBirthTvTitle.setTextColor(normalStroke)
        binding.componentEdtInputPhoneNumberTvAdditional.visibility = View.GONE
        binding.componentEdtInputBirthBtnError.visibility = View.GONE
    }

    private fun setError() {
        binding.componentEdtInputPhoneNumberTvAdditional.apply {
            visibility = View.VISIBLE
            setTextColor(errorStroke)

            //여기 수정
            text = "휴대폰 번호가 올바르지 않습니다"
        }
        binding.componentEdtInputBirthTvTitle.setTextColor(errorStroke)
        binding.componentEdtInputBirthCardContainer.strokeColor = errorStroke

        binding.componentEdtInputBirthBtnError.visibility = View.VISIBLE
    }

    private fun setDoneState() {
        resetStroke()
        _doneState.value = if(isErrorContains) regexError() else checkLength()
    }

    fun getUserInputContent(): String =
        binding.componentEdtInputBirthEdtBirthday.text.toString()

    private fun strokeWhenNotError(b: Boolean): Int = if (b) doneStroke else normalStroke

    private fun regexError() : Boolean =  checkLength()

    private var normalStroke = context.resources.getColor(R.color.gray_400, null)
    private var doneStroke = context.resources.getColor(R.color.gray_600, null)
    private var errorStroke = context.resources.getColor(R.color.color_error, null)

    private fun checkLength() : Boolean = binding.componentEdtInputBirthEdtBirthday.text.length == 6 && binding.componentEdtInputBirthEdtGender.text.isNotEmpty()
}