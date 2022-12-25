package ru.netology.mapmarkers.adapter

import android.system.Os.remove
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.mapmarkers.R
import ru.netology.mapmarkers.databinding.CardPointBinding
import ru.netology.mapmarkers.dto.Point


interface OnInteractionListener {
        fun onRemoveListener(point: Point) {}
        fun onEditListener(point: Point) {}
        fun onPoint(point: Point)
    }

class PointsAdapter (
        private val listener: OnInteractionListener
    ) : ListAdapter<Point, PointViewHolder>(PostDiffCallback()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
            //    val inflater = LayoutInflater.from(parent.context)
            val binding = CardPointBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PointViewHolder(binding, listener)
        }

        override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
            val post = getItem(position)
            holder.bind(post)
        }
    }

    class PointViewHolder(
        private val binding: CardPointBinding,
        private val listener: OnInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(point: Point) {
            binding.apply {
                name.text = point.name
                content.text = point.content
                thisPoint.setOnClickListener { listener.onPoint(point) }
                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_point)

                        setOnMenuItemClickListener { item ->
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
                    }.show()
                }
            }
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Point>() {
        override fun areItemsTheSame(oldItem: Point, newItem: Point): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Point, newItem: Point): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: Point, newItem: Point): Any = Unit

    }