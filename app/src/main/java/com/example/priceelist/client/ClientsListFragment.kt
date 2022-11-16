package com.example.priceelist.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.priceelist.PricelistApplication
import com.example.priceelist.data.Client
import com.example.priceelist.databinding.FragmentClientsListBinding

/**
 * A fragment representing a list of Items.
 */
class ClientsListFragment : Fragment() {

    private var _binding: FragmentClientsListBinding? = null
    private val binding get() = _binding!!

    val viewModel: ClientViewModel by activityViewModels {
        ClientViewModelFactory(
            (activity?.application as PricelistApplication).database.clientDao()
        )
    }

    lateinit var item: Client

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClientsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ClientsListAdapter({
            val action =
                ClientsListFragmentDirections.actionClientsListFragmentToAddClientFragment(it.id)
            this.findNavController().navigate(action)
        },
            {
                val action =
                    ClientsListFragmentDirections.actionClientsListFragmentToClientMenuDialogFragment(
                        it.id
                    )
                this.findNavController().navigate(action)
            })

        binding.recyclerView.adapter = adapter
        viewModel.allClients.observe(this.viewLifecycleOwner) { clients ->
            clients.let {
                adapter.submitList(it)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.setHasFixedSize(true)

        viewModel.getLastClient().observe(this.viewLifecycleOwner) {client ->
            if (client == null) {
                binding.emptyView.visibility = View.VISIBLE
            } else {
                binding.emptyView.visibility = View.GONE
            }
        }

        binding.fab.setOnClickListener {
            val action = ClientsListFragmentDirections.actionClientsListFragmentToAddClientFragment()
            this.findNavController().navigate(action)
        }
    }


}