package com.proteam.fithub.presentation.util

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ComponentToastBinding

object FithubToast {

    fun createToast(context: Context, message: String): Toast {
        val binding: ComponentToastBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.component_toast,
            null,
            false
        )

        binding.componentToastText.text = message

        return Toast(context).apply {
            setGravity(Gravity.CENTER or Gravity.FILL_HORIZONTAL, 100.toPx(), 0)
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}