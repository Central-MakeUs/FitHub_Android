package com.proteam.fithub.presentation.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
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

    private var status = ComponentStatus.NONE

    private var _isComplete = MutableLiveData<Boolean>()
    val isComplete: LiveData<Boolean> = _isComplete

    private var normalStroke = context.resources.getColor(R.color.gray_400, null)
    private var doneStroke = context.resources.getColor(R.color.gray_600, null)
    private var errorStroke = context.resources.getColor(R.color.color_error, null)


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
        binding.componentEdtInputPhoneNumberEdtContent.setOnFocusChangeListener { view, focus ->
            setStrokeColor(focus)
            setTextColor()
            setDeleteVisibility(focus)
        }

        binding.componentEdtInputPhoneNumberEdtContent.addTextChangedListener {
            if(isErrorContains && it?.length == 11) {
                checkWhenError()
            } else {
                checkWhenNotError()
            }
            setDeleteVisibility(binding.componentEdtInputPhoneNumberEdtContent.hasFocus())
        }
    }

    private fun setStrokeColor(focus : Boolean?) {
        binding.componentEdtInputPhoneNumberCardContainer.strokeColor = if (status == ComponentStatus.ERROR) {
            errorStroke
        } else if(focus == true) {
            doneStroke
        } else {
            normalStroke
        }
    }

    private fun setTextColor() {
        binding.componentEdtInputPhoneNumberTvTitle.setTextColor(when(status) {
            ComponentStatus.ERROR -> errorStroke
            else -> normalStroke
        })
    }

    private fun checkWhenError() {
        status = if(regexError()) {
            ComponentStatus.DONE
        } else {
            ComponentStatus.ERROR
        }
        updateUiByStatus()
        checkIsComplete()
    }

    private fun checkWhenNotError() {
        status = if(checkLength()) {
            ComponentStatus.DONE
        } else {
            ComponentStatus.NONE
        }
        updateUiByStatus()
        checkIsComplete()
    }

    private fun updateUiByStatus() {
        when(status) {
            ComponentStatus.ERROR -> setErrorState()
            else -> setNormalState()
        }
    }

    /** ERROR **/
    private fun setErrorState() {
        binding.componentEdtInputPhoneNumberTvAdditional.apply {
            visibility = View.VISIBLE
            setTextColor(errorStroke)
            text = "휴대폰 번호가 올바르지 않습니다"
        }
        binding.componentEdtInputPhoneNumberBtnError.visibility = View.VISIBLE
        setStrokeColor(null)
        setTextColor()
    }


    /** NORMAL **/
    private fun setNormalState() {
        setStrokeColor(true)
        setTextColor()
        binding.componentEdtInputPhoneNumberTvAdditional.visibility = View.GONE
        binding.componentEdtInputPhoneNumberBtnError.visibility = View.GONE
    }


    /** DONE STATUS **/
    private fun checkIsComplete() {
        _isComplete.value = status == ComponentStatus.DONE
    }


    /** DELETE BUTTON **/
    private fun setDeleteVisibility(focus : Boolean) {
        binding.componentEdtInputPhoneNumberBtnClear.visibility =
            if (binding.componentEdtInputPhoneNumberEdtContent.text.isEmpty() || !focus) View.GONE else View.VISIBLE
    }

    fun onDeleteClicked() {
        binding.componentEdtInputPhoneNumberEdtContent.setText("")
    }


    /** REGEX **/
    private fun regexError() : Boolean = checkLength() && checkStart3Words()

    private fun checkLength() : Boolean = binding.componentEdtInputPhoneNumberEdtContent.text.length == 11
    private fun checkStart3Words() : Boolean = binding.componentEdtInputPhoneNumberEdtContent.text.let { if(it.length >= 3) it.substring(0, 3) == "010" else false}


    /** OBJECT **/
    fun getUserInputContent(): String =
        binding.componentEdtInputPhoneNumberEdtContent.text.toString()

    fun phoneNumberEdt() : EditText = binding.componentEdtInputPhoneNumberEdtContent
}