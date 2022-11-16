package com.example.priceelist.pricelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.priceelist.PricelistApplication
import com.example.priceelist.R
import com.example.priceelist.data.Pricelist
import com.example.priceelist.data.getFormattedPrice
import com.example.priceelist.databinding.FragmentMenuDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MenuDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: PricelistViewModel by activityViewModels {
        PricelistViewModelFactory(
            (activity?.application as PricelistApplication).database.pricelistDao()
        )
    }

    private var _binding: FragmentMenuDialogBinding? = null
    private val navigationArgs: MenuDialogFragmentArgs by navArgs()
    lateinit var item: Pricelist

    private val binding get() = _binding!!

    private fun bind(item: Pricelist) {
        binding.apply {
            binding.listTitle.text = item.dbListTitle
            binding.listTotal.text = item.getFormattedPrice()

            menuCopy.setOnClickListener { addCopiedList(item) }

            menuDelete.setOnClickListener { showConfirmationDialog() }

            menuDownload.setOnClickListener {
                val action = MenuDialogFragmentDirections.actionMenuDialogFragmentToShareDownloadListFragment(
                    item.id, "download"
                )
                findNavController().navigate(action)
            }

            menuShare.setOnClickListener {
                val action = MenuDialogFragmentDirections.actionMenuDialogFragmentToShareDownloadListFragment(
                    item.id, "share"
                )
                findNavController().navigate(action)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMenuDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.itemId

        viewModel.retrieveList(id).observe(this.viewLifecycleOwner) {selectedItem ->
            /**
             * condition was added as a solution to an app crash issue: Java Null Pointer Exception...
             * ... which crashed the app stating that selectedItem must not be null.
             */
            if (selectedItem == null) {
                dismiss()
            } else {
                item = selectedItem
                bind(item)
            }
        }
    }

    private fun addCopiedList(item: Pricelist) {
        viewModel.addnewList(
            item.dbListTitle, item.date,
            item.dbItem1.toString(), item.dbQuantity1.toString(), item.dbAmount1.toString(),
            item.dbItem2.toString(), item.dbQuantity2.toString(), item.dbAmount2.toString(),
            item.dbItem3.toString(), item.dbQuantity3.toString(), item.dbAmount3.toString(),
            item.dbItem4.toString(), item.dbQuantity4.toString(), item.dbAmount4.toString(),
            item.dbItem5.toString(), item.dbQuantity5.toString(), item.dbAmount5.toString(),
            item.dbItem6.toString(), item.dbQuantity6.toString(), item.dbAmount6.toString(),
            item.dbItem7.toString(), item.dbQuantity7.toString(), item.dbAmount7.toString(),
            item.dbItem8.toString(), item.dbQuantity8.toString(), item.dbAmount8.toString(),
            item.dbItem9.toString(), item.dbQuantity9.toString(), item.dbAmount9.toString(),
            item.dbItem10.toString(), item.dbQuantity10.toString(), item.dbAmount10.toString(),
            item.dbItem11.toString(), item.dbQuantity11.toString(), item.dbAmount11.toString(),
            item.dbItem12.toString(), item.dbQuantity12.toString(), item.dbAmount12.toString(),
            item.dbItem13.toString(), item.dbQuantity13.toString(), item.dbAmount13.toString(),
            item.dbItem14.toString(), item.dbQuantity14.toString(), item.dbAmount14.toString(),
            item.dbItem15.toString(), item.dbQuantity15.toString(), item.dbAmount15.toString(), item.dbTotal.toString(),
            item.listType, item.note
        )
    }

    private fun deleteList() {
        viewModel.deleteList(item)
        findNavController().navigateUp()
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ ->}
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteList()
            }
            .show()
    }

    override fun dismiss() {
        super.dismiss()
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}