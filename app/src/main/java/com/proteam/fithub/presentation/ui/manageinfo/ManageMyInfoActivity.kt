package com.proteam.fithub.presentation.ui.manageinfo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivityManageMyInfoBinding
import com.proteam.fithub.presentation.component.ComponentDialogOneButton
import com.proteam.fithub.presentation.component.ComponentDialogYesNo
import com.proteam.fithub.presentation.ui.FitHub
import com.proteam.fithub.presentation.ui.change_password.ChangePasswordActivity
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.manageinfo.viewmodel.ManageMyInfoViewModel
import com.proteam.fithub.presentation.ui.sign.`in`.social.SocialSignInActivity
import com.proteam.fithub.presentation.util.AnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageMyInfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityManageMyInfoBinding
    private val viewModel : ManageMyInfoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_my_info)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        observeMyInfoData()
        initBinding()
    }

    private fun initBinding() {
        binding.activity = this
        binding.lifecycleOwner = this
    }

    private fun observeMyInfoData() {
        viewModel.myInfoData.observe(this) {
            binding.data = it
        }
    }

    fun onSignOutClicked() {
        ComponentDialogYesNo(::requestSignOut).show(supportFragmentManager, "SIGN_OUT")
    }

    private fun requestSignOut() {
        viewModel.requestSignOut()
        observeSignOutState()
    }

    private fun observeSignOutState() {
        viewModel.signOutState.observe(this) {
            if(it == 2000) ComponentDialogOneButton(::afterSignOut).show(supportFragmentManager, "SIGN_OUT")
        }
    }

    fun openChangePasswordActivity() {
        requestGotoSignIn.launch(Intent(this, ChangePasswordActivity::class.java))
    }

    private fun afterSignOut() {
        setResult(RESULT_OK, Intent(this, MainActivity::class.java).putExtra("state", true))
        finish()
    }

    fun onBackPress() {
        finish()
    }

    private val requestGotoSignIn =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val state = it.data!!.extras?.getBoolean("state")
                if(state == true) {
                    setResult(RESULT_OK, Intent(this, MainActivity::class.java).putExtra("state", true))
                    finish()
                }
            }
        }
}