package com.example.priceelist.invoice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.priceelist.MainActivity
import com.example.priceelist.PricelistApplication
import com.example.priceelist.R
import com.example.priceelist.account.StatsViewModel
import com.example.priceelist.account.StatsViewModelFactory
import com.example.priceelist.data.Invoice
import com.example.priceelist.data.StatsDao
import com.example.priceelist.databinding.NewInvoicesListItemBinding
import java.text.DateFormat
import java.util.*

class InvoicesListAdapter(
    private val onItemClicked: (Invoice) -> Unit,
    private val onItemLongClicked: (Invoice) -> Unit
) :
    ListAdapter<Invoice, InvoicesListAdapter.InvoicesListViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InvoicesListViewHolder {
        return InvoicesListViewHolder(
            NewInvoicesListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: InvoicesListViewHolder, position: Int) {
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


    class InvoicesListViewHolder(private var binding: NewInvoicesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Invoice) {
            binding.invoiceNumber.text = item.invoiceNumber
            binding.clientNote.text = item.receiverNote
            binding.date.text = item.date
            binding.listType.text = displayListType(item.listType)

            if (item.cleared) {
                binding.clearButton.visibility = View.GONE
                binding.clearButtonActive.visibility = View.VISIBLE
            } else {
                binding.clearButtonActive.visibility = View.GONE
                binding.clearButton.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Invoice>() {
            override fun areItemsTheSame(oldItem: Invoice, newItem: Invoice): Boolean {
                return (oldItem.fileName == newItem.fileName && oldItem.cleared == newItem.cleared)
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

