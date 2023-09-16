package com.proteam.fithub.presentation.ui.main.arounds.map

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.proteam.fithub.R
import com.proteam.fithub.data.remote.response.ResponseLocationData
import com.proteam.fithub.databinding.FragmentAroundsMapBinding
import com.proteam.fithub.presentation.ui.main.arounds.list.AroundListFragment
import com.proteam.fithub.presentation.ui.main.arounds.viewmodel.AroundsViewModel
import com.proteam.fithub.presentation.util.AnalyticsHelper
import com.proteam.fithub.presentation.util.CustomSnackBar
import kotlinx.coroutines.delay
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class AroundsMapFragment : Fragment(), MapView.MapViewEventListener {
    private lateinit var binding: FragmentAroundsMapBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var eventListener: MarkerEventListener

    private val viewModel: AroundsViewModel by activityViewModels()

    private val customSnackbar by lazy {
        CustomSnackBar.makeSnackBar(binding.root, "이 지역은 아직 시설정보가 없어요.")
    }

    private var isDragged = false

    private lateinit var mapView: MapView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_arounds_map, container, false)

        AnalyticsHelper.setAnalyticsLog(this.javaClass.simpleName)

        initBinding()
        initUi()
        updateLocation()
        manageObserve()

        return binding.root
    }

    private fun initBinding() {
        binding.fragment = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun initUi() {
        initMapView()
    }

    private fun initMapView() {
        mapView = MapView(requireActivity())
        binding.fgAroundContainerMap.addView(mapView)
        mapView.setMapViewEventListener(this)
        eventListener = MarkerEventListener()
        mapView.setPOIItemEventListener(eventListener)
    }

    private fun manageObserve() {
        requestLocationWhenInit()
        observeMarkers()
    }

    fun openListFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.fg_arounds_layout_container, AroundListFragment()).commit()
    }

    private fun requestLocationWhenInit() {
        viewModel.currentLocation.observe(viewLifecycleOwner) {
            if (viewModel.isResearchNeed.value == false) {
                requestLocationInfo()
            }
        }
    }

    private fun requestLocationInfo() {
        viewModel.requestLocationDataWithoutKeyword()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.requestLocationUpdates(
            LocationRequest.create().also { it.priority = LocationRequest.PRIORITY_HIGH_ACCURACY },
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let {
                viewModel.setCurrentLocation(it)
                setMapToUserLocation(it)
            }
        }
    }

    private fun setMapToUserLocation(location: Location) {
        if (viewModel.targetLocation.value == null) mapView.setMapCenterPoint(
            MapPoint.mapPointWithGeoCoord(
                location.latitude,
                location.longitude
            ), true
        )
        removeLegacyCurrentMarker()
        mapView.addPOIItem(returnCurrentMarker(location))
    }

    private fun removeLegacyCurrentMarker() {
        for (i in mapView.poiItems) {
            if (i.tag == 0) {
                mapView.removePOIItem(i)
            }
        }
    }

    private fun returnCurrentMarker(location: Location) = MapPOIItem().apply {
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
        viewModel.initTargetLocation()
        viewModel.setSearchNeed(isDragged).also { isDragged = false }
        viewModel.currentLocation.value?.let { setMapToUserLocation(it) }
    }

    fun onResearchClicked() {
        viewModel.setSearchNeed(false)
        viewModel.requestLocationDataWithoutKeyword()
    }

    override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
        viewModel.setTargetLocation(
            Pair(
                p1!!.mapPointGeoCoord?.latitude!!,
                p1.mapPointGeoCoord?.longitude!!
            )
        )
    }

    override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {
        viewModel.setSearchNeed(true)
        isDragged = true
    }

    override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {
        viewModel.setIsCardShowing(false)
    }

    private fun checkIsMapContains() : Boolean = requireActivity().supportFragmentManager.findFragmentById(R.id.fg_arounds_layout_container)?.javaClass.toString().contains("Map")

    /** Location Marker **/
    private fun observeMarkers() {
        viewModel.currentMarkerItems.observe(viewLifecycleOwner) {
            viewModel.setFilteredItems(viewModel.selectedFilter.value ?: 0)
        }

        viewModel.filteredMarkerItems.observe(viewLifecycleOwner) {
            it?.let {
                if(it.isEmpty() && checkIsMapContains() && !customSnackbar.isShown) customSnackbar.show()
                changeMarkers(it)
            }
        }

        viewModel.selectedFilter.observe(viewLifecycleOwner) {
            viewModel.setFilteredItems(it)
        }

        viewModel.selectedMarkerItems.observe(viewLifecycleOwner) {
            mapView.selectPOIItem(mapView.findPOIItemByName(it.name)[0], true)
            viewModel.setIsCardShowing(true)
            it.setCard()
        }
    }

    private fun changeMarkers(markers: MutableList<ResponseLocationData.LocationItems>) {
        removeAllMarkers()
        appendMarkers(markers)
        mapView.fitMapViewAreaToShowMapPoints(markers.getMapPoints())
            .also { if (markers.isNotEmpty()) mapView.zoomOut(true) }
    }

    private fun removeAllMarkers() {
        for (i in mapView.poiItems) {
            if (i.tag != 0) {
                mapView.removePOIItem(i)
            }
        }
    }

    private fun appendMarkers(markers: MutableList<ResponseLocationData.LocationItems>) {
        for (i in markers) {
            mapView.addPOIItem(i.makeMarker())
        }
    }

    private fun MutableList<ResponseLocationData.LocationItems>.getMapPoints(): Array<MapPoint> {
        return arrayListOf<MapPoint>().apply {
            for (i in this@getMapPoints) {
                this.add(MapPoint.mapPointWithGeoCoord(i.y.toDouble(), i.x.toDouble()))
            }
        }.toTypedArray()
    }

    private fun ResponseLocationData.LocationItems.makeMarker(): MapPOIItem {
        return MapPOIItem().apply {
            itemName = name
            tag = 1
            markerType = MapPOIItem.MarkerType.CustomImage
            selectedMarkerType = MapPOIItem.MarkerType.CustomImage
            mapPoint = MapPoint.mapPointWithGeoCoord(y.toDouble(), x.toDouble())
            customImageBitmap = BitmapFactory.decodeResource(requireActivity().resources, R.drawable.ic_around_marker_not_clicked)
            customSelectedImageBitmap = BitmapFactory.decodeResource(requireActivity().resources, R.drawable.ic_around_marker_clicked)
            isCustomImageAutoscale = false
            isShowCalloutBalloonOnTouch = false
            showAnimationType = null
            setCustomImageAnchor(0.5F, 1F)
        }
    }

    private fun ResponseLocationData.LocationItems.setCard() {
        binding.fgAroundComponentLocationCard.getData(this)
    }

    inner class MarkerEventListener : MapView.POIItemEventListener {
        override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
            viewModel.setSelectedItem(viewModel.currentMarkerItems.value?.find { it.name == p1?.itemName }!!)
        }

        override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {}
        override fun onCalloutBalloonOfPOIItemTouched(
            p0: MapView?,
            p1: MapPOIItem?,
            p2: MapPOIItem.CalloutBalloonButtonType?
        ) {
        }

        override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {}
    }

    override fun onMapViewInitialized(p0: MapView?) {}
    override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {}
    override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {}
    override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {}

    override fun onPause() {
        super.onPause()
        removeAllMarkers()
    }
}