package com.example.priceelist.sharedfragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.priceelist.PricelistApplication
import com.example.priceelist.client.ClientViewModel
import com.example.priceelist.client.ClientViewModelFactory
import com.example.priceelist.data.Client
import com.example.priceelist.databinding.FragmentSavedClientsMenuDialogBinding
import com.example.priceelist.databinding.SavedClientsMenuItemBinding
import com.example.priceelist.invoice.InvoiceClientInfoFragmentDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SavedClientsMenuDialog : BottomSheetDialogFragment() {

    private var _binding: FragmentSavedClientsMenuDialogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: ClientViewModel by activityViewModels {
        ClientViewModelFactory(
            (activity?.application as PricelistApplication).database.clientDao()
        )
    }

    lateinit var item: Client
    private val navigationArgs: SavedClientsMenuDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedClientsMenuDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (navigationArgs.invoiceId > 0) {
            val adapter = SavedClientsListAdapter {
                val action =
                    SavedClientsMenuDialogDirections.actionSavedClientsMenuDialogToInvoiceNewClientFragment(
                        navigationArgs.invoiceId,
                        navigationArgs.listType,
                        it.id
                    )
                this.findNavController().navigate(action)
            }

            binding.list.adapter = adapter
            viewModel.allClients.observe(this.viewLifecycleOwner) { clients ->
                clients.let {
                    adapter.submitList(it)
                }
            }
        }
        else {
            val adapter = SavedClientsListAdapter {
                val action =
                    SavedClientsMenuDialogDirections.actionSavedClientsMenuDialogToInvoiceClientInfoFragment(
                        navigationArgs.listType,
                        it.id
                    )
                this.findNavController().navigate(action)
            }

            binding.list.adapter = adapter
            viewModel.allClients.observe(this.viewLifecycleOwner) { clients ->
                clients.let {
                    adapter.submitList(it)
                }
            }
        }

        binding.list.layoutManager = LinearLayoutManager(this.context)
        binding.list.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private class SavedClientsListAdapter(private val onItemClicked: (Client) -> Unit) :
    ListAdapter<Client, SavedClientsListAdapter.SavedClientsViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedClientsViewHolder {
        return SavedClientsViewHolder(
            SavedClientsMenuItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: SavedClientsViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }


    class SavedClientsViewHolder(private var binding: SavedClientsMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Client) {
            binding.nameInitial.text = getNameInitial(item.clientName).toString()
            binding.clientName.text = item.clientName
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

private fun getNameInitial(name: String): Char {
    return name.first()
}