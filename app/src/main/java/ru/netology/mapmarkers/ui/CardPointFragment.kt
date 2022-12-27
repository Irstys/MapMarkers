package ru.netology.mapmarkers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import ru.netology.mapmarkers.R
import ru.netology.mapmarkers.adapter.OnInteractionListener
import ru.netology.mapmarkers.adapter.PointsAdapter
import ru.netology.mapmarkers.databinding.PlacesFragmentBinding
import ru.netology.mapmarkers.dto.PlacePoint
import ru.netology.mapmarkers.viewModel.MapsViewModel


const val KEY_CAMERA_POSITION = "camera_position"
const val KEY_CAMERA_POSITION_LATITUDE = "camera_position_latitude"
const val KEY_CAMERA_POSITION_LONGITUDE = "camera_position_longitude"
class CardPointFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val binding = PlacesFragmentBinding.inflate(
                inflater,
                container,
                false
            )

            val viewModel by viewModels<MapsViewModel>()

            val adapter = PointsAdapter(object : OnInteractionListener {

                override fun onClickPoint(place: PlacePoint) {
                    findNavController().navigate(
                        R.id.action_cardPointFragment_to_mapsFragment, bundleOf(
                            MapsFragment.LAT_KEY to place.latitude,
                            MapsFragment.LONG_KEY to place.longitude
                        )
                    )
                }

                override fun onCameraPosition(latitude: Double, longitude: Double) {
                    setFragmentResult(
                       KEY_CAMERA_POSITION,
                        bundleOf(
                            KEY_CAMERA_POSITION_LATITUDE to latitude,
                            KEY_CAMERA_POSITION_LONGITUDE to longitude
                        )
                    )
                    findNavController().navigate(R.id.action_cardPointFragment_to_mapsFragment)         }


                override fun onRemoveListener(place: PlacePoint) {
                    viewModel.deletePlaceById(place.id)
                }

                override fun onEditListener(place: PlacePoint) {
                    EditPointDialog.newInstance(lat = place.latitude, long = place.longitude, id = place.id)
                        .show(childFragmentManager, null)
                }
            })

            binding.list.adapter = adapter

            viewLifecycleOwner.lifecycle.coroutineScope.launchWhenStarted {
                viewModel.places.collectLatest { places ->
                    adapter.submitList(places)
                    binding.empty.isVisible = places.isEmpty()
                }
            }
            val observe = viewModel.data.observe(viewLifecycleOwner) { places ->

                val newPoint = adapter.itemCount < places.size
                adapter.submitList(places)
                if (newPoint) {
                    binding.list.smoothScrollToPosition(0)
                }

            }

            return binding.root
        }
}
