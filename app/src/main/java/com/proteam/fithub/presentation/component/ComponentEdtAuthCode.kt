package com.proteam.fithub.presentation.component

import android.content.Context
import android.os.CountDownTimer
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
import com.proteam.fithub.databinding.ComponentEdtInputAuthCodeBinding

class ComponentEdtAuthCode(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private lateinit var binding: ComponentEdtInputAuthCodeBinding
    private var status = Status.NON_FOCUSED_NO_INPUT

    private val errorColor = resources.getColor(R.color.color_error, null)
    private val successColor = resources.getColor(R.color.color_info, null)
    private val nonFocusNonInputColor = resources.getColor(R.color.icon_enabled, null)
    private val nonFocusInputColor = resources.getColor(R.color.icon_disabled, null)
    private val focusColor = resources.getColor(R.color.icon_sub, null)

    private var isErrorEnable: Boolean = false

    private var _isFinished = MutableLiveData<Boolean>()
    val isFinished : LiveData<Boolean> = _isFinished

    private val count = object : CountDownTimer(180000, 1000) {
        override fun onTick(p0: Long) {
            binding.componentEdtInputAuthCodeTvTimer.text = "${(p0 / 60000).toInt()} : ${((p0/1000) % 60)}"
        }

        override fun onFinish() {
            status = Status.FOCUSED
        }
    }

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
            R.layout.component_edt_input_auth_code,
            this,
            false
        )
        binding.layout = this
        addView(binding.root)
    }

    private fun initUi() {
        setFocusListener()
        setTextChangeListener()
        startCountDownTimer()
    }

    private fun setFocusListener() {
        binding.componentEdtInputAuthCodeEdtContent.setOnFocusChangeListener { _, focus ->
            checkStatus(focus)
            updateUiWithState()
        }
    }

    private fun setTextChangeListener() {
        binding.componentEdtInputAuthCodeEdtContent.addTextChangedListener {
            checkLength(it)
            updateUiWithState()
            checkFinished()
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
        binding.componentEdtInputAuthCodeCardContainer.strokeColor = setStrokeColor()
        binding.componentEdtInputAuthCodeTvTitle.setTextColor(setTitleTextColor())
        setDeleteIconStatus(binding.componentEdtInputAuthCodeEdtContent.hasFocus())
    }

    private fun setDeleteIconStatus(focus: Boolean) {
        binding.componentEdtInputAuthCodeBtnClear.visibility = if (binding.componentEdtInputAuthCodeEdtContent.text.isEmpty() || !focus) View.GONE else View.VISIBLE
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
        return if (binding.componentEdtInputAuthCodeEdtContent.text.isNullOrEmpty()) Status.NON_FOCUSED_NO_INPUT else Status.NON_FOCUSED_INPUT
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

    private fun setDeleteVisibility(focus: Boolean) {
        binding.componentEdtInputAuthCodeBtnClear.visibility =
            if (binding.componentEdtInputAuthCodeEdtContent.text.isEmpty() || !focus) View.GONE else View.VISIBLE
    }


    fun onDeleteClicked() {
        binding.componentEdtInputAuthCodeEdtContent.setText("")
    }



    /** Timer **/

    fun startCountDownTimer() {
        count.start()
    }

    fun stopCountDownTimer() {
        count.cancel()
    }

    /** Regex **/

    private fun checkLength(value : Editable?) {
        status = if(value?.length == 6) Status.FINISHED else Status.FOCUSED
    }

    /** Etc **/

    fun getUserInputContent(): String =
        binding.componentEdtInputAuthCodeEdtContent.text.toString()

    var authCodeEdt : EditText = binding.componentEdtInputAuthCodeEdtContent


}
