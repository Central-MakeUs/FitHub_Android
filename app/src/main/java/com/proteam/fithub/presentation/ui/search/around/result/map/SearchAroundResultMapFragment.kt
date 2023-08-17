package com.proteam.fithub.presentation.ui.search.around.result.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.proteam.fithub.R
import com.proteam.fithub.databinding.FragmentSearchAroundResultMapBinding
import com.proteam.fithub.presentation.ui.main.around.AroundFragment
import com.proteam.fithub.presentation.ui.main.around.viewmodel.AroundViewModel
import com.proteam.fithub.presentation.ui.search.around.viewmodel.AroundSearchViewModel
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class SearchAroundResultMapFragment : Fragment(){
    private lateinit var binding : FragmentSearchAroundResultMapBinding
    private val searchViewModel : AroundSearchViewModel by activityViewModels()
    private val aroundViewModel : AroundViewModel by activityViewModels()

    private var mapView : MapView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_around_result_map, container ,false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initMapView()
    }

    private fun initMapView() {
        mapView = MapView(requireActivity())
        binding.fgAroundContainerMap.addView(mapView)
        //updateLocation()
        //mapView.setMapViewEventListener(this)
        //eventListener = AroundFragment.MarkerEventListener()
        mapView?.setZoomLevel(3, true)
        //mapView.setPOIItemEventListener(eventListener)
        /*mapView?.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(
            if(tag!!.contains("AROUND")) aroundViewModel.currentLocation.value!!.latitude else searchViewModel.currentMarkerItems.value!![0].y.toDouble(),
            if(tag!!.contains("AROUND")) aroundViewModel.currentLocation.value!!.longitude else searchViewModel.currentMarkerItems.value!![0].x.toDouble(),
        ), false) */
    }

    override fun onStop() {
        super.onStop()
        binding.fgAroundContainerMap.removeView(mapView)
        mapView = null
    }
}