package com.proteam.fithub.presentation.component

import android.content.Context
import android.text.Editable
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
import com.proteam.fithub.databinding.ComponentEdtInputBirthBinding
import java.time.LocalDate

class ComponentBirthDay(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private lateinit var binding: ComponentEdtInputBirthBinding
    private var isErrorEnable: Boolean = false

    private var status = Status.NON_FOCUSED_NO_INPUT

    private var _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> = _isFinished

    private var errorType: String = ""

    private val errorColor = resources.getColor(R.color.color_error, null)
    private val successColor = resources.getColor(R.color.color_info, null)
    private val nonFocusNonInputColor = resources.getColor(R.color.icon_enabled, null)
    private val nonFocusInputColor = resources.getColor(R.color.icon_disabled, null)
    private val focusColor = resources.getColor(R.color.icon_sub, null)

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
            R.layout.component_edt_input_birth,
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
        binding.componentEdtInputBirthEdtBirthday.setOnFocusChangeListener { _, focus ->
            checkStatus(focus)
            updateUiWithState(errorType)
        }
        binding.componentEdtInputBirthEdtGender.setOnFocusChangeListener { _, focus ->
            checkStatus(focus)
            updateUiWithState(errorType)
        }
    }

    private fun setTextChangeListener() {
        binding.componentEdtInputBirthEdtBirthday.addTextChangedListener {
            if (it?.length == 6) {
                binding.componentEdtInputBirthEdtGender.requestFocus()
            }
            if (checkAllTextInserted()) {
                errorType = checkWhenError(it)
            } else {
                setFocusedState()
            }
            updateUiWithState(errorType)
            setDeleteVisibility()
            checkFinished()
        }
        binding.componentEdtInputBirthEdtGender.addTextChangedListener {
            if (checkAllTextInserted()) {
                errorType = checkWhenError(binding.componentEdtInputBirthEdtBirthday.text)
            } else {
                setFocusedState()
            }
            updateUiWithState(errorType)
            setDeleteVisibility()
            checkFinished()
        }
    }

    private fun checkAllTextInserted(): Boolean =
        binding.componentEdtInputBirthEdtBirthday.text.length == 6 && binding.componentEdtInputBirthEdtGender.text.isNotEmpty()

    private fun checkWhenError(value: Editable?) : String{
        return if (!regexError(value)) {
            setErrorState()
            "NOT_EXIST"
        } else if (checkAge(value) < 14) {
            setErrorState()
            "UNDER_AGE"
        } else if (checkAge(value) > 14 && regexError(value)) {
            setFinishState()
            ""
        } else {
            setFocusedState()
            ""
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

    private fun updateUiWithState(errorType: String) {
        binding.componentEdtInputBirthCardContainer.strokeColor = setStrokeColor()
        binding.componentEdtInputBirthTvTitle.setTextColor(setTitleTextColor())
        setNoticeTextStatus(errorType)
        setAdditionalIconStatus()
        setDeleteIconStatus()
    }

    private fun setNoticeTextStatus(errorType: String) {
        binding.componentEdtInputBirthTvAdditional.apply {
            visibility =
                if (isErrorEnable && (status == Status.ERROR || status == Status.SUCCESS)) VISIBLE else GONE
            text =
                when (errorType) {
                    "NOT_EXIST" -> resources.getString(R.string.error_not_exist_birth)
                    else -> resources.getString(R.string.error_under_age)
                }

            setTextColor(if(status == Status.ERROR) errorColor else nonFocusInputColor)
        }
    }

    private fun setAdditionalIconStatus() {
        binding.componentEdtInputBirthBtnError.apply {
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

    private fun setDeleteIconStatus() {
        binding.componentEdtInputBirthBtnClear.visibility =
            if ((binding.componentEdtInputBirthEdtBirthday.text.isEmpty() && binding.componentEdtInputBirthEdtGender.text.isEmpty()) || (!binding.componentEdtInputBirthEdtBirthday.hasFocus() && !binding.componentEdtInputBirthEdtGender.hasFocus())) View.GONE else View.VISIBLE
    }


    /** Set Status **/
    private fun checkStatus(focus: Boolean) {
        if (status == Status.ERROR || status == Status.SUCCESS) {
            return
        } else {
            status = if (focus) Status.FOCUSED else checkStatusWhenNotFocused()
        }
    }

    private fun checkStatusWhenNotFocused(): Status {
        return if (binding.componentEdtInputBirthEdtBirthday.text.isNullOrEmpty()) Status.NON_FOCUSED_NO_INPUT else Status.NON_FOCUSED_INPUT
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

    /** DELETE BUTTON **/
    private fun setDeleteVisibility() {
        binding.componentEdtInputBirthBtnClear.visibility =
            if ((binding.componentEdtInputBirthEdtBirthday.text.isEmpty() && binding.componentEdtInputBirthEdtGender.text.isEmpty()) || (!binding.componentEdtInputBirthEdtBirthday.hasFocus() && !binding.componentEdtInputBirthEdtGender.hasFocus())) View.GONE else View.VISIBLE
    }

    fun onDeleteClicked() {
        binding.componentEdtInputBirthEdtBirthday.setText("")
        binding.componentEdtInputBirthEdtGender.setText("")
    }


    /** Regex **/

    private fun regexError(value: Editable?): Boolean {
        return (checkLength(value) && setBirthValidate(value))
    }

    private fun checkLength(value: Editable?): Boolean = value?.length == 6 && value.isNotEmpty()
    private fun setBirthValidate(value: Editable?): Boolean {
        return if (value!!.substring(0, 2) <= LocalDate.now().year.toString().substring(2, 4)) {
            checkBirthAvailable(value, "20") && checkGenderCode("20")
        } else {
            checkBirthAvailable(value, "19") && checkGenderCode("19")
        }
    }

    private fun String.convertToInt(): Int = this.toInt()

    private fun checkBirthAvailable(value: Editable, century: String): Boolean {
        return try {
            LocalDate.of(
                "$century${value.substring(0, 2)}".convertToInt(),
                value.substring(2, 4).convertToInt(),
                value.substring(4, 6).convertToInt()
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun checkGenderCode(century: String): Boolean {
        return if (century == "19") binding.componentEdtInputBirthEdtGender.text.toString()
            .toInt() in 1..2 else binding.componentEdtInputBirthEdtGender.text.toString()
            .toInt() in 3..4
    }

    private fun checkAge(value: Editable?): Int {
        return if (value!!.substring(0, 2) <= LocalDate.now().year.toString().substring(2, 4)) {
            getAge(value, "20")
        } else {
            getAge(value, "19")
        }
    }

    private fun getAge(value: Editable, century: String): Int {
        return if (LocalDate.now().month.value * 100 + LocalDate.now().dayOfMonth < value.substring(
                2,
                4
            ).toInt() * 100 + value.substring(4, 6).toInt()
        ) {
            LocalDate.now().year - "$century${value.substring(0, 2)}".toInt() - 1
        } else LocalDate.now().year - "$century${value.substring(0, 2)}".toInt()
    }


    /** Etc **/

    fun getUserInputBirth(): String =
        binding.componentEdtInputBirthEdtBirthday.text.toString()
    fun getUserInputGender() : String = binding.componentEdtInputBirthEdtGender.text.toString()
    fun birthDayEdt(): EditText = binding.componentEdtInputBirthEdtBirthday
}