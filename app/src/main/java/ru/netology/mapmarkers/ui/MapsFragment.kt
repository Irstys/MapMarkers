package ru.netology.mapmarkers.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.ui_view.ViewProvider
import kotlinx.coroutines.flow.collectLatest
import ru.netology.mapmarkers.R
import ru.netology.mapmarkers.databinding.FragmentMapsBinding
import ru.netology.mapmarkers.databinding.PlacePointBinding
import ru.netology.mapmarkers.viewModel.MapsViewModel
import androidx.navigation.fragment.findNavController
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import ru.netology.mapmarkers.util.attachToLifecycle
import ru.netology.mapmarkers.util.drawPlacemark
import ru.netology.mapmarkers.util.getUserLocation
import ru.netology.mapmarkers.util.moveToLocation


class MapsFragment  : Fragment() {
    companion object {
        const val LAT_KEY = "LAT_KEY"
        const val LONG_KEY = "LONG_KEY"
    }
    private var mapView: MapView? = null
    private var defaultCameraLocation = Point(59.945933, 30.320045)
    private lateinit var userLocation: UserLocationLayer
    private lateinit var mapKit: MapKit
    private lateinit var mapObjects: MapObjectCollection
    private lateinit var usLocation: Point
    private var isMoveToPoint = false

    private val listener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) = Unit

        override fun onMapLongTap(map: Map, point: Point) {
            EditPointDialog.newInstance(point.latitude, point.longitude)
                .show(childFragmentManager, null)
        }

    }
    private val locationObjectListener = object : UserLocationObjectListener {
        override fun onObjectAdded(view: UserLocationView) = Unit

        override fun onObjectRemoved(view: UserLocationView) = Unit

        override fun onObjectUpdated(view: UserLocationView, event: ObjectEvent) {
            userLocation.cameraPosition()?.target?.let {
                mapView?.map?.move(CameraPosition(it, 10F, 0F, 0F))
            }
            userLocation.setObjectListener(null)
        }
    }
    private val viewModel by viewModels<MapsViewModel>()

    private val placeTapListener = MapObjectTapListener { mapObject, _ ->
        viewModel.deletePlaceById(mapObject.userData as Long)
        true
    }
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    userLocation.isVisible = true
                    userLocation.isHeadingEnabled = false
                    userLocation.cameraPosition()?.target?.also {
                        val map = mapView?.map ?: return@registerForActivityResult
                        val cameraPosition = map.cameraPosition
                        map.move(
                            CameraPosition(
                                it,
                                cameraPosition.zoom,
                                cameraPosition.azimuth,
                                cameraPosition.tilt,
                            )
                        )
                    }
                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.need_permission),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMapsBinding.inflate(inflater, container, false)

        mapView = binding.map.apply {
            userLocation = MapKitFactory.getInstance().createUserLocationLayer(mapWindow)
            if (requireActivity()
                    .checkSelfPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                userLocation.isVisible = true
                userLocation.isHeadingEnabled = false
            }

            map.addInputListener(listener)

            val collection = map.mapObjects.addCollection()
            viewLifecycleOwner.lifecycle.coroutineScope.launchWhenStarted {
                viewModel.places.collectLatest { places ->
                    collection.clear()
                    places.forEach { place ->
                        val placeBinding = PlacePointBinding.inflate(layoutInflater)
                        placeBinding.title.text = place.name
                        collection.addPlacemark(
                            Point(place.latitude, place.longitude),
                            ViewProvider(placeBinding.root)
                        ).apply {
                            userData = place.id
                        }
                    }
                }
            }
            collection.addTapListener(placeTapListener)

            // Переход к точке на карте после клика на списке
            val arguments = arguments
            if (arguments != null &&
                arguments.containsKey(LAT_KEY) &&
                arguments.containsKey(LONG_KEY)
            ) {
                val cameraPosition = map.cameraPosition
                map.move(
                    CameraPosition(
                        Point(arguments.getDouble(LAT_KEY), arguments.getDouble(LONG_KEY)),
                        10F,
                        cameraPosition.azimuth,
                        cameraPosition.tilt,
                    )
                )
                arguments.remove(LAT_KEY)
                arguments.remove(LONG_KEY)
            } else {
                // При входе в приложение показываем текущее местоположение
                userLocation.setObjectListener(locationObjectListener)
            }
        }

        binding.plus.setOnClickListener {
            binding.map.map.move(
                CameraPosition(
                    binding.map.map.cameraPosition.target,
                    binding.map.map.cameraPosition.zoom + 1, 0.0f, 0.0f
                ),
                Animation(Animation.Type.SMOOTH, 1F),
                null
            )
        }

        binding.minus.setOnClickListener {
            binding.map.map.move(
                CameraPosition(
                    binding.map.map.cameraPosition.target,
                    binding.map.map.cameraPosition.zoom - 1, 0.0f, 0.0f
                ),
                Animation(Animation.Type.SMOOTH, 1F),
                null,
            )
        }

        binding.location.setOnClickListener {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.map_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                if (menuItem.itemId == R.id.list) {
                    findNavController().navigate(R.id.action_mapsFragment_to_cardPointFragment)
                    true
                } else {
                    false
                }

        }, viewLifecycleOwner)

        getUserLocation(defaultCameraLocation, this).observe(viewLifecycleOwner) {
            usLocation = it
            if (!isMoveToPoint) {
                moveToLocation(mapView!!, usLocation)
            }
        }
        binding.location.setOnClickListener {
            moveToLocation(mapView!!, usLocation)
        }
        userLocation = mapKit.createUserLocationLayer(mapView!!.mapWindow).apply {
            isVisible = true
        }
        viewModel.data.observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {
                mapView!!.map.mapObjects.clear()
                mapObjects = mapView!!.map.mapObjects.addCollection()
                data.forEach { marker ->
                    val point = Point(marker.latitude, marker.longitude)
                    drawPlacemark(point, mapObjects)
                }
            }
        }

        mapView!!.attachToLifecycle(viewLifecycleOwner)
        return binding.root
    }

    override fun onStart() {
        MapKitFactory.getInstance().onStart()
        super.onStart()
        mapView?.onStart()

    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
        mapView?.onStop()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView = null
    }
}