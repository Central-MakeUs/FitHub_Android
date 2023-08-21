package com.proteam.fithub.presentation.ui.terms.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.proteam.fithub.databinding.ActivityTermsDetailBinding
import com.proteam.fithub.presentation.ui.terms.detail.viewmodel.TermsDetailViewModel
import com.proteam.fithub.presentation.util.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermsDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTermsDetailBinding
    private val viewModel : TermsDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTermsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestTermsData()
    }

    private fun requestTermsData() {
        intent.type?.let { viewModel.requestTermsData(it.toInt()).also { showLoadingDialog() } }
        observeTermsData()
    }

    private fun observeTermsData() {
        viewModel.termsData.observe(this) {
            dismissLoadingDialog()

            initWebView(it.link)
        }
    }

    private fun initWebView(link : String) {
        Intent(Intent.ACTION_VIEW, Uri.parse(link)).apply { startActivity(this) }
        finish()
    }



    private var loadingDialog = LoadingDialog()
    private fun showLoadingDialog() = loadingDialog.show(supportFragmentManager, null)
    private fun dismissLoadingDialog() = loadingDialog.dismiss()
}