package com.example.priceelist.invoice

import android.annotation.SuppressLint
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
import com.example.priceelist.R
import com.example.priceelist.data.Invoice
import com.example.priceelist.databinding.FragmentInvoicePriceInfoBinding
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class InvoicePriceInfoFragment : Fragment() {
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

    private var _binding: FragmentInvoicePriceInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InvoiceViewModel by activityViewModels {
        InvoiceViewModelFactory(
            (activity?.application as PricelistApplication).database.invoiceDao()
        )
    }

    lateinit var item: Invoice
    private val navigationArgs: InvoicePriceInfoFragmentArgs by navArgs()

    private fun isToPreviewPossible(): Boolean {
        return viewModel.isToPreviewPossible(
            binding.item1.text.toString(),
            binding.unit1.text.toString(),
            binding.amount1.text.toString(),
            binding.item2.text.toString(),
            binding.unit2.text.toString(),
            binding.amount2.text.toString(),
            binding.item3.text.toString(),
            binding.unit3.text.toString(),
            binding.amount3.text.toString(),
            binding.item4.text.toString(),
            binding.unit4.text.toString(),
            binding.amount4.text.toString(),
            binding.item5.text.toString(),
            binding.unit5.text.toString(),
            binding.amount5.text.toString(),
            binding.item6.text.toString(),
            binding.unit6.text.toString(),
            binding.amount6.text.toString(),
            binding.item7.text.toString(),
            binding.unit7.text.toString(),
            binding.amount7.text.toString(),
            binding.item8.text.toString(),
            binding.unit8.text.toString(),
            binding.amount8.text.toString(),
            binding.item9.text.toString(),
            binding.unit9.text.toString(),
            binding.amount9.text.toString(),
            binding.item10.text.toString(),
            binding.unit10.text.toString(),
            binding.amount10.text.toString()
        )
    }

    @SuppressLint("ResourceAsColor")
    private fun showSnackBar() {
        if (!isToPreviewPossible()) {
            Snackbar.make(this.requireView(), "Please, enter at least a row", Snackbar.LENGTH_LONG)
                .show()
        }
    }

    private fun passNewInfoToPreview() {
        if (isToPreviewPossible()) {
            val clientName = navigationArgs.clientName
            val clientPhone = navigationArgs.clientPhone
            val clientEmail = navigationArgs.clientEmail
            val clientAddress = navigationArgs.clientAddress
            val clientId = navigationArgs.clientId

            val item1 = binding.item1.text.toString()
            val unit1 = binding.unit1.text.toString()
            val amount1 = binding.amount1.text.toString()
            val item2 = binding.item2.text.toString()
            val unit2 = binding.unit2.text.toString()
            val amount2 = binding.amount2.text.toString()
            val item3 = binding.item3.text.toString()
            val unit3 = binding.unit3.text.toString()
            val amount3 = binding.amount3.text.toString()
            val item4 = binding.item4.text.toString()
            val unit4 = binding.unit4.text.toString()
            val amount4 = binding.amount4.text.toString()
            val item5 = binding.item5.text.toString()
            val unit5 = binding.unit5.text.toString()
            val amount5 = binding.amount5.text.toString()
            val item6 = binding.item6.text.toString()
            val unit6 = binding.unit6.text.toString()
            val amount6 = binding.amount6.text.toString()
            val item7 = binding.item7.text.toString()
            val unit7 = binding.unit7.text.toString()
            val amount7 = binding.amount7.text.toString()
            val item8 = binding.item8.text.toString()
            val unit8 = binding.unit8.text.toString()
            val amount8 = binding.amount8.text.toString()
            val item9 = binding.item9.text.toString()
            val unit9 = binding.unit9.text.toString()
            val amount9 = binding.amount9.text.toString()
            val item10 = binding.item10.text.toString()
            val unit10 = binding.unit10.text.toString()
            val amount10 = binding.amount10.text.toString()

            val listType = navigationArgs.listType
            val invoiceNote = binding.addNoteText.text.toString()

            val action =
                InvoicePriceInfoFragmentDirections.actionInvoicePriceInfoFragmentToInvoicePreviewFragment(
                    clientId, clientName, clientPhone, clientEmail, clientAddress,
                    item1, unit1, amount1, item2, unit2, amount2,
                    item3, unit3, amount3, item4, unit4, amount4,
                    item5, unit5, amount5, item6, unit6, amount6,
                    item7, unit7, amount7, item8, unit8, amount8,
                    item9, unit9, amount9, item10, unit10, amount10, listType, invoiceNote
                )
            this.findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInvoicePriceInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.totalButton.setOnClickListener { calculateTotal() }

        val id = navigationArgs.invoiceId
        if (id > 0) {
            bind()
        } else {
            val clientName = navigationArgs.clientName
            binding.clientName.text = clientName
            binding.nextButton.setOnClickListener {
                showSnackBar()
                passNewInfoToPreview()
            }

            binding.backButton.setOnClickListener {
                this.findNavController().navigateUp()
            }
        }

        when (navigationArgs.listType) {
            "In" -> binding.appTitle.setText(R.string.invoice_details)
            "Re" -> binding.appTitle.setText(R.string.receipt_details)
        }
    }

    private fun calculateTotal() {
        var sumTotal = 0.0

        var amount1 = binding.amount1.text.toString().toDoubleOrNull()
        if (amount1 == null) {
            amount1 = 0.0
        }

        var unit1 = binding.unit1.text.toString().toDoubleOrNull()
        if (unit1 == null) {
            unit1 = 1.0
        }

        val finalAmount1 = amount1 * unit1
        sumTotal += finalAmount1

        var amount2 = binding.amount2.text.toString().toDoubleOrNull()
        if (amount2 == null) {
            amount2 = 0.0
        }

        var unit2 = binding.unit2.text.toString().toDoubleOrNull()
        if (unit2 == null) {
            unit2 = 1.0
        }

        val finalAmount2 = amount2 * unit2
        sumTotal += finalAmount2

        var amount3 = binding.amount3.text.toString().toDoubleOrNull()
        if (amount3 == null) {
            amount3 = 0.0
        }

        var unit3 = binding.unit3.text.toString().toDoubleOrNull()
        if (unit3 == null) {
            unit3 = 1.0
        }

        val finalAmount3 = amount3 * unit3
        sumTotal += finalAmount3

        var amount4 = binding.amount4.text.toString().toDoubleOrNull()
        if (amount4 == null) {
            amount4 = 0.0
        }

        var unit4 = binding.unit4.text.toString().toDoubleOrNull()
        if (unit4 == null) {
            unit4 = 1.0
        }

        val finalAmount4 = amount4 * unit4
        sumTotal += finalAmount4

        var amount5 = binding.amount5.text.toString().toDoubleOrNull()
        if (amount5 == null) {
            amount5 = 0.0
        }

        var unit5 = binding.unit5.text.toString().toDoubleOrNull()
        if (unit5 == null) {
            unit5 = 1.0
        }

        val finalAmount5 = amount5 * unit5
        sumTotal += finalAmount5

        var amount6 = binding.amount6.text.toString().toDoubleOrNull()
        if (amount6 == null) {
            amount6 = 0.0
        }

        var unit6 = binding.unit6.text.toString().toDoubleOrNull()
        if (unit6 == null) {
            unit6 = 1.0
        }

        val finalAmount6 = amount6 * unit6
        sumTotal += finalAmount6

        var amount7 = binding.amount7.text.toString().toDoubleOrNull()
        if (amount7 == null) {
            amount7 = 0.0
        }

        var unit7 = binding.unit7.text.toString().toDoubleOrNull()
        if (unit7 == null) {
            unit7 = 1.0
        }

        val finalAmount7 = amount7 * unit7
        sumTotal += finalAmount7

        var amount8 = binding.amount8.text.toString().toDoubleOrNull()
        if (amount8 == null) {
            amount8 = 0.0
        }

        var unit8 = binding.unit8.text.toString().toDoubleOrNull()
        if (unit8 == null) {
            unit8 = 1.0
        }

        val finalAmount8 = amount8 * unit8
        sumTotal += finalAmount8

        var amount9 = binding.amount9.text.toString().toDoubleOrNull()
        if (amount9 == null) {
            amount9 = 0.0
        }

        var unit9 = binding.unit9.text.toString().toDoubleOrNull()
        if (unit9 == null) {
            unit9 = 1.0
        }

        val finalAmount9 = amount9 * unit9
        sumTotal += finalAmount9

        var amount10 = binding.amount10.text.toString().toDoubleOrNull()
        if (amount10 == null) {
            amount10 = 0.0
        }

        var unit10 = binding.unit10.text.toString().toDoubleOrNull()
        if (unit10 == null) {
            unit10 = 1.0
        }

        val finalAmount10 = amount10 * unit10
        sumTotal += finalAmount10

        displayTotal(sumTotal)
    }

    private fun displayTotal(sumTotal: Double) {
        val formattedTotal = NumberFormat.getCurrencyInstance().format(sumTotal)
        binding.total.text = getString(R.string.total_amount, formattedTotal)
    }

    private fun bind() {
        binding.apply {
            clientName.text = navigationArgs.clientName

            item1.setText(navigationArgs.item1, TextView.BufferType.SPANNABLE)
            unit1.setText(navigationArgs.unit1, TextView.BufferType.SPANNABLE)
            amount1.setText(navigationArgs.amount1, TextView.BufferType.SPANNABLE)
            item2.setText(navigationArgs.item2, TextView.BufferType.SPANNABLE)
            unit2.setText(navigationArgs.unit2, TextView.BufferType.SPANNABLE)
            amount2.setText(navigationArgs.amount2, TextView.BufferType.SPANNABLE)
            item3.setText(navigationArgs.item3, TextView.BufferType.SPANNABLE)
            unit3.setText(navigationArgs.unit3, TextView.BufferType.SPANNABLE)
            amount3.setText(navigationArgs.amount3, TextView.BufferType.SPANNABLE)
            item4.setText(navigationArgs.item4, TextView.BufferType.SPANNABLE)
            unit4.setText(navigationArgs.unit4, TextView.BufferType.SPANNABLE)
            amount4.setText(navigationArgs.amount4, TextView.BufferType.SPANNABLE)
            item5.setText(navigationArgs.item5, TextView.BufferType.SPANNABLE)
            unit5.setText(navigationArgs.unit5, TextView.BufferType.SPANNABLE)
            amount5.setText(navigationArgs.amount5, TextView.BufferType.SPANNABLE)
            item6.setText(navigationArgs.item6, TextView.BufferType.SPANNABLE)
            unit6.setText(navigationArgs.unit6, TextView.BufferType.SPANNABLE)
            amount6.setText(navigationArgs.amount6, TextView.BufferType.SPANNABLE)
            item7.setText(navigationArgs.item7, TextView.BufferType.SPANNABLE)
            unit7.setText(navigationArgs.unit7, TextView.BufferType.SPANNABLE)
            amount7.setText(navigationArgs.amount7, TextView.BufferType.SPANNABLE)
            item8.setText(navigationArgs.item8, TextView.BufferType.SPANNABLE)
            unit8.setText(navigationArgs.unit8, TextView.BufferType.SPANNABLE)
            amount8.setText(navigationArgs.amount8, TextView.BufferType.SPANNABLE)
            item9.setText(navigationArgs.item9, TextView.BufferType.SPANNABLE)
            unit9.setText(navigationArgs.unit9, TextView.BufferType.SPANNABLE)
            amount9.setText(navigationArgs.amount9, TextView.BufferType.SPANNABLE)
            item10.setText(navigationArgs.item10, TextView.BufferType.SPANNABLE)
            unit10.setText(navigationArgs.unit10, TextView.BufferType.SPANNABLE)
            amount10.setText(navigationArgs.amount10, TextView.BufferType.SPANNABLE)
            total.setText(navigationArgs.total, TextView.BufferType.SPANNABLE)
            addNoteText.setText(navigationArgs.invoiceNote, TextView.BufferType.SPANNABLE)

            binding.nextButton.setOnClickListener {
                when (navigationArgs.invoiceId) {
                    777777777 -> {
                        passToPreviewAsNew()
                    }
                    else -> {
                        passUpdateToPreview()
                    }
                }
            }
        }
        binding.backButton.setOnClickListener { this.findNavController().navigateUp() }
    }

    private fun passUpdateToPreview() {
        if (isToPreviewPossible()) {
            val clientName = navigationArgs.clientName
            val clientPhone = navigationArgs.clientPhone
            val clientEmail = navigationArgs.clientEmail
            val clientAddress = navigationArgs.clientAddress
            val clientId = navigationArgs.clientId

            val invoiceNumber = navigationArgs.invoiceNumber
            val date = navigationArgs.date
            val item1 = binding.item1.text.toString()
            val unit1 = binding.unit1.text.toString()
            val amount1 = binding.amount1.text.toString()
            val item2 = binding.item2.text.toString()
            val unit2 = binding.unit2.text.toString()
            val amount2 = binding.amount2.text.toString()
            val item3 = binding.item3.text.toString()
            val unit3 = binding.unit3.text.toString()
            val amount3 = binding.amount3.text.toString()
            val item4 = binding.item4.text.toString()
            val unit4 = binding.unit4.text.toString()
            val amount4 = binding.amount4.text.toString()
            val item5 = binding.item5.text.toString()
            val unit5 = binding.unit5.text.toString()
            val amount5 = binding.amount5.text.toString()
            val item6 = binding.item6.text.toString()
            val unit6 = binding.unit6.text.toString()
            val amount6 = binding.amount6.text.toString()
            val item7 = binding.item7.text.toString()
            val unit7 = binding.unit7.text.toString()
            val amount7 = binding.amount7.text.toString()
            val item8 = binding.item8.text.toString()
            val unit8 = binding.unit8.text.toString()
            val amount8 = binding.amount8.text.toString()
            val item9 = binding.item9.text.toString()
            val unit9 = binding.unit9.text.toString()
            val amount9 = binding.amount9.text.toString()
            val item10 = binding.item10.text.toString()
            val unit10 = binding.unit10.text.toString()
            val amount10 = binding.amount10.text.toString()

            val listType = navigationArgs.listType
            val invoiceNote = binding.addNoteText.text.toString()
            val fileName = navigationArgs.fileName
            val receiverNote = navigationArgs.receiverNote
            val cleared = navigationArgs.cleared
            val invoiceId = navigationArgs.invoiceId

            val action =
                InvoicePriceInfoFragmentDirections.actionInvoicePriceInfoFragmentToInvoicePreviewFragment(
                    clientId,
                    clientName,
                    clientPhone,
                    clientEmail,
                    clientAddress,
                    item1,
                    unit1,
                    amount1,
                    item2,
                    unit2,
                    amount2,
                    item3,
                    unit3,
                    amount3,
                    item4,
                    unit4,
                    amount4,
                    item5,
                    unit5,
                    amount5,
                    item6,
                    unit6,
                    amount6,
                    item7,
                    unit7,
                    amount7,
                    item8,
                    unit8,
                    amount8,
                    item9,
                    unit9,
                    amount9,
                    item10,
                    unit10,
                    amount10,
                    listType,
                    invoiceNote,
                    fileName,
                    date,
                    invoiceNumber,
                    receiverNote,
                    cleared,
                    invoiceId
                )
            this.findNavController().navigate(action)
        }
    }

    //pass an existing invoice/receipt being sent to a new client as a new invoice/receipt entry
    private fun passToPreviewAsNew() {
        if (isToPreviewPossible()) {
            val clientName = navigationArgs.clientName
            val clientPhone = navigationArgs.clientPhone
            val clientEmail = navigationArgs.clientEmail
            val clientAddress = navigationArgs.clientAddress
            val clientId = navigationArgs.clientId

            val invoiceNumber = navigationArgs.invoiceNumber
            val date = navigationArgs.date
            val item1 = binding.item1.text.toString()
            val unit1 = binding.unit1.text.toString()
            val amount1 = binding.amount1.text.toString()
            val item2 = binding.item2.text.toString()
            val unit2 = binding.unit2.text.toString()
            val amount2 = binding.amount2.text.toString()
            val item3 = binding.item3.text.toString()
            val unit3 = binding.unit3.text.toString()
            val amount3 = binding.amount3.text.toString()
            val item4 = binding.item4.text.toString()
            val unit4 = binding.unit4.text.toString()
            val amount4 = binding.amount4.text.toString()
            val item5 = binding.item5.text.toString()
            val unit5 = binding.unit5.text.toString()
            val amount5 = binding.amount5.text.toString()
            val item6 = binding.item6.text.toString()
            val unit6 = binding.unit6.text.toString()
            val amount6 = binding.amount6.text.toString()
            val item7 = binding.item7.text.toString()
            val unit7 = binding.unit7.text.toString()
            val amount7 = binding.amount7.text.toString()
            val item8 = binding.item8.text.toString()
            val unit8 = binding.unit8.text.toString()
            val amount8 = binding.amount8.text.toString()
            val item9 = binding.item9.text.toString()
            val unit9 = binding.unit9.text.toString()
            val amount9 = binding.amount9.text.toString()
            val item10 = binding.item10.text.toString()
            val unit10 = binding.unit10.text.toString()
            val amount10 = binding.amount10.text.toString()

            val listType = navigationArgs.listType
            val invoiceNote = binding.addNoteText.text.toString()
            val fileName = navigationArgs.fileName
            val receiverNote = navigationArgs.receiverNote
            //val invoiceId = navigationArgs.invoiceId

            val action =
                InvoicePriceInfoFragmentDirections.actionInvoicePriceInfoFragmentToInvoicePreviewFragment(
                    clientId,
                    clientName,
                    clientPhone,
                    clientEmail,
                    clientAddress,
                    item1,
                    unit1,
                    amount1,
                    item2,
                    unit2,
                    amount2,
                    item3,
                    unit3,
                    amount3,
                    item4,
                    unit4,
                    amount4,
                    item5,
                    unit5,
                    amount5,
                    item6,
                    unit6,
                    amount6,
                    item7,
                    unit7,
                    amount7,
                    item8,
                    unit8,
                    amount8,
                    item9,
                    unit9,
                    amount9,
                    item10,
                    unit10,
                    amount10,
                    listType,
                    invoiceNote,
                    fileName,
                    date,
                    invoiceNumber,
                    receiverNote
                )
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