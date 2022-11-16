package com.example.priceelist.client

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.priceelist.PricelistApplication
import com.example.priceelist.R
import com.example.priceelist.data.Client
import com.example.priceelist.databinding.FragmentClientMenuDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ClientMenuDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentClientMenuDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClientViewModel by activityViewModels {
        ClientViewModelFactory(
            (activity?.application as PricelistApplication).database.clientDao()
        )
    }

    lateinit var item: Client
    private val navigationArgs: ClientMenuDialogFragmentArgs by navArgs()

    private fun getNameInitial(name: String): Char {
        return name.first()
    }

    private fun deleteClient() {
        viewModel.deleteClient(item)
        findNavController().navigateUp()
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_client_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ ->}
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteClient()
            }
            .show()
    }

    private fun editClient() {
        val action = ClientMenuDialogFragmentDirections.actionClientMenuDialogFragmentToAddClientFragment(item.id)
        this.findNavController().navigate(action)
    }

    private fun shareClientInfo(item: Client) {
        val clientName = item.clientName
        val clientPhone = item.clientPhone
        val clientMail = item.clientMail
        val clientAddress = item.clientAddress
        val textToShare = "Name: $clientName\nContact No: $clientPhone\nEmail: $clientMail\nAddress: $clientAddress"

        val intentToSend: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textToShare)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(intentToSend, "Share")
        activity?.startActivity(shareIntent)
    }

    private fun bind(item: Client) {
        binding.apply {
            binding.nameInitial.text = getNameInitial(item.clientName).toString()
            binding.clientName.text = item.clientName

            menuEditClient.setOnClickListener { editClient() }
            menuDelete.setOnClickListener { showConfirmationDialog() }
            menuShare.setOnClickListener { shareClientInfo(item) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClientMenuDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.clientId

        viewModel.retrieveClient(id).observe(this.viewLifecycleOwner) { selectedItem ->

            if (selectedItem == null) {
                dismiss()
            } else {
                item = selectedItem
                bind(item)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}