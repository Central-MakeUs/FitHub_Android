package com.proteam.fithub.presentation.component

import android.content.Context
import android.text.Editable
import android.text.TextUtils.substring
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ComponentEdtInputPhoneNumberBinding

class ComponentEdtPhoneNumber(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private lateinit var binding: ComponentEdtInputPhoneNumberBinding
    /** true : 에러 / false : 일반 **/
    private var state : Boolean = false

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
            binding.componentEdtInputPhoneNumberTvTitle.setTextColor(strokeWhenNotError(b))
            setDeleteVisibility(b)
        }

        binding.componentEdtInputPhoneNumberEdtContent.addTextChangedListener {
            if(isErrorContains) {
                checkWhenError()
            } else {
                checkWhenNotError()
            }
            setDeleteVisibility(binding.componentEdtInputPhoneNumberEdtContent.hasFocus())
        }
    }

    private fun checkWhenError() {
        state = if(regexError()) {
            setDoneState()
            false
        } else {
            setError()
            true
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
        _doneState.value = if(isErrorContains) regexError() && checkLength() else checkLength()
    }

    private fun setDeleteVisibility(focus : Boolean) {
        binding.componentEdtInputPhoneNumberBtnClear.visibility =
            if (binding.componentEdtInputPhoneNumberEdtContent.text.isEmpty() || !focus) View.GONE else View.VISIBLE
    }

    fun onDeleteClicked() {
        binding.componentEdtInputPhoneNumberEdtContent.setText("")
    }

    fun getUserInputContent(): String =
        binding.componentEdtInputPhoneNumberEdtContent.text.toString()

    private fun strokeWhenNotError(b: Boolean): Int {
        return if (state) errorStroke
        else if (b) doneStroke
        else normalStroke
    }

    private fun regexError() : Boolean =  if(!checkLength()) true else  checkLength() && checkStart3Words() == true

    private var normalStroke = context.resources.getColor(R.color.gray_400, null)
    private var doneStroke = context.resources.getColor(R.color.gray_600, null)
    private var errorStroke = context.resources.getColor(R.color.color_error, null)

    private fun checkLength() : Boolean = binding.componentEdtInputPhoneNumberEdtContent.text.length == 11
    private fun checkStart3Words() : Boolean? = binding.componentEdtInputPhoneNumberEdtContent.text?.let { if(it.length >= 3) it.substring(0, 3) == "010" else false}

    fun phoneNumberEdt() : EditText = binding.componentEdtInputPhoneNumberEdtContent
}