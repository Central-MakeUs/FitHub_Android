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
import com.proteam.fithub.databinding.ComponentEdtInputNameBinding

class ComponentEdtName(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {
    private lateinit var binding: ComponentEdtInputNameBinding

    /** true : 에러 / false : 일반 **/
    private var state: Boolean = false

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
            R.layout.component_edt_input_name,
            this,
            false
        )
        binding.layout = this
        addView(binding.root)
    }

    private fun initUi() {
        binding.componentEdtInputNameEdtContent.setOnFocusChangeListener { view, b ->
            binding.componentEdtInputNameCardContainer.strokeColor = strokeWhenNotError(b)
            binding.componentEdtInputNameTvTitle.setTextColor(strokeWhenNotError(b))
            setDeleteVisibility(b)
        }

        binding.componentEdtInputNameEdtContent.addTextChangedListener {
            checkWhenNotError()
            setDeleteVisibility(binding.componentEdtInputNameEdtContent.hasFocus())
        }
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

    private fun checkWhenNotError() {
        setDoneState()
    }

    private fun resetStroke() {
        binding.componentEdtInputNameCardContainer.strokeColor = doneStroke
        binding.componentEdtInputNameTvTitle.setTextColor(normalStroke)
        binding.componentEdtInputNameTvAdditional.visibility = View.GONE
        binding.componentEdtInputNameBtnError.visibility = View.GONE
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
        _doneState.value = binding.componentEdtInputNameEdtContent.text.isNotEmpty()
    }

    private fun setDeleteVisibility(focus: Boolean) {
        binding.componentEdtInputNameBtnClear.visibility =
            if (binding.componentEdtInputNameEdtContent.text.isEmpty() || !focus) View.GONE else View.VISIBLE
    }

    fun onDeleteClicked() {
        binding.componentEdtInputNameEdtContent.setText("")
    }

    fun getUserInputContent(): String =
        binding.componentEdtInputNameEdtContent.text.toString()

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
}