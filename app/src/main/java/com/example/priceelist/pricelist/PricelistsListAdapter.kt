package com.example.priceelist.pricelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.priceelist.data.Pricelist
import com.example.priceelist.data.getFormattedPrice
import com.example.priceelist.databinding.ListsItemBinding

class PricelistsListAdapter(private val onItemClicked: (Pricelist) -> Unit, private val onItemLongClicked: (Pricelist) -> Unit):
    ListAdapter<Pricelist, PricelistsListAdapter.PricelistViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PricelistViewHolder {
        return PricelistViewHolder (
            ListsItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(
        holder: PricelistViewHolder,
        position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClicked(current)
            true
        }
        holder.bind(current)
    }

    class PricelistViewHolder(private var binding: ListsItemBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind (item: Pricelist) {
                binding.listTitle.text = item.dbListTitle
                binding.listTotal.text = item.getFormattedPrice()
                binding.date.text = item.date
                binding.listType.text = item.listType
            }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Pricelist>() {
            override fun areContentsTheSame(oldItem: Pricelist, newItem: Pricelist): Boolean {
                return (oldItem.dbListTitle == newItem.dbListTitle && oldItem.dbTotal == newItem.dbTotal)
            }

            override fun areItemsTheSame(oldItem: Pricelist, newItem: Pricelist): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}