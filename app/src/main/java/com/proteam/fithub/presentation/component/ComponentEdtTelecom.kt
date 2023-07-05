package com.proteam.fithub.presentation.component

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ComponentEdtInputTelecomBinding

class ComponentEdtTelecom(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private lateinit var binding: ComponentEdtInputTelecomBinding
    private var isErrorContains : Boolean = false

    private var status = ComponentStatus.NONE

    private var _isComplete = MutableLiveData<Boolean>()
    val isComplete : LiveData<Boolean> = _isComplete

    private var normalStroke = context.resources.getColor(R.color.gray_400, null)
    private var doneStroke = context.resources.getColor(R.color.gray_600, null)

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
            R.layout.component_edt_input_telecom,
            this,
            false
        )
        binding.layout = this
        addView(binding.root)
    }

    private fun initUi() {
        binding.componentEdtInputTelecomEdtContent.setOnFocusChangeListener { view, b ->
            binding.componentEdtInputTelecomCardContainer.strokeColor =
                strokeWhenNotError(b)
        }

        binding.componentEdtInputTelecomEdtContent.addTextChangedListener {
            setStatus(it)
            setDoneState()
        }
    }

    private fun setStatus(value : Editable?) {
        status = if(value.isNullOrEmpty()) {
            ComponentStatus.NONE
        } else {
            ComponentStatus.DONE
        }
    }

    private fun setDoneState() {
        _isComplete.value = status == ComponentStatus.DONE
    }

    fun getUserInputContent() : String = binding.componentEdtInputTelecomEdtContent.text.toString()

    fun getUserSelectedTelecom(telecom : String) {
        binding.componentEdtInputTelecomEdtContent.setText(telecom)
    }

    private fun strokeWhenNotError(b: Boolean): Int = if (b) doneStroke else normalStroke
}