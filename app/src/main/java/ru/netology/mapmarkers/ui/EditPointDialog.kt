package ru.netology.mapmarkers.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import ru.netology.mapmarkers.R
import ru.netology.mapmarkers.dto.PlacePoint
import ru.netology.mapmarkers.viewModel.MapsViewModel

class EditPointDialog: DialogFragment() {
    companion object {
        private const val ID_KEY = "ID_KEY"
        private const val LAT_KEY = "LAT_KEY"
        private const val LONG_KEY = "LONG_KEY"
        fun newInstance(lat: Double, long: Double, id: Long? = null) = EditPointDialog().apply {
            arguments = bundleOf(LAT_KEY to lat, LONG_KEY to long, ID_KEY to id)
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val viewModel by viewModels<MapsViewModel>()
        val view = AppCompatEditText(requireContext())
        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setTitle(getString(R.string.name))
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val text = view.text?.toString()?.takeIf { it.isNotBlank() } ?: run {
                    Toast.makeText(requireContext(), "Name is empty", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                viewModel.insertPlace(
                    PlacePoint(
                        id = requireArguments().getSerializable(ID_KEY) as? Long ?: 0,
                        name = text,
                        latitude = requireArguments().getDouble(LAT_KEY),
                        longitude = requireArguments().getDouble(LONG_KEY),
                    )
                )
            }
            .create()
    }
}