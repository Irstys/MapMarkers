package ru.netology.mapmarkers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.mapmarkers.R
import ru.netology.mapmarkers.databinding.CardPointBinding
import ru.netology.mapmarkers.dto.PlacePoint


interface OnInteractionListener {
        fun onRemoveListener(point: PlacePoint) {}
        fun onEditListener(point: PlacePoint) {}
        fun onClickPoint(point: PlacePoint)
    }

class PointsAdapter (
        private val listener: OnInteractionListener
    ) : ListAdapter<PlacePoint, PointViewHolder>(PostDiffCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
            val binding = CardPointBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val holder = PointViewHolder(binding)
            with(binding) {
                root.setOnClickListener {
                    val point = getItem(holder.adapterPosition)
                    listener.onClickPoint(point)
                }
                menu.setOnClickListener {
                    PopupMenu(root.context, it).apply {
                        inflate(R.menu.options_point)

                        setOnMenuItemClickListener { item ->
                            val point = getItem(holder.adapterPosition)
                            when (item.itemId) {
                                R.id.remove -> {
                                    listener.onRemoveListener(point)
                                    true
                                }
                                R.id.edit -> {
                                    listener.onEditListener(point)
                                    true
                                }
                                else -> false
                            }
                        }

                        show()
                    }
                }
            }

            return holder
        }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
class PointViewHolder(
        private val binding: CardPointBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(point: PlacePoint) {
            binding.apply {
                name.text = point.name
                content.text = point.content

            }
        }
}
class PostDiffCallback : DiffUtil.ItemCallback<PlacePoint>() {
        override fun areItemsTheSame(oldItem: PlacePoint, newItem: PlacePoint): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlacePoint, newItem: PlacePoint): Boolean {
            return oldItem == newItem
        }

}