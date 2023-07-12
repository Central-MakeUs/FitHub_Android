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
import java.util.regex.Pattern

class ComponentEdtNickName(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private lateinit var binding: ComponentEdtInputNicknameBinding
    private var status = Status.NON_FOCUSED_INPUT

    private var isErrorEnable: Boolean = false

    private val errorColor = resources.getColor(R.color.color_error, null)
    private val successColor = resources.getColor(R.color.color_info, null)
    private val nonFocusNonInputColor = resources.getColor(R.color.icon_enabled, null)
    private val nonFocusInputColor = resources.getColor(R.color.icon_disabled, null)
    private val focusColor = resources.getColor(R.color.icon_sub, null)

    private val nickNamePattern = """^[가-힣a-zA-Z]*$"""

    private var _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> = _isFinished

    private var errorType : String = ""

    private var _checkSameClicked = MutableLiveData<Boolean>()
    var checkSameClicked : LiveData<Boolean> = _checkSameClicked

    init {
        initBinding()
        initUi()
    }

    fun setErrorEnable(type: Boolean) {
        isErrorEnable = type
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
        setFocusListener()
        setTextChangeListener()
    }

    private fun setFocusListener() {
        binding.componentEdtInputNicknameEdtContent.setOnFocusChangeListener { _, focus ->
            checkStatus(focus)
            updateUiWithState()
        }
    }

    private fun setTextChangeListener() {
        binding.componentEdtInputNicknameEdtContent.addTextChangedListener {
            if (it.isNullOrEmpty()) {
                setFocusedState()
            } else if (isErrorEnable) {
                errorType = checkRegex(it)
            }
            updateUiWithState()
            checkFinished()
        }
    }

    /** Ui **/

    private fun setStrokeColor(): Int {
        return when (status) {
            Status.SUCCESS -> successColor
            Status.ERROR -> errorColor
            Status.FOCUSED, Status.FINISHED, Status.READY -> focusColor
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
        binding.componentEdtInputNicknameCardContainer.strokeColor = setStrokeColor()
        binding.componentEdtInputNicknameTvTitle.setTextColor(setTitleTextColor())
        setNoticeTextStatus()
        setAdditionalIconStatus()
        setDeleteIconStatus(binding.componentEdtInputNicknameEdtContent.hasFocus())
    }

    private fun setNoticeTextStatus() {
        binding.componentEdtInputNicknameTvAdditional.apply {
            text = when(status) {
                Status.ERROR -> {
                    if(errorType == "REGEX") {
                        resources.getString(R.string.error_nickname_regex)
                    } else {
                        resources.getString(R.string.error_same_nickname)
                    }
                }

                Status.SUCCESS -> resources.getString(R.string.success_can_use_nickname)
                else -> resources.getString(R.string.sign_up_with_phone_user_profile_additional)
            }
            setTextColor(when(status) {
                Status.ERROR -> errorColor
                Status.SUCCESS -> successColor
                else -> nonFocusNonInputColor
            })
        }
    }

    private fun setAdditionalIconStatus() {
        binding.componentEdtInputNicknameBtnErrorOrSuccess.apply {
            setImageResource(
                when (status) {
                    Status.ERROR -> R.drawable.ic_edt_error
                    Status.SUCCESS -> R.drawable.ic_edt_success
                    else -> R.drawable.btn_nickname_check_same
                }
            )
            visibility =
                if (isErrorEnable && (status == Status.ERROR || status == Status.SUCCESS || status == Status.READY)) VISIBLE else GONE
        }
    }

    private fun setDeleteIconStatus(focus: Boolean) {
        binding.componentEdtInputNicknameBtnClear.visibility = if (binding.componentEdtInputNicknameEdtContent.text.isEmpty() || !focus) View.GONE else View.VISIBLE
    }


    /** Set Status **/

    private fun checkStatus(focus: Boolean) {
        if (status == Status.ERROR || status == Status.SUCCESS || status == Status.READY) {
            return
        } else {
            status = if (focus) Status.FOCUSED else checkStatusWhenNotFocused()
        }
    }

    private fun checkStatusWhenNotFocused(): Status {
        return if (binding.componentEdtInputNicknameEdtContent.text.isNullOrEmpty()) Status.NON_FOCUSED_NO_INPUT else Status.NON_FOCUSED_INPUT
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

    private fun setReadyState() {
        status = Status.READY
    }


    private fun setFinishState() {
        status = Status.FINISHED
    }

    private fun checkFinished() {
        _isFinished.value = status == Status.FINISHED || status == Status.SUCCESS
    }

    fun getCheckResult(code : Int) {
        if(code == 2010) {
            setErrorState()
            errorType = "SAME"
        } else if (code == 2011) {
            setSuccessState()
            errorType = "SUCCESS"
        }
        updateUiWithState()
        checkFinished()
    }


    /** Regex **/

    private fun checkRegex(value: Editable?) : String {
        return if(!checkPatternMatches(value) || !checkLengthMatches(value)) {
            setErrorState()
            "REGEX"
        } else {
            setReadyState()
            "READY"
        }
    }

    private fun checkPatternMatches(value : Editable?) : Boolean{
        return if(!value.isNullOrEmpty()) Pattern.matches(nickNamePattern, value.toString()) else false
    }

    private fun checkLengthMatches(value : Editable?) : Boolean {
        return value?.length in 1..10
    }

    fun onDeleteClicked() {
        binding.componentEdtInputNicknameEdtContent.setText("")
    }

    fun onAdditionalClicked() {
        if(status == Status.READY) {
            _checkSameClicked.value.apply {
                if(this == null) {
                    _checkSameClicked.value = false
                } else {
                    _checkSameClicked.value = this.not()
                }
            }
        }
    }

    fun getUserInputContent(): String = binding.componentEdtInputNicknameEdtContent.text.toString()
}