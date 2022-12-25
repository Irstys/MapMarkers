package ru.netology.mapmarkers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.mapmarkers.adapter.OnInteractionListener
import ru.netology.mapmarkers.adapter.PointsAdapter
import ru.netology.mapmarkers.databinding.FragmentFeedBinding
import ru.netology.mapmarkers.viewModel.PointViewModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import androidx.navigation.fragment.findNavController
import ru.netology.mapmarkers.R
import ru.netology.mapmarkers.dto.Point
import ru.netology.mapmarkers.ui.CardPointFragment.Companion.textArg

class FeedFragment : Fragment() {
    internal val viewModel: PointViewModel by viewModels(ownerProducer = ::requireParentFragment)
private val binding by lazy {
    FragmentFeedBinding.inflate(layoutInflater)
}

override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View {
    val binding = FragmentFeedBinding.inflate(
        inflater,
        container,
        false
    )
    val adapter = PointsAdapter(object : OnInteractionListener {

        override fun onRemoveListener(point: Point) {
            viewModel.removeById(point.id)
        }

        override fun onEditListener(point: Point) {
            viewModel.edit(point)
            findNavController().navigate(
                R.id.action_feedFragment_to_editPointFragment,
                Bundle().apply {
                    textArg = point.content
                }
            )
        }

        override fun onPoint(point: Point) {
            findNavController().navigate(
                R.id.action_feedFragment_to_cardPointFragment,
                Bundle().apply {
                    idArg = point.id.toInt()
                }
            )
        }
    })
    binding.list.adapter = adapter
    viewModel.data.observe(viewLifecycleOwner) { posts ->
        val newPost = adapter.itemCount < posts.size
        adapter.submitList(posts) {
            if (newPost) binding.list.smoothScrollToPosition(0)
        }
    }
    binding.retryButton.setOnClickListener {
        viewModel.loadPoints()
    }
    binding.swipeRefresh.setOnRefreshListener {
        viewModel.refreshPoints()
    }

    binding.addPoint.setOnClickListener {
        findNavController().navigate(R.id.action_feedFragment_to_editPointFragment)
    }

    return binding.root
}

companion object {
    var Bundle.idArg: Int by IntArg
}

object IntArg : ReadWriteProperty<Bundle, Int> {
    override fun getValue(thisRef: Bundle, property: KProperty<*>): Int {
        return thisRef.getInt(property.name)
    }

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Int) {
        thisRef.putInt(property.name, value)
    }
}
}
