package com.proteam.fithub.presentation.component

import android.content.Context
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
import com.proteam.fithub.databinding.ComponentEdtInputPhoneNumberBinding

class ComponentEdtPassword(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private lateinit var binding: ComponentEdtInputPasswordBinding
    private var isErrorContains : Boolean = false

    private var _doneState = MutableLiveData<Boolean>()
    val doneState : LiveData<Boolean> = _doneState

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
                strokeWhenNotError(b)
        }

        binding.componentEdtInputPasswordEdtContent.addTextChangedListener {
            setDoneState()
            setDeleteVisibility()
        }
    }

    private fun setDoneState() {
        _doneState.value = binding.componentEdtInputPasswordEdtContent.text.isNotEmpty()
    }

    private fun setDeleteVisibility() {
        binding.componentEdtInputPasswordBtnClear.visibility = if(binding.componentEdtInputPasswordEdtContent.text.isEmpty()) View.GONE else View.VISIBLE
    }

    fun onDeleteClicked() {
        binding.componentEdtInputPasswordEdtContent.setText("")
    }

    fun getUserInputContent() : String = binding.componentEdtInputPasswordEdtContent.text.toString()

    private fun strokeWhenNotError(b: Boolean): Int = if (b) doneStroke else normalStroke

    private var normalStroke = context.resources.getColor(R.color.gray_400, null)
    private var doneStroke = context.resources.getColor(R.color.gray_600, null)
}