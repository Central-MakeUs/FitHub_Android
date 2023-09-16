package com.proteam.fithub.presentation.util

import android.os.Bundle
import android.util.Log
import com.proteam.fithub.presentation.ui.FitHub.Companion.analytics

object AnalyticsHelper {

    fun setAnalyticsLog(javaClass : String) {
        val bundle = Bundle()
        bundle.putString("InComing", javaClass)
        analytics.logEvent(javaClass, bundle)
    }
}