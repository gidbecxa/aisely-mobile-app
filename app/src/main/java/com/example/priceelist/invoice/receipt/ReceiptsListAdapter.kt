package com.example.priceelist.invoice.receipt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.priceelist.data.Invoice
import com.example.priceelist.databinding.NewReceiptsListItemBinding

class ReceiptsListAdapter(
    private val onItemClicked: (Invoice) -> Unit,
    private val onItemLongClicked: (Invoice) -> Unit
) : ListAdapter<Invoice, ReceiptsListAdapter.ReceiptsListViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReceiptsListViewHolder {
        return ReceiptsListViewHolder(
            NewReceiptsListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(
        holder: ReceiptsListViewHolder,
        position: Int
    ) {
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

    class ReceiptsListViewHolder(private var binding: NewReceiptsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Invoice) {
            binding.invoiceNumber.text = item.invoiceNumber
            binding.clientNote.text = item.receiverNote
            binding.date.text = item.date
            binding.listType.text = displayListType(item.listType)
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Invoice>() {
            override fun areItemsTheSame(oldItem: Invoice, newItem: Invoice): Boolean {
                return oldItem.fileName == newItem.fileName
            }

            override fun areContentsTheSame(oldItem: Invoice, newItem: Invoice): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}

private fun displayListType(type: String): String {
    return when (type) {
        "In" -> {
            "In"
        }
        else -> "Re"
    }
}

