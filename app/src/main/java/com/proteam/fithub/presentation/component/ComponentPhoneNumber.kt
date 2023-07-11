package com.proteam.fithub.presentation.component

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ComponentEdtInputPhoneNumberBinding

class ComponentPhoneNumber(context : Context, attrs : AttributeSet) : ConstraintLayout(context, attrs) {
    private lateinit var binding : ComponentEdtInputPhoneNumberBinding
    private var status = Status.NON_FOCUSED_NO_INPUT

    private val errorColor = resources.getColor(R.color.color_error, null)
    private val successColor = resources.getColor(R.color.color_info, null)
    private val nonFocusNonInputColor = resources.getColor(R.color.icon_enabled, null)
    private val nonFocusInputColor = resources.getColor(R.color.icon_disabled, null)
    private val focusColor = resources.getColor(R.color.icon_sub, null)

    private var isErrorEnable = false

    private val _isFinished = MutableLiveData<Boolean>()
    val isFinished : LiveData<Boolean> = _isFinished

    init {
        initBinding()
        initUI()
    }

    fun setErrorEnable(state : Boolean) {
        isErrorEnable = state
    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.component_edt_input_phone_number, this, false)
        binding.layout = this
        addView(binding.root)
    }

    private fun initUI() {
        setFocusListener()
        setTextChangeListener()
    }

    private fun setFocusListener() {
        binding.componentEdtInputPhoneNumberEdtContent.setOnFocusChangeListener { _, focus ->
            checkStatus(focus)
            updateUiWithState()
        }
    }

    private fun checkStatusWhenNotFocused() : Status{
        return if(binding.componentEdtInputPhoneNumberEdtContent.text.isNullOrEmpty()) Status.NON_FOCUSED_NO_INPUT else Status.NON_FOCUSED_INPUT
    }

    private fun setTextChangeListener() {
        binding.componentEdtInputPhoneNumberEdtContent.addTextChangedListener {
            if(isErrorEnable) {
                if(it?.length == 11) {
                    checkWhenError(it)
                } else {
                    setFocusedState()
                }
            } else {
                checkWhenNotError(it)
            }
            updateUiWithState()
            checkFinished()
        }
    }

    private fun checkWhenError(value : Editable?) {
        if(checkLengthError(value) && checkRegexError(value)) {
            setFinishState()
        } else {
            setErrorState()
        }
    }

    private fun checkWhenNotError(value : Editable?) {
        if(checkLengthError(value)) {
            setFinishState()
        }
    }

    /** Ui **/

    private fun setStrokeColor() : Int {
        return when(status) {
            Status.SUCCESS -> successColor
            Status.ERROR -> errorColor
            Status.FOCUSED, Status.FINISHED -> focusColor
            Status.NON_FOCUSED_INPUT -> nonFocusInputColor
            else -> nonFocusNonInputColor
        }
    }

    private fun setTitleTextColor() : Int {
        return when(status) {
            Status.SUCCESS -> successColor
            Status.ERROR -> errorColor
            else -> nonFocusInputColor
        }
    }

    private fun updateUiWithState() {
        binding.componentEdtInputPhoneNumberCardContainer.strokeColor = setStrokeColor()
        binding.componentEdtInputPhoneNumberTvTitle.setTextColor(setTitleTextColor())
        setNoticeTextStatus()
        setAdditionalIconStatus()
        setDeleteIconStatus(binding.componentEdtInputPhoneNumberEdtContent.hasFocus())
    }

    private fun setNoticeTextStatus() {
        binding.componentEdtInputPhoneNumberTvAdditional.visibility = if(isErrorEnable && status == Status.ERROR) VISIBLE else GONE
        binding.componentEdtInputPhoneNumberTvAdditional.text = resources.getString(R.string.error_phone_number)
    }

    private fun setAdditionalIconStatus() {
        binding.componentEdtInputPhoneNumberBtnError.apply {
            setImageResource(when(status) {
                Status.ERROR -> R.drawable.ic_edt_error
                else -> R.drawable.ic_edt_success
            })

            visibility = if(isErrorEnable && (status == Status.ERROR || status == Status.SUCCESS)) VISIBLE else GONE
        }
    }

    private fun setDeleteIconStatus(focus : Boolean) {
        binding.componentEdtInputPhoneNumberBtnClear.visibility = if(focus) VISIBLE else GONE
    }

    /** Set Status **/

    private fun checkStatus(focus : Boolean) {
        if(status == Status.ERROR || status == Status.SUCCESS) {
            return
        } else {
            status = if(focus) Status.FOCUSED else checkStatusWhenNotFocused()
        }
    }

    private fun setFocusedState() {
        status = Status.FOCUSED
    }

    private fun setErrorState() {
        status = Status.ERROR
    }

    private fun setFinishState() {
        status = Status.FINISHED
    }

    private fun checkFinished() {
        _isFinished.value = status == Status.FINISHED
    }

    /** Regex **/

    private fun checkRegexError(value : Editable?) : Boolean {
        return if(value?.isEmpty() == true) {
            false
        } else value!!.length > 3 && value.substring(0,3) =="010"
    }

    private fun checkLengthError(value : Editable?) : Boolean {
        return value?.length == 11
    }

    /** Etc **/

    fun returnEdtComponent() : EditText = binding.componentEdtInputPhoneNumberEdtContent

    fun returnUserInputContent() : String = binding.componentEdtInputPhoneNumberEdtContent.text.toString()

    fun onDeleteClicked() {
        binding.componentEdtInputPhoneNumberEdtContent.setText("")
    }
}