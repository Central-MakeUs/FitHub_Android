package com.proteam.fithub.presentation.ui.terms.terms

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivityTermsBinding
import com.proteam.fithub.presentation.ui.terms.detail.TermsDetailActivity
import com.proteam.fithub.presentation.ui.terms.terms.adapter.TermsAdapter
import com.proteam.fithub.presentation.util.AnalyticsHelper

class TermsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTermsBinding
    private val termsAdapter by lazy {
        TermsAdapter(returnTitles(), ::onOpenDetailTerms)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        initBinding()
        initRv()
    }

    private fun initBinding() {
        binding.activity = this
    }

    private fun initRv() {
        binding.termsRvContents.adapter = termsAdapter
    }

    private fun onOpenDetailTerms(position : Int) {
        startActivity(Intent(this, TermsDetailActivity::class.java).setType(position.toString()))
    }

    fun onBackPress() {
        finish()
    }

    /** Dummy **/
    private fun returnTitles() : List<String> = listOf(
        resources.getString(R.string.terms_title_1),
        resources.getString(R.string.terms_title_2),
        resources.getString(R.string.terms_title_3),
        resources.getString(R.string.terms_title_4)
    )
}