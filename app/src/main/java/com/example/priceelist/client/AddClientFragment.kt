package com.example.priceelist.client

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.priceelist.PricelistApplication
import com.example.priceelist.data.Client
import com.example.priceelist.databinding.FragmentAddClientBinding
import com.google.android.material.snackbar.Snackbar

//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class AddClientFragment : Fragment() {
    //private var param1: String? = null
    //private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }*/
    }

    /**
     * Core part begins...
     */

    private val viewModel: ClientViewModel by activityViewModels {
        ClientViewModelFactory(
            (activity?.application as PricelistApplication).database.clientDao()
        )
    }

    lateinit var item: Client
    private val navigationArgs: AddClientFragmentArgs by navArgs()

    private var _binding: FragmentAddClientBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.clientId

        if (id > 0) {
            viewModel.retrieveClient(id).observe(this.viewLifecycleOwner) {selectedClient ->
                item = selectedClient
                bindToFields(item)
            }
        } else {
            binding.save.setOnClickListener {
                showSnackBar()
                saveNewClient()
            }
        }

        binding.backButton.setOnClickListener {
            val action = AddClientFragmentDirections.actionAddClientFragmentToClientsListFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun bindToFields(item: Client) {
        binding.apply {
            clientName.setText(item.clientName, TextView.BufferType.SPANNABLE)
            clientPhone.setText(item.clientPhone, TextView.BufferType.SPANNABLE)
            clientEmail.setText(item.clientMail, TextView.BufferType.SPANNABLE)
            clientAddress.setText(item.clientAddress, TextView.BufferType.SPANNABLE)

            binding.save.setOnClickListener {
                showSnackBar()
                updateClient()
            }
        }
    }

    private fun updateClient() {
        if (isSaveClientPossible()) {
            viewModel.updateClient(
                this.navigationArgs.clientId,
                binding.clientName.text.toString(), binding.clientPhone.text.toString(),
                binding.clientEmail.text.toString(), binding.clientAddress.text.toString()
            )
            val action = AddClientFragmentDirections.actionAddClientFragmentToClientsListFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun isSaveClientPossible(): Boolean {
        return viewModel.isClientSavePossible(
            binding.clientName.text.toString(),
        binding.clientAddress.text.toString(),
            binding.clientPhone.text.toString()
        )
    }

    private fun showSnackBar() {
        when {
            binding.clientName.text.toString().isBlank() -> {
                Snackbar.make(binding.coordinatorLayout, "Client name is required", Snackbar.LENGTH_LONG).show()
            }
            binding.clientPhone.text.toString().isBlank() -> {
                Snackbar.make(binding.coordinatorLayout, "Client phone number is required", Snackbar.LENGTH_LONG).show()
            }
            binding.clientAddress.text.toString().isBlank() -> {
                Snackbar.make(binding.coordinatorLayout, "Client address is required", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun saveNewClient() {
        if (isSaveClientPossible()) {
            viewModel.addNewClient(
                binding.clientName.text.toString(), binding.clientPhone.text.toString(),
                binding.clientEmail.text.toString(), binding.clientAddress.text.toString()
            )
            val action = AddClientFragmentDirections.actionAddClientFragmentToClientsListFragment()
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}