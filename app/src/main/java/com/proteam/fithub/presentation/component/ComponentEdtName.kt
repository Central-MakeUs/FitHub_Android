package com.proteam.fithub.presentation.component

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ComponentEdtInputNameBinding
import java.util.regex.Pattern

class ComponentEdtName(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private lateinit var binding: ComponentEdtInputNameBinding
    private var status = Status.NON_FOCUSED_NO_INPUT

    private val errorColor = resources.getColor(R.color.color_error, null)
    private val successColor = resources.getColor(R.color.color_info, null)
    private val nonFocusNonInputColor = resources.getColor(R.color.icon_enabled, null)
    private val nonFocusInputColor = resources.getColor(R.color.icon_disabled, null)
    private val focusColor = resources.getColor(R.color.icon_sub, null)

    private var isErrorEnable: Boolean = false

    private var _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> = _isFinished

    private val namePattern = """^[가-힣a-zA-Z]*$"""
    init {
        initBinding()
        initUi()
    }

    fun setErrorEnable(state: Boolean) {
        isErrorEnable = state
    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.component_edt_input_name,
            this,
            false
        )
        binding.layout = this
        addView(binding.root)
    }

    private fun initUi() {
        setFocusListener()
        setTextChangeListener()
    }

    private fun setFocusListener() {
        binding.componentEdtInputNameEdtContent.setOnFocusChangeListener { _, focus ->
            checkStatus(focus)
            updateUiWithState()
        }
    }

    private fun checkStatusWhenNotFocused(): Status {
        return if (binding.componentEdtInputNameEdtContent.text.isNullOrEmpty()) Status.NON_FOCUSED_NO_INPUT else Status.NON_FOCUSED_INPUT
    }

    private fun setTextChangeListener() {
        binding.componentEdtInputNameEdtContent.addTextChangedListener {
            if(it.isNullOrEmpty()) {
                setFocusedState()
            } else if(isErrorEnable) {
                checkWhenError(it)
            }
            updateUiWithState()
            checkFinished()
        }
    }

    private fun checkWhenError(value : Editable?) {
        if(!checkRegexError(value)) {
            setErrorState()
        } else {
            setFinishState()
        }
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

    private fun updateUiWithState() {
        binding.componentEdtInputNameCardContainer.strokeColor = setStrokeColor()
        binding.componentEdtInputNameTvTitle.setTextColor(setTitleTextColor())
        setNoticeTextStatus()
        setAdditionalIconStatus()
        setDeleteIconStatus(binding.componentEdtInputNameEdtContent.hasFocus())
    }

    private fun setNoticeTextStatus() {
        binding.componentEdtInputNameTvAdditional.apply {
            visibility = if(status == Status.ERROR) VISIBLE else GONE
            text = resources.getString(R.string.error_password_regex)
            if(status == Status.ERROR) {
                setTextColor(errorColor)
            }
        }
    }

    private fun setAdditionalIconStatus() {
        binding.componentEdtInputNameBtnError.apply {
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
        binding.componentEdtInputNameBtnClear.visibility = if (binding.componentEdtInputNameEdtContent.text.isEmpty() || !focus) View.GONE else View.VISIBLE
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


    fun onDeleteClicked() {
        binding.componentEdtInputNameEdtContent.setText("")
    }

    fun getUserInputContent(): String =
        binding.componentEdtInputNameEdtContent.text.toString()

    /** Regex **/

    private fun checkRegexError(value : Editable?) : Boolean {
        return if(value?.isEmpty() == true) {
            false
        } else Pattern.matches(namePattern, value.toString())
    }

}