package com.proteam.fithub.presentation.util

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("common_setImageButton")
fun ImageView.setImageButton(path : Any?) {
    Glide.with(this).load(path).into(this)
}

@BindingAdapter("common_setBackgroundDrawable")
fun ImageButton.setBackground(path : Any?) {
    this.setBackgroundResource(path.toString().toInt())
}

@BindingAdapter("common_Circle_Image")
fun ImageView.setCircleImage(path: Any?) {
    Glide.with(this).load(path).circleCrop().into(this)
}

@BindingAdapter("common_set_Image")
fun ImageView.setImage(path : Any?) {
    Glide.with(this).load(path).into(this)
}