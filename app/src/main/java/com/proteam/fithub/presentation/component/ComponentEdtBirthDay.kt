package com.proteam.fithub.presentation.component

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.util.Log
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

class ComponentEdtBirthDay(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private lateinit var binding: ComponentEdtInputBirthBinding
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
            R.layout.component_edt_input_birth,
            this,
            false
        )
        binding.layout = this
        addView(binding.root)
    }

    private fun initUi() {
        binding.componentEdtInputBirthEdtBirthday.setOnFocusChangeListener { view, b ->
            binding.componentEdtInputBirthCardContainer.strokeColor = strokeWhenNotError(b)
            binding.componentEdtInputBirthTvTitle.setTextColor(normalStroke)
        }

        binding.componentEdtInputBirthEdtBirthday.addTextChangedListener {
            if(it?.length == 6) {
                binding.componentEdtInputBirthEdtGender.requestFocus()
            }
            if (checkAllTextInserted()) {
                checkWhenError(it)
            } else {
                resetStroke()
                _doneState.value = false
            }
        }
        binding.componentEdtInputBirthEdtGender.addTextChangedListener {
            if (checkAllTextInserted()) {
                checkWhenError(binding.componentEdtInputBirthEdtBirthday.text)
            } else {
                resetStroke()
                _doneState.value = false
            }
        }
    }

    private fun checkAllTextInserted(): Boolean =
        binding.componentEdtInputBirthEdtBirthday.text.length == 6 && binding.componentEdtInputBirthEdtGender.text.isNotEmpty()

    private fun checkWhenError(value: Editable?) {
        if (!regexError(value)) {
            setError("Invalidate")
        } else if (checkAge(value) < 14) {
            setError("NotEnoughAge")
        }
        _doneState.value = regexError(value)
    }

    private fun resetStroke() {
        binding.componentEdtInputBirthCardContainer.strokeColor = doneStroke
        binding.componentEdtInputBirthTvTitle.setTextColor(normalStroke)
        binding.componentEdtInputPhoneNumberTvAdditional.visibility = View.GONE
        binding.componentEdtInputBirthBtnError.visibility = View.GONE
    }

    private fun setError(type : String) {
        binding.componentEdtInputPhoneNumberTvAdditional.apply {
            visibility = View.VISIBLE
            setTextColor(errorStroke)

            text = if(type == "Invalidate") "생년월일 및 성별을 정확히 입력해주세요." else "만 14세 미만은 서비스 이용이 불가합니다."
        }
        binding.componentEdtInputBirthTvTitle.setTextColor(errorStroke)
        binding.componentEdtInputBirthCardContainer.strokeColor = errorStroke

        binding.componentEdtInputBirthBtnError.visibility = View.VISIBLE
    }

    fun getUserInputContent(): String =
        binding.componentEdtInputBirthEdtBirthday.text.toString()

    private fun strokeWhenNotError(b: Boolean): Int = if (b) doneStroke else normalStroke

    private fun regexError(value: Editable?): Boolean {
        return (checkLength(value) && setBirthValidate(value))
    }

    private var normalStroke = context.resources.getColor(R.color.gray_400, null)
    private var doneStroke = context.resources.getColor(R.color.gray_600, null)
    private var errorStroke = context.resources.getColor(R.color.color_error, null)

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

    private fun checkGenderCode (century : String) : Boolean {
        return if(century == "19") binding.componentEdtInputBirthEdtGender.text.toString().toInt() in 1..2 else binding.componentEdtInputBirthEdtGender.text.toString().toInt() in 3..4
    }

    private fun checkAge(value: Editable?) : Int {
        return if (value!!.substring(0, 2) <= LocalDate.now().year.toString().substring(2, 4)) {
            getAge(value,"20")
        } else {
            getAge(value, "19")
        }
    }

    private fun getAge(value: Editable, century: String) : Int {
        return if(LocalDate.now().month.value * 100 + LocalDate.now().dayOfMonth < value.substring(2,4).toInt() * 100 + value.substring(4,6).toInt()) {
            LocalDate.now().year - "$century${value.substring(0,2)}".toInt() - 1
        } else LocalDate.now().year - "$century${value.substring(0,2)}".toInt()
    }

    fun birthDayEdt() : EditText = binding.componentEdtInputBirthEdtBirthday
}