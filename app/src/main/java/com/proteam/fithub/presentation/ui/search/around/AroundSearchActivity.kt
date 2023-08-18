package com.proteam.fithub.presentation.ui.search.around

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.proteam.fithub.R
import com.proteam.fithub.databinding.ActivityAroundSearchBinding
import com.proteam.fithub.presentation.ui.search.around.none.SearchAroundNoneFragment
import com.proteam.fithub.presentation.ui.search.around.normal.SearchAroundDefaultFragment
import com.proteam.fithub.presentation.ui.search.around.result.list.SearchAroundResultFragment
import com.proteam.fithub.presentation.ui.search.around.result.map.SearchAroundResultMapFragment
import com.proteam.fithub.presentation.ui.search.around.viewmodel.AroundSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AroundSearchActivity : AppCompatActivity() {
    private val REQUEST_PERMISSION_LOCATION = 10

    private lateinit var binding : ActivityAroundSearchBinding
    private val viewModel : AroundSearchViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_around_search)

        initBinding()
        initUi()
        checkPermissionForLocation(this)
        observeLocationData()

    }

    private fun initBinding() {
        binding.activity = this
        binding.viewModel = viewModel
    }

    private fun initUi() {
        initFragment()
        observeUserKeywords()
        updateLocation()
    }

    private fun initFragment() {
        SearchAroundDefaultFragment().changeFragment(null)
    }

    private fun observeUserKeywords() {
        binding.aroundSearchEdtKeyword.setOnEditorActionListener { text, i, keyEvent ->
            if(text.text.isNotEmpty() && i == EditorInfo.IME_NULL && keyEvent.action == MotionEvent.ACTION_DOWN) {
                viewModel.requestSearchLocationData(null, null)
            }
            return@setOnEditorActionListener true
        }

        binding.aroundSearchEdtKeyword.addTextChangedListener {
            initFragment()
        }
    }

    private fun Fragment.changeFragment(tag : String?) {
        supportFragmentManager.beginTransaction().replace(R.id.around_search_layout_container, this, tag).commit()
    }

    fun clickRecommends(tag : String) {
        binding.aroundSearchEdtKeyword.setText(tag)
        viewModel.requestSearchLocationData(null, null)
    }

    private fun checkPermissionForLocation(context: Context): Boolean {
        return if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
            false
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(LocationRequest.create().also { it.priority = LocationRequest.PRIORITY_HIGH_ACCURACY }, mLocationCallback, Looper.myLooper())
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { viewModel.setCurrentLocation(it) }
        }
    }

    private fun observeLocationData() {
        viewModel.currentMarkerItems.observe(this) {
            if(it.isNotEmpty()) {
                SearchAroundResultFragment().changeFragment(null)
            } else {
                SearchAroundNoneFragment().changeFragment(null)
            }
        }
    }

    fun onDeleteClicked() {
        viewModel.initKeyword()
        binding.aroundSearchEdtKeyword.setText("")
    }

    fun openMapFragment() {
        //mapFragment.changeFragment("Search")
    }

    fun onBackPress() {
        finish()
    }
}