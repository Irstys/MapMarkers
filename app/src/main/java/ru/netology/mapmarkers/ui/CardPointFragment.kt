package ru.netology.mapmarkers.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.mapmarkers.R
import ru.netology.mapmarkers.databinding.FragmentCardPointBinding
import ru.netology.mapmarkers.ui.FeedFragment.Companion.idArg
import ru.netology.mapmarkers.viewModel.PointViewModel
import kotlin.reflect.KProperty

@Suppress("DEPRECATION")
@AndroidEntryPoint
class CardPointFragment : Fragment() {
        private val viewModel: PointViewModel by viewModels(
            ownerProducer = ::requireParentFragment
        )

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

            viewModel.data.observe(viewLifecycleOwner) { posts ->
                binding.postLayout.apply {
                    posts.map { point ->
                        if (point.id.toInt() == id) {
                            name.text = point.name
                            content.text = point.content
                            menu.setOnClickListener {
                                PopupMenu(it.context, it).apply {
                                    inflate(R.menu.options_point)

                                    setOnMenuItemClickListener { item ->
                                        when (item.itemId) {
                                            R.id.remove -> {
                                                viewModel.removeById(point.id)
                                                findNavController().navigateUp()
                                                true
                                            }
                                            R.id.edit -> {
                                                viewModel.edit(point)
                                                findNavController().navigate(
                                                    R.id.action_cardPointFragment_to_editPointFragment,
                                                    Bundle().apply {
                                                        textArg = point.content
                                                    }
                                                )
                                                true
                                            }

                                            else -> {
                                                false
                                            }
                                        }
                                    }
                                }.show()
                            }

                        }
                    }

                }
            }

            return binding.root
        }

    companion object {

        private const val TEXT_KEY = "TEXT_KEY"
        var Bundle.textArg: String?
            set(value) = putString(TEXT_KEY, value)
            get() = getString(TEXT_KEY)
    }

}