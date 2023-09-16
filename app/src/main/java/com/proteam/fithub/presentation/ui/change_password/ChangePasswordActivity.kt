package com.proteam.fithub.presentation.ui.change_password

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivityChangePasswordBinding
import com.proteam.fithub.presentation.ui.change_password.check_password.CheckPasswordFragment
import com.proteam.fithub.presentation.util.AnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        initFragment()
        initBinding()
    }

    private fun initBinding() {
        binding.activity = this
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.change_password_layout_container, CheckPasswordFragment()).commit()
    }

    fun onBackPress() {
        if(supportFragmentManager.backStackEntryCount == 0) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
            supportFragmentManager.beginTransaction().commit()
        }
    }
}