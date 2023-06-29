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
import com.proteam.fithub.databinding.ComponentEdtInputPhoneNumberBinding

class ComponentEdtPhoneNumber(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private lateinit var binding: ComponentEdtInputPhoneNumberBinding
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
            R.layout.component_edt_input_phone_number,
            this,
            false
        )
        binding.layout = this
        addView(binding.root)
    }

    private fun initUi() {
        binding.componentEdtInputPhoneNumberEdtContent.setOnFocusChangeListener { view, b ->
            binding.componentEdtInputPhoneNumberCardContainer.strokeColor = strokeWhenNotError(b)
            binding.componentEdtInputPhoneNumberTvTitle.setTextColor(normalStroke)
        }

        binding.componentEdtInputPhoneNumberEdtContent.addTextChangedListener {
            if(isErrorContains) {
                checkWhenError(it)
            } else {
                checkWhenNotError()
            }
            setDeleteVisibility()
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

    private fun checkWhenNotError() {
        setDoneState()
    }

    private fun resetStroke() {
        binding.componentEdtInputPhoneNumberCardContainer.strokeColor = doneStroke
        binding.componentEdtInputPhoneNumberTvTitle.setTextColor(normalStroke)
        binding.componentEdtInputPhoneNumberTvAdditional.visibility = View.GONE
        binding.componentEdtInputPhoneNumberBtnError.visibility = View.GONE
    }

    private fun setError() {
        binding.componentEdtInputPhoneNumberTvAdditional.apply {
            visibility = View.VISIBLE
            setTextColor(errorStroke)
            text = "휴대폰 번호가 올바르지 않습니다"
        }
        binding.componentEdtInputPhoneNumberTvTitle.setTextColor(errorStroke)
        binding.componentEdtInputPhoneNumberCardContainer.strokeColor = errorStroke

        binding.componentEdtInputPhoneNumberBtnError.visibility = View.VISIBLE
    }

    private fun setDoneState() {
        resetStroke()
        _doneState.value = if(isErrorContains) regexError() else checkLength()
    }

    private fun setDeleteVisibility() {
        binding.componentEdtInputPhoneNumberBtnClear.visibility =
            if (binding.componentEdtInputPhoneNumberEdtContent.text.isEmpty()) View.GONE else View.VISIBLE
    }

    fun onDeleteClicked() {
        binding.componentEdtInputPhoneNumberEdtContent.setText("")
    }

    fun getUserInputContent(): String =
        binding.componentEdtInputPhoneNumberEdtContent.text.toString()

    private fun strokeWhenNotError(b: Boolean): Int = if (b) doneStroke else normalStroke

    private fun regexError() : Boolean =  checkLength() && checkStart3Words()

    private var normalStroke = context.resources.getColor(R.color.gray_400, null)
    private var doneStroke = context.resources.getColor(R.color.gray_600, null)
    private var errorStroke = context.resources.getColor(R.color.color_error, null)

    private fun checkLength() : Boolean = binding.componentEdtInputPhoneNumberEdtContent.text.length == 11
    private fun checkStart3Words() : Boolean = if(binding.componentEdtInputPhoneNumberEdtContent.text.length >= 3) binding.componentEdtInputPhoneNumberEdtContent.text.substring(0, 3) == "010" else false
}