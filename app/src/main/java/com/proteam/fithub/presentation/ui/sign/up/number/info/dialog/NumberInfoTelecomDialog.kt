package com.proteam.fithub.presentation.ui.sign.up.number.info.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.proteam.fithub.R
import com.proteam.fithub.databinding.DialogBottomSelectTelecomBinding
import com.proteam.fithub.presentation.ui.sign.up.number.info.dialog.adapter.NumberInfoTelecomAdapter
import com.proteam.fithub.presentation.ui.sign.up.number.info.viewmodel.NumberInfoViewModel
import com.proteam.fithub.presentation.ui.sign.up.number.viewmodel.NumberSignUpViewModel

class NumberInfoTelecomDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogBottomSelectTelecomBinding

    private val numberViewModel : NumberSignUpViewModel by activityViewModels()

    private val adapter by lazy {
        NumberInfoTelecomAdapter(telecoms(), ::onTelecomClicked)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_bottom_select_telecom,
            container,
            false
        )

        initTelecomRV()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun initTelecomRV() {
        binding.dialogBottomSelectTelecomRvContent.adapter = adapter
    }

    private fun onTelecomClicked(title: String) {
        numberViewModel.setSelectedTelecom(title)
        dismiss()
    }

    private fun telecoms(): List<String> =
        listOf("SKT", "KT", "LG U+", "SKT 알뜰폰", "KT 알뜰폰", "LG U+ 알뜰폰")

}