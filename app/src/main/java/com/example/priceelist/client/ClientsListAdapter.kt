package com.example.priceelist.client

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.priceelist.data.Client
import com.example.priceelist.databinding.ClientsListItemBinding

class ClientsListAdapter(private val onItemClicked: (Client) -> Unit, private val onItemLongClicked: (Client) -> Unit):
     ListAdapter<Client, ClientsListAdapter.ClientsViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClientsViewHolder {
        return ClientsViewHolder(
            ClientsListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(
        holder: ClientsViewHolder,
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

    class ClientsViewHolder(private var binding: ClientsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Client) {
            binding.nameInitial.text = getNameInitial(item.clientName).toString()
            binding.clientName.text = item.clientName
            //binding.clientAddress.text = item.clientAddress
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Client>() {
            override fun areContentsTheSame(oldItem: Client, newItem: Client): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: Client, newItem: Client): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }


}

fun getNameInitial(name: String): Char {
    return name.first()
}

