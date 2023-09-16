package com.proteam.fithub.presentation.ui.findpassword

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivityFindPasswordBinding
import com.proteam.fithub.presentation.ui.findpassword.phone.FindPasswordAuthPhoneNumberFragment
import com.proteam.fithub.presentation.util.AnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindPasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFindPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_find_password)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        initBinding()
        initFragments()

    }

    private fun initBinding() {
        binding.activity = this
    }

    private fun initFragments() {
        supportFragmentManager.beginTransaction().replace(R.id.find_password_layout_container, FindPasswordAuthPhoneNumberFragment()).commit()
    }

    fun changeFragments(fragment : Fragment) {
        supportFragmentManager.beginTransaction().addToBackStack(fragment.id.toString()).add(R.id.find_password_layout_container, fragment, "Find").commit()
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