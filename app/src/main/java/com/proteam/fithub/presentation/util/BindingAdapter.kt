package com.proteam.fithub.presentation.util

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

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

@BindingAdapter("common_iv_radius_5")
fun ImageView.setRadius5Image(path: Any?) {
    Glide.with(this).load(path).transform(CenterCrop(), RoundedCorners(20)).into(this)
}

@BindingAdapter("margin_End")
fun View.marginEnd(dimen : Float) {
    this.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        marginEnd = dimen.toInt()
    }
}

