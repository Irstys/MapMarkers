package ru.netology.mapmarkers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import ru.netology.mapmarkers.R
import ru.netology.mapmarkers.adapter.OnInteractionListener
import ru.netology.mapmarkers.adapter.PointsAdapter
import ru.netology.mapmarkers.databinding.FragmentCardPointBinding
import ru.netology.mapmarkers.dto.PlacePoint
import ru.netology.mapmarkers.ui.FeedFragment.Companion.idArg
import ru.netology.mapmarkers.viewModel.MapsViewModel


class CardPointFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val binding = FragmentCardPointBinding.inflate(
                inflater,
                container,
                false
            )
            val id = arguments?.idArg

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

                override suspend fun onRemoveListener(place: PlacePoint) {
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

            return binding.root
        }
}