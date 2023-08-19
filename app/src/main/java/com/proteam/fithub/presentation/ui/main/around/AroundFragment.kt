package com.proteam.fithub.presentation.ui.main.around

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.chip.Chip
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseExercises
import com.proteam.fithub.data.remote.response.ResponseLocationData
import com.proteam.fithub.databinding.FragmentAroundBinding
import com.proteam.fithub.presentation.ui.main.MainActivity
import com.proteam.fithub.presentation.ui.main.around.list.AroundListFragment
import com.proteam.fithub.presentation.ui.main.around.viewmodel.AroundViewModel
import com.proteam.fithub.presentation.util.CustomSnackBar
import dagger.hilt.android.AndroidEntryPoint
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

@AndroidEntryPoint
class AroundFragment : Fragment(), MapView.MapViewEventListener {
    private val REQUEST_PERMISSION_LOCATION = 10

    private lateinit var eventListener : MarkerEventListener
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: FragmentAroundBinding
    private val viewModel : AroundViewModel by activityViewModels()
    private var mapView : MapView? = null

    private var isTracking = true
    private var isFirst = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_around, container, false)

        initBinding()
        initUi()
        initMapView()
        checkPermissionForLocation(requireActivity())
        observeMarkers()
        observeFilterExercises()

        return binding.root
    }

    private fun checkPermissionForLocation(context: Context): Boolean {
        return if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
            false
        }
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initMapView() {
        mapView = MapView(requireActivity())
        binding.fgAroundContainerMap.addView(mapView)
        updateLocation()
        mapView?.setMapViewEventListener(this)
        eventListener = MarkerEventListener()
        mapView?.setZoomLevel(3, true)
        mapView?.setPOIItemEventListener(eventListener)
        requestLocationWhenInit()
    }

    private fun initUi() {
        initChipGroup()
    }

    private fun setMapToMyLocation(location : Location) {
        if(isTracking) mapView?.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(location.latitude, location.longitude), true)
        removeLegacyCurrentMarker()
        mapView?.addPOIItem(returnCurrentMarker(location))
    }

    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.requestLocationUpdates(LocationRequest.create().also { it.priority = LocationRequest.PRIORITY_HIGH_ACCURACY }, mLocationCallback, Looper.myLooper())
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { viewModel.setCurrentLocation(it) }
            locationResult.lastLocation?.let { setMapToMyLocation(it) }
        }
    }

    private fun removeLegacyCurrentMarker() {
        for(i in mapView!!.poiItems) {
            if(i.tag == 0) {
                mapView?.removePOIItem(i)
            }
        }
    }


    private fun returnCurrentMarker(location : Location) = MapPOIItem().apply {
        itemName = "CURRENT"
        tag = 0
        markerType = MapPOIItem.MarkerType.CustomImage
        mapPoint = MapPoint.mapPointWithGeoCoord(location.latitude, location.longitude)
        customImageResourceId = R.drawable.ic_user_location
        isCustomImageAutoscale = false
        isShowCalloutBalloonOnTouch = false
        setCustomImageAnchor(0.5F, 0.5F)
    }

    fun onNavigateClicked() {
        isTracking = true
        viewModel.currentLocation.value?.let { setMapToMyLocation(it) }
    }

    fun onResearchClicked() {
        viewModel.setSearchNeed(false)
        viewModel.requestLocationData(viewModel.selectedFilter.value)
    }

    fun onOpenListClicked() {

    }

    private fun requestLocationWhenInit() {
        viewModel.targetLocation.observe(viewLifecycleOwner) {
            if(isFirst) {
                viewModel.requestLocationData(viewModel.selectedFilter.value)
            }
        }
    }

    private fun observeMarkers() {
        viewModel.currentMarkerItems.observe(viewLifecycleOwner) {
            changeMarkers(it)
        }
    }

    private fun changeMarkers(markers : MutableList<ResponseLocationData.LocationItems>) {
        removeAllMarkers()
        appendMarkers(markers)
    }

    private fun removeAllMarkers() {
        for(i in mapView!!.poiItems) {
            if(i.tag != 0) {
                mapView?.removePOIItem(i)
            }
        }
    }

    private fun appendMarkers(markers : MutableList<ResponseLocationData.LocationItems>) {
        for (i in markers) {
            mapView?.addPOIItem(i.makeMarker())
        }
    }

    private fun ResponseLocationData.LocationItems.makeMarker() : MapPOIItem {
        return MapPOIItem().apply {
            itemName = name
            tag = 1
            markerType = MapPOIItem.MarkerType.CustomImage
            selectedMarkerType = MapPOIItem.MarkerType.CustomImage
            mapPoint = MapPoint.mapPointWithGeoCoord(y.toDouble(), x.toDouble())
            customImageResourceId = R.drawable.ic_around_marker_not_clicked
            customSelectedImageResourceId = R.drawable.ic_around_marker_clicked
            isCustomImageAutoscale = false
            isShowCalloutBalloonOnTouch = false
            showAnimationType = null
            setCustomImageAnchor(0.5F, 1F)
        }
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
        isTracking = false
        isFirst = false
        viewModel.setTargetLocation(Pair(p1!!.mapPointGeoCoord?.latitude!!, p1.mapPointGeoCoord?.longitude!!))
    }
    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {
        if(!isFirst) viewModel.setSearchNeed(true)
        viewModel.setTargetLocation(Pair(p1!!.mapPointGeoCoord?.latitude!!, p1.mapPointGeoCoord?.longitude!!))
    }

    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {}
    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewInitialized(p0: MapView?) {}


    inner class MarkerEventListener : MapView.POIItemEventListener {
        override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
            viewModel.currentMarkerItems.value?.find { it.name == p1?.itemName }?.let { binding.fgAroundComponentLocationCard.also { it.visibility = View.VISIBLE }.getData(it) }
        }
        override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {}
        override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?, p2: MapPOIItem.CalloutBalloonButtonType?) {}
        override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {}
    }


    private fun initChipGroup() {
        binding.fgAroundChipgroupExerciseFilter.apply {
            isSingleSelection = true

            setOnCheckedStateChangeListener { _, _ ->
                if(checkedChipId == -1) return@setOnCheckedStateChangeListener

                for(i in 0 until this.childCount) {
                    this[i].isClickable = true
                }
                this[checkedChipId].isClickable = false
                requestAPI(checkedChipId)
            }
        }
    }

    private fun observeFilterExercises() {
        viewModel.exerciseFilters.observe(viewLifecycleOwner) {
            addChips(it)
        }
    }

    private fun addChips(items : MutableList<ResponseExercises.ExercisesList>) {
        binding.fgAroundChipgroupExerciseFilter.apply {
            for(i in getChipList(items)) {
                this.addView(i)
            }
            this[0].id.apply {
                check(this)
                requestAPI(this)
            }
        }
    }

    private fun getChipList(items : MutableList<ResponseExercises.ExercisesList>) : List<Chip> {
        return mutableListOf<Chip>().apply {
            for(item in items) {
                add(Chip(requireContext()).apply {
                    id = item.id
                    text = item.name
                    setChipStyles()
                })
            }
            add(0, Chip(requireContext()).apply {
                id = 0
                text = "전체"
                setChipStyles()
            })
        }
    }

    private fun Chip.setChipStyles() {
        this.apply {
            setTextAppearance(R.style.Certificate_Chip_Text_Style)
            setChipBackgroundColorResource(R.color.selector_bg_chip_selected)
            setChipStrokeColorResource(R.color.selector_stroke_chip_selected)
            chipStrokeWidth = 0.5F
            isCheckable = true
            isCheckedIconVisible = false
        }
    }

    private fun requestAPI(checkedId : Int) {
        viewModel.setSelectedFilter(checkedId)
        observeSelectedFilter()
    }

    private fun observeSelectedFilter() {
        viewModel.selectedFilter.observe(viewLifecycleOwner) {
            viewModel.requestLocationData(it)
        }
    }
}