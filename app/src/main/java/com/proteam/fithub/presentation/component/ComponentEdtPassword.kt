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
import com.proteam.fithub.databinding.ComponentEdtInputPasswordBinding
import java.util.regex.Pattern

class ComponentEdtPassword(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private lateinit var binding: ComponentEdtInputPasswordBinding
    private var isErrorContains: Boolean = false
    private var isForCheck: Boolean = false

    private val pwPattern = """^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^+\-=])(?=\S+$).*$"""

    /** 재확인일때 비교하기 위한 비밀번호 **/
    private var password: String = ""

    private var _doneState = MutableLiveData<Boolean>()
    val doneState: LiveData<Boolean> = _doneState

    private var _checkFocus = MutableLiveData<Boolean>()
    val checkFocus : LiveData<Boolean> = _checkFocus

    /** E / S / N **/
    /** Error / Success / Null **/
    private var status: String = ""

    init {
        initBinding()
        initUi()
    }

    fun getAttr(type: Boolean, forCheck: Boolean) {
        isErrorContains = type
        isForCheck = forCheck

        if(isForCheck) {
            setTextForCheck()
        }
    }

    fun getPassword(password: String) {
        this.password = password
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

    private fun initUi() {
        binding.componentEdtInputPasswordEdtContent.setOnFocusChangeListener { view, b ->
            binding.componentEdtInputPasswordCardContainer.strokeColor =
                if (isErrorContains) strokeWhenError(b) else strokeWhenNotError(b)
            binding.componentEdtInputPasswordTvTitle.setTextColor(strokeWhenError(b))
            setDeleteVisibility(b)

            if(isForCheck) _checkFocus.value = b
        }

        binding.componentEdtInputPasswordTvAdditional.visibility = if(isErrorContains) View.VISIBLE else View.GONE

        binding.componentEdtInputPasswordEdtContent.addTextChangedListener {
            if (it.isNullOrEmpty()) {
                resetStroke()
            } else if (isErrorContains && !isForCheck) {
                checkRegex(it)
            } else if (isForCheck) {
                checkSame(it)
            } else {
                setDoneState()
            }
            setDeleteVisibility(binding.componentEdtInputPasswordEdtContent.hasFocus())
        }
    }

    private fun setTextForCheck() {
        binding.componentEdtInputPasswordTvTitle.text = "비밀번호 확인"
        binding.componentEdtInputPasswordEdtContent.hint = "비밀번호 재입력"
    }

    private fun checkRegex(value: Editable?) {
        if(!checkPatternMatches(value)) {
            setError("regex")
            status = "E"
        } else if (!checkLengthMatches(value)) {
            setError("length")
            status = "E"
        } else {
            setSuccess("original")
            status = "S"
            setDoneState()
        }
    }

    private fun checkPatternMatches(value : Editable?) : Boolean{
        return if(!value.isNullOrEmpty()) Pattern.matches(pwPattern, value.toString()) else false
    }

    private fun checkLengthMatches(value : Editable?) : Boolean {
        return value?.length in 8..16
    }

    private fun checkSame(value: Editable?) {
        if (!value.isNullOrEmpty() && password == value.toString()) {
            setSuccess("copy")
            status = "S"
            setDoneState()
        } else {
            setError("same")
            status = "F"
        }
    }

    private fun setError(errorType : String) {
        binding.componentEdtInputPasswordTvAdditional.apply {
            visibility = View.VISIBLE
            setTextColor(errorStroke)
            text = returnErrorText(errorType)
        }
        binding.componentEdtInputPasswordTvTitle.setTextColor(errorStroke)
        binding.componentEdtInputPasswordCardContainer.strokeColor = errorStroke

        binding.componentEdtInputPasswordBtnErrorOrSuccess.apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_edt_error)
        }
    }

    private fun setSuccess(successType : String) {
        binding.componentEdtInputPasswordTvAdditional.apply {
            visibility = View.VISIBLE
            setTextColor(successStroke)
            text = returnSuccessText(successType)
        }
        binding.componentEdtInputPasswordTvTitle.setTextColor(successStroke)
        binding.componentEdtInputPasswordCardContainer.strokeColor = successStroke

        binding.componentEdtInputPasswordBtnErrorOrSuccess.apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_edt_success)
        }
    }

    private fun returnErrorText(errorType : String) : String {
        return when(errorType) {
            "regex" -> "특수문자, 숫자, 영문 조합으로 입력하세요"
            "length" -> "비밀번호는 8~16자 이내로 입력하세요"
            else -> "비밀번호가 일치하지 않습니다"
        }
    }

    private fun returnSuccessText(successType : String) : String {
        return when(successType) {
            "original" -> "사용할 수 있는 비밀번호입니다"
            else -> "비밀번호가 일치합니다"
        }
    }

    private fun resetStroke() {
        binding.componentEdtInputPasswordCardContainer.strokeColor = doneStroke
        binding.componentEdtInputPasswordTvTitle.setTextColor(normalStroke)
        binding.componentEdtInputPasswordBtnErrorOrSuccess.visibility = View.GONE


        binding.componentEdtInputPasswordTvAdditional.apply {
            setTextColor(doneStroke)
            if (isErrorContains) {
                View.VISIBLE
                this.text = "영어, 숫자, 특수문자를 조합하여 8~16자로 입력해주세요"
            } else {
                View.GONE
            }
        }
    }

    private fun setDoneState() {
        _doneState.value = binding.componentEdtInputPasswordEdtContent.text.isNotEmpty()
    }

    private fun setDeleteVisibility(focus : Boolean) {
        binding.componentEdtInputPasswordBtnClear.visibility =
            if (binding.componentEdtInputPasswordEdtContent.text.isEmpty() || !focus) View.GONE else View.VISIBLE
    }

    fun onDeleteClicked() {
        binding.componentEdtInputPasswordEdtContent.setText("")
    }

    fun getUserInputContent(): String = binding.componentEdtInputPasswordEdtContent.text.toString()

    private fun strokeWhenNotError(b: Boolean): Int = if (b) doneStroke else normalStroke
    private fun strokeWhenError(b: Boolean): Int {
        return if (status == "S") successStroke
        else if (status == "E") errorStroke
        else if (status == "N" && b) doneStroke
        else normalStroke
    }

    private var normalStroke = context.resources.getColor(R.color.gray_400, null)
    private var doneStroke = context.resources.getColor(R.color.gray_600, null)
    private var errorStroke = context.resources.getColor(R.color.color_error, null)
    private var successStroke = context.resources.getColor(R.color.color_info, null)
}