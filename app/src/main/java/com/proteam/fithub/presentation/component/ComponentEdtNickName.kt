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
import com.proteam.fithub.databinding.ComponentEdtInputNicknameBinding
import com.proteam.fithub.databinding.ComponentEdtInputPasswordBinding
import java.util.regex.Pattern

class ComponentEdtNickName(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private lateinit var binding: ComponentEdtInputNicknameBinding
    private var isErrorContains: Boolean = false

    private val nickNamePattern = """^(?=.*[가-힣])(?=.*[a-zA-Z])(?=\S+$).*$"""


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

    fun getAttr(type: Boolean) {
        isErrorContains = type
    }

    private fun initBinding() {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.component_edt_input_nickname,
            this,
            false
        )
        binding.layout = this
        addView(binding.root)
    }

    private fun initUi() {
        binding.componentEdtInputNicknameEdtContent.setOnFocusChangeListener { view, b ->
            binding.componentEdtInputNicknameCardContainer.strokeColor =
                if (isErrorContains) strokeWhenError(b) else strokeWhenNotError(b)
            binding.componentEdtInputNicknameTvTitle.setTextColor(strokeWhenError(b))
            //setDeleteVisibility(b)
        }

        binding.componentEdtInputNicknameTvAdditional.visibility = if(isErrorContains) View.VISIBLE else View.GONE

        binding.componentEdtInputNicknameEdtContent.addTextChangedListener {
            if (it.isNullOrEmpty()) {
                resetStroke()
            } else if (isErrorContains) {
                checkRegex(it)
            } else {
                setDoneState()
            }
            //setDeleteVisibility(binding.componentEdtInputPasswordEdtContent.hasFocus())
        }
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
        return if(!value.isNullOrEmpty()) Pattern.matches(nickNamePattern, value.toString()) else false
    }

    private fun checkLengthMatches(value : Editable?) : Boolean {
        return value?.length in 1..10
    }

    private fun setError(errorType : String) {
        binding.componentEdtInputNicknameTvAdditional.apply {
            visibility = View.VISIBLE
            setTextColor(errorStroke)
            text = returnErrorText(errorType)
        }
        binding.componentEdtInputNicknameTvTitle.setTextColor(errorStroke)
        binding.componentEdtInputNicknameCardContainer.strokeColor = errorStroke

        binding.componentEdtInputNicknameBtnErrorOrSuccess.apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_edt_error)
        }
    }

    private fun setSuccess(successType : String) {
        binding.componentEdtInputNicknameTvAdditional.apply {
            visibility = View.VISIBLE
            setTextColor(successStroke)
            text = returnSuccessText(successType)
        }
        binding.componentEdtInputNicknameTvTitle.setTextColor(successStroke)
        binding.componentEdtInputNicknameCardContainer.strokeColor = successStroke

        binding.componentEdtInputNicknameBtnErrorOrSuccess.apply {
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
        binding.componentEdtInputNicknameCardContainer.strokeColor = doneStroke
        binding.componentEdtInputNicknameTvTitle.setTextColor(normalStroke)
        binding.componentEdtInputNicknameBtnErrorOrSuccess.visibility = View.GONE


        binding.componentEdtInputNicknameTvAdditional.apply {
            setTextColor(doneStroke)
            if (isErrorContains) {
                View.VISIBLE
                this.text = "한글 혹은 영문을 포함하여 1~10자로 입력해주세요"
            } else {
                View.GONE
            }
        }
    }

    private fun setDoneState() {
        _doneState.value = binding.componentEdtInputNicknameEdtContent.text.isNotEmpty()
    }

    /*private fun setDeleteVisibility(focus : Boolean) {
        binding.componentEdtInputPasswordBtnClear.visibility =
            if (binding.componentEdtInputPasswordEdtContent.text.isEmpty() || !focus) View.GONE else View.VISIBLE
    }

    fun onDeleteClicked() {
        binding.componentEdtInputPasswordEdtContent.setText("")
    }*/

    fun getUserInputContent(): String = binding.componentEdtInputNicknameEdtContent.text.toString()

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