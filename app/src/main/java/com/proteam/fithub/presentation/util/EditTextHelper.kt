package com.proteam.fithub.presentation.util

import android.text.InputFilter
import android.text.Spanned
import android.widget.EditText

object EditTextHelper {

    fun EditText.banSpaceInput() {
        this.filters = arrayOf(object : InputFilter { override fun filter(p0: CharSequence, p1: Int, p2: Int, p3: Spanned?, p4: Int, p5: Int
        ): CharSequence {
            if(p0 == "" || !p0.contains(" ")) {
                return p0
            }
            return p0.trim()
        }
        })
    }
}