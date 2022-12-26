package ru.netology.mapmarkers.ui

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.mapmarkers.R
import ru.netology.mapmarkers.databinding.FragmentEditPointBinding
import ru.netology.mapmarkers.util.AndroidUtils
import ru.netology.mapmarkers.viewModel.PointViewModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class EditPointFragment: Fragment() {

    private val viewModel: PointViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private var fragmentBinding: FragmentEditPointBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditPointBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.textArg
            ?.let { binding.edit.setText(it) }

        binding.edit.requestFocus()

        binding.edit.setText(viewModel.edited.value?.content)

        viewModel.pointCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_new_point, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                when (menuItem.itemId) {
                    R.id.save -> {
                        fragmentBinding?.let {
                            viewModel.run {
                                changeContent(it.edit.text.toString())
                                this.save()
                            }
                            AndroidUtils.hideKeyboard(requireView())
                        }
                        true
                    }
                    else -> false
                }

        }, viewLifecycleOwner)

        return binding.root
    }
    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }


    companion object {
        var Bundle.textArg: String? by StringArg
    }

    object StringArg : ReadWriteProperty<Bundle, String?> {
        override fun getValue(thisRef: Bundle, property: KProperty<*>): String? {
            return thisRef.getString(property.name)
        }

        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) {
            thisRef.putString(property.name, value)
        }
    }
}
