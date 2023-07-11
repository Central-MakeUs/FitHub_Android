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
import com.proteam.fithub.databinding.ComponentEdtInputPasswordBinding
import com.proteam.fithub.databinding.ComponentEdtInputPhoneNumberBinding
import java.util.regex.Pattern

class ComponentPassword(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private lateinit var binding: ComponentEdtInputPasswordBinding
    private var status = Status.NON_FOCUSED_NO_INPUT

    private val errorColor = resources.getColor(R.color.color_error, null)
    private val successColor = resources.getColor(R.color.color_info, null)
    private val nonFocusNonInputColor = resources.getColor(R.color.icon_enabled, null)
    private val nonFocusInputColor = resources.getColor(R.color.icon_disabled, null)
    private val focusColor = resources.getColor(R.color.icon_sub, null)
    private val defaultAdditionalColor = resources.getColor(R.color.text_info, null)

    private var isErrorEnable = false
    private var isForCheck = false

    private val _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> = _isFinished

    private lateinit var checkPassword : String
    private var errorType : String = ""

    private val pwPattern = """^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^+\-=])(?=\S+$).*$"""

    var userInputPassword = MutableLiveData<String>().apply { value = "" }

    init {
        initBinding()
        initUI()
    }

    fun setErrorEnable(state: Boolean, forCheck: Boolean) {
        isErrorEnable = state
        isForCheck = forCheck
    }

    fun setPasswordForCheck(password : String) {
        checkPassword = password
    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.component_edt_input_password,
            this,
            false
        )
        binding.layout = this
        addView(binding.root)
    }

    private fun initUI() {
        setFocusListener()
        setTextChangeListener()
    }

    private fun setFocusListener() {
        binding.componentEdtInputPasswordEdtContent.setOnFocusChangeListener { _, focus ->
            checkStatus(focus)
            updateUiWithState(errorType)
        }
    }

    private fun checkStatusWhenNotFocused(): Status {
        return if (binding.componentEdtInputPasswordEdtContent.text.isNullOrEmpty()) Status.NON_FOCUSED_NO_INPUT else Status.NON_FOCUSED_INPUT
    }

    private fun setTextChangeListener() {
        binding.componentEdtInputPasswordEdtContent.addTextChangedListener {
            if(it.isNullOrEmpty()) {
                setFocusedState()
            }
            if (isErrorEnable) {
                errorType = if (isForCheck) {
                    checkIsSameWithPassword()
                } else {
                    checkWhenError(it)
                }
            } else {
                checkWhenNotError()
            }
            updateUiWithState(errorType)
            checkFinished()
        }
    }

    private fun checkWhenError(value: Editable?) : String{
        return if(!checkLengthError(value)) {
            setErrorState()
            "LENGTH"
        } else if(!checkRegexError(value)) {
            setErrorState()
            "REGEX"
        } else {
            setSuccessState()
            "SUCCESS"
        }
    }

    private fun checkWhenNotError() {
        setFinishState()
    }

    /** Ui **/

    private fun setStrokeColor(): Int {
        return when (status) {
            Status.SUCCESS -> successColor
            Status.ERROR -> errorColor
            Status.FOCUSED, Status.FINISHED -> focusColor
            Status.NON_FOCUSED_INPUT -> nonFocusInputColor
            else -> nonFocusNonInputColor
        }
    }

    private fun setTitleTextColor(): Int {
        return when (status) {
            Status.SUCCESS -> successColor
            Status.ERROR -> errorColor
            else -> nonFocusInputColor
        }
    }

    private fun updateUiWithState(errorType : String) {
        binding.componentEdtInputPasswordCardContainer.strokeColor = setStrokeColor()
        binding.componentEdtInputPasswordTvTitle.setTextColor(setTitleTextColor())
        setNoticeTextStatus(errorType)
        setAdditionalIconStatus()
        setDeleteIconStatus(binding.componentEdtInputPasswordEdtContent.hasFocus())
    }

    private fun setNoticeTextStatus(errorType : String) {
        binding.componentEdtInputPasswordTvAdditional.apply {
            visibility = if(isErrorEnable || (isForCheck && (status == Status.ERROR || status == Status.SUCCESS))) VISIBLE else GONE
            text =
                when (errorType) {
                    "LENGTH" -> resources.getString(R.string.error_password_length)
                    "REGEX" -> resources.getString(R.string.error_password_regex)
                    "NOT_MATCH" -> resources.getString(R.string.error_password_not_match)
                    "SUCCESS" -> resources.getString(R.string.success_password)
                    "MATCH" -> resources.getString(R.string.success_password_match)
                    else -> if(!isForCheck && isErrorEnable) resources.getString(R.string.sign_up_with_phone_additional_password) else ""
                }

            setTextColor(if(status == Status.ERROR) errorColor else if (status == Status.SUCCESS) successColor else defaultAdditionalColor)
        }
    }

    private fun setAdditionalIconStatus() {
        binding.componentEdtInputPasswordBtnErrorOrSuccess.apply {
            setImageResource(
                when (status) {
                    Status.ERROR -> R.drawable.ic_edt_error
                    else -> R.drawable.ic_edt_success
                }
            )

            visibility =
                if (isErrorEnable && (status == Status.ERROR || status == Status.SUCCESS)) VISIBLE else GONE
        }
    }

    private fun setDeleteIconStatus(focus: Boolean) {
        binding.componentEdtInputPasswordBtnClear.visibility = if (focus) VISIBLE else GONE
    }

    /** Set Status **/

    private fun checkStatus(focus: Boolean) {
        if (status == Status.ERROR || status == Status.SUCCESS) {
            return
        } else {
            status = if (focus) Status.FOCUSED else checkStatusWhenNotFocused()
        }
    }

    private fun setSuccessState() {
        status = Status.SUCCESS
    }

    private fun setErrorState() {
        status = Status.ERROR
    }

    private fun setFocusedState() {
        status = Status.FOCUSED
    }

    private fun setFinishState() {
        status = Status.FINISHED
    }

    private fun checkFinished() {
        _isFinished.value = status == Status.FINISHED || status == Status.SUCCESS
    }

    /** Regex **/
    fun checkWhenOriginalChanged() {
        errorType = checkIsSameWithPassword()
        updateUiWithState(errorType)
    }

    private fun checkIsSameWithPassword() : String{
        return if(userInputPassword.value.isNullOrEmpty()) {
            return ""
        } else {
            if (userInputPassword.value.toString() == checkPassword) {
                status = Status.SUCCESS
                "MATCH"
            } else {
                status = Status.ERROR
                "NOT_MATCH"
            }
        }
    }

    private fun checkRegexError(value: Editable?): Boolean {
        return if (value?.isEmpty() == true) {
            false
        } else Pattern.matches(pwPattern, value.toString())
    }

    private fun checkLengthError(value: Editable?): Boolean {
        return value?.length in 8..16
    }

    /** Etc **/

    fun returnEdtComponent(): EditText = binding.componentEdtInputPasswordEdtContent

    fun returnUserInputContent(): String =
        binding.componentEdtInputPasswordEdtContent.text.toString()

    fun onDeleteClicked() {
        binding.componentEdtInputPasswordEdtContent.setText("")
    }
}