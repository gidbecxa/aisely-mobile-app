package com.example.priceelist.invoice

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.priceelist.client.ClientViewModel
import com.example.priceelist.client.ClientViewModelFactory
import com.example.priceelist.data.Client
import com.example.priceelist.databinding.FragmentInvoiceClientInfoBinding
import com.google.android.material.snackbar.Snackbar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"


class InvoiceClientInfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    //private var param1: String? = null
    //private var param2: String? = null

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }*/

    private var _binding: FragmentInvoiceClientInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClientViewModel by activityViewModels {
        ClientViewModelFactory(
            (activity?.application as PricelistApplication).database.clientDao()
        )
    }

    lateinit var item: Client
    private val navigationArgs: InvoiceClientInfoFragmentArgs by navArgs()

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
                Snackbar.make(
                    binding.coordinatorLayout,
                    "Client name is required",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            binding.clientPhone.text.toString().isBlank() -> {
                Snackbar.make(
                    binding.coordinatorLayout,
                    "Client phone number is required",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            binding.clientAddress.text.toString().isBlank() -> {
                Snackbar.make(
                    binding.coordinatorLayout,
                    "Client address is required",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun passInfoToNext() {
        if (isSaveClientPossible()) {
            val clientName = binding.clientName.text.toString()
            val clientPhone = binding.clientPhone.text.toString()
            val clientEmail = binding.clientEmail.text.toString()
            val clientAddress = binding.clientAddress.text.toString()
            val listType = navigationArgs.listType

            val action =
                InvoiceClientInfoFragmentDirections.actionInvoiceClientInfoFragmentToInvoicePriceInfoFragment(
                    clientName, clientPhone, clientEmail, clientAddress, listType
                )
            this.findNavController().navigate(action)
        }
    }

    private fun bindToFields(item: Client) {
        binding.apply {
            clientName.setText(item.clientName, TextView.BufferType.SPANNABLE)
            clientPhone.setText(item.clientPhone, TextView.BufferType.SPANNABLE)
            clientEmail.setText(item.clientMail, TextView.BufferType.SPANNABLE)
            clientAddress.setText(item.clientAddress, TextView.BufferType.SPANNABLE)

            binding.nextButton.setOnClickListener {
                showSnackBar()
                passUpdatedInfoToNext()
            }
        }
    }

    private fun passUpdatedInfoToNext() {
        if (isSaveClientPossible()) {
            val clientName = binding.clientName.text.toString()
            val clientPhone = binding.clientPhone.text.toString()
            val clientEmail = binding.clientEmail.text.toString()
            val clientAddress = binding.clientAddress.text.toString()
            val listType = navigationArgs.listType

            val action =
                InvoiceClientInfoFragmentDirections.actionInvoiceClientInfoFragmentToInvoicePriceInfoFragment(
                    clientName, clientPhone, clientEmail, clientAddress, listType, item.id
                )
            this.findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInvoiceClientInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.clientId

        if (id > 0) {
            viewModel.retrieveClient(id).observe(this.viewLifecycleOwner) { selectedClient ->
                item = selectedClient
                bindToFields(item)
            }
        } else {
            binding.nextButton.setOnClickListener {
                showSnackBar()
                passInfoToNext()
            }
        }

        binding.choose.setOnClickListener {
            binding.choose.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                val action =
                    InvoiceClientInfoFragmentDirections.actionInvoiceClientInfoFragmentToSavedClientsMenuDialog(navigationArgs.listType)
                this.findNavController().navigate(action)
                binding.choose.isEnabled = true
            }, 100)
        }

        binding.backButton.setOnClickListener {
            this.findNavController().navigateUp()
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