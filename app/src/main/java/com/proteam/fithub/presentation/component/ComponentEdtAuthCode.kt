package com.proteam.fithub.presentation.component

import android.content.Context
import android.os.CountDownTimer
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
import com.proteam.fithub.databinding.ComponentEdtInputAuthCodeBinding

class ComponentEdtAuthCode(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private lateinit var binding: ComponentEdtInputAuthCodeBinding

    /** true : 에러 / false : 일반 **/
    private var state: Boolean = false

    private var isErrorContains: Boolean = false

    private var _doneState = MutableLiveData<Boolean>()
    val doneState: LiveData<Boolean> = _doneState

    val count = object : CountDownTimer(180000, 1000) {
        override fun onTick(p0: Long) {
            binding.componentEdtInputAuthCodeTvTimer.text = "${(p0 / 60000).toInt()} : ${((p0/1000) % 60)}"
        }

        override fun onFinish() {
            Log.d("----", "onFinish: TIMEUP")
        }
    }

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
            R.layout.component_edt_input_auth_code,
            this,
            false
        )
        binding.layout = this
        addView(binding.root)
    }

    private fun initUi() {
        binding.componentEdtInputAuthCodeEdtContent.setOnFocusChangeListener { view, b ->
            binding.componentEdtInputAuthCodeCardContainer.strokeColor = strokeWhenNotError(b)
            binding.componentEdtInputAuthCodeTvTitle.setTextColor(strokeWhenNotError(b))
            setDeleteVisibility(b)
        }

        binding.componentEdtInputAuthCodeEdtContent.addTextChangedListener {
            checkWhenNotError()
            setDeleteVisibility(binding.componentEdtInputAuthCodeEdtContent.hasFocus())
        }
        startCountDownTimer()
    }
    /*
        private fun checkWhenError() {
            state = if(regexError()) {
                setDoneState()
                false
            } else {
                setError()
                true
            }
        } */

    fun startCountDownTimer() {
        count.start()
    }

    fun stopCountDownTimer() {
        count.cancel()
    }

    private fun checkWhenNotError() {
        setDoneState()
    }

    private fun resetStroke() {
        binding.componentEdtInputAuthCodeCardContainer.strokeColor = doneStroke
        binding.componentEdtInputAuthCodeTvTitle.setTextColor(normalStroke)
        binding.componentEdtInputAuthCodeTvAdditional.visibility = View.GONE
    }
    /*
        private fun setError() {
            binding.componentEdtInputNameTvAdditional.apply {
                visibility = View.VISIBLE
                setTextColor(errorStroke)
                text = "휴대폰 번호가 올바르지 않습니다"
            }
            binding.componentEdtInputNameTvTitle.setTextColor(errorStroke)
            binding.componentEdtInputNameCardContainer.strokeColor = errorStroke

            binding.componentEdtInputNameBtnError.visibility = View.VISIBLE
        } */

    private fun setDoneState() {
        resetStroke()
        _doneState.value = binding.componentEdtInputAuthCodeEdtContent.text.length == 6
    }

    private fun setDeleteVisibility(focus: Boolean) {
        binding.componentEdtInputAuthCodeBtnClear.visibility =
            if (binding.componentEdtInputAuthCodeEdtContent.text.isEmpty() || !focus) View.GONE else View.VISIBLE
    }

    fun onDeleteClicked() {
        binding.componentEdtInputAuthCodeEdtContent.setText("")
    }

    fun getUserInputContent(): String =
        binding.componentEdtInputAuthCodeEdtContent.text.toString()

    private fun strokeWhenNotError(b: Boolean): Int {
        return if (state) errorStroke
        else if (b) doneStroke
        else normalStroke
    }

    //private fun regexError() : Boolean =  if(!checkLength()) true else  checkLength() && checkStart3Words() == true

    private var normalStroke = context.resources.getColor(R.color.gray_400, null)
    private var doneStroke = context.resources.getColor(R.color.gray_600, null)
    private var errorStroke = context.resources.getColor(R.color.color_error, null)

    /*private fun checkLength() : Boolean = binding.componentEdtInputNameEdtContent.text.length == 11
    private fun checkStart3Words() : Boolean? = binding.componentEdtInputNameEdtContent.text?.let { if(it.length >= 3) it.substring(0, 3) == "010" else false} */

    var authCodeEdt : EditText = binding.componentEdtInputAuthCodeEdtContent
}