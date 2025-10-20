package com.goomer.ps.legacy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goomer.ps.databinding.ItemMenuBinding
import com.goomer.ps.domain.model.MenuItem

/**
 * @param listener Callback para quando um item é clicado
 */
class MenuAdapter(
    private val listener: OnItemClickListener?
) : ListAdapter<MenuItem, MenuAdapter.MenuViewHolder>(MenuItemDiffCallback()) {

    interface OnItemClickListener {
        fun onItemClick(item: MenuItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    class MenuViewHolder(
        private val binding: ItemMenuBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MenuItem, listener: OnItemClickListener?) {
            binding.apply {
                tvName.text = item.name
                tvDescription.text = item.description
                tvPrice.text = root.context.getString(
                    com.goomer.ps.R.string.price,
                    item.price
                )

                root.setOnClickListener {
                    listener?.onItemClick(item)
                }
            }
        }
    }

    private class MenuItemDiffCallback : DiffUtil.ItemCallback<MenuItem>() {

        override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean = oldItem == newItem
    }
}
