package com.example.priceelist.invoice

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.priceelist.PricelistApplication
import com.example.priceelist.client.ClientViewModel
import com.example.priceelist.client.ClientViewModelFactory
import com.example.priceelist.data.Client
import com.example.priceelist.data.Invoice
import com.example.priceelist.data.getFormattedPrice
import com.example.priceelist.databinding.FragmentInvoiceNewClientBinding
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormat
import java.util.*


class InvoiceNewClientFragment : Fragment() {

    private var _binding: FragmentInvoiceNewClientBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClientViewModel by activityViewModels {
        ClientViewModelFactory(
            (activity?.application as PricelistApplication).database.clientDao()
        )
    }

    private val invoiceViewModel: InvoiceViewModel by activityViewModels {
        InvoiceViewModelFactory(
            (activity?.application as PricelistApplication).database.invoiceDao()
        )
    }

    lateinit var item: Client
    private lateinit var itemIn: Invoice
    private val navigationArgs: InvoiceNewClientFragmentArgs by navArgs()

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

    private fun formatDouble(double: Double): String {
        return BigDecimal(double).setScale(2, RoundingMode.HALF_UP).toString()
    }

    private fun setToEmptyIfNull(dbField: Double?): String {
        return if (dbField.toString() == "null") {
            ""
        } else {
            formatDouble(dbField!!.toDouble())
        }
    }

    private val calendar = Calendar.getInstance().time
    private val calendarDate = DateFormat.getDateInstance().format(calendar).toString()

    @RequiresApi(Build.VERSION_CODES.N)
    fun generateCode(): String {
        println("Random code...")

        val sourceChars = "0123456789"
        val randomCode = Random().ints(8, 0, sourceChars.length)
            .toArray()
            .map(sourceChars::get)
            .joinToString("")
        return "PL$randomCode"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    val thisInvoiceNumber: String = generateCode()

    private fun passInfoToNext(itemIn: Invoice) {
        if (isSaveClientPossible()) {
            val clientName = binding.clientName.text.toString()
            val clientPhone = binding.clientPhone.text.toString()
            val clientEmail = binding.clientEmail.text.toString()
            val clientAddress = binding.clientAddress.text.toString()
            val listType = itemIn.listType
            val invoiceId =
                777777777 //unique value to distinguish an invoice/receipt being sent to a new client as new
            val item1 = itemIn.invoiceItem1.toString()
            val unit1 = setToEmptyIfNull(itemIn.invoiceQuantity1)
            val amount1 = setToEmptyIfNull(itemIn.invoiceAmount1)
            val item2 = itemIn.invoiceItem2.toString()
            val unit2 = setToEmptyIfNull(itemIn.invoiceQuantity2)
            val amount2 = setToEmptyIfNull(itemIn.invoiceAmount2)
            val item3 = itemIn.invoiceItem3.toString()
            val unit3 = setToEmptyIfNull(itemIn.invoiceQuantity3)
            val amount3 = setToEmptyIfNull(itemIn.invoiceAmount3)
            val item4 = itemIn.invoiceItem4.toString()
            val unit4 = setToEmptyIfNull(itemIn.invoiceQuantity4)
            val amount4 = setToEmptyIfNull(itemIn.invoiceAmount4)
            val item5 = itemIn.invoiceItem5.toString()
            val unit5 = setToEmptyIfNull(itemIn.invoiceQuantity5)
            val amount5 = setToEmptyIfNull(itemIn.invoiceAmount5)
            val item6 = itemIn.invoiceItem6.toString()
            val unit6 = setToEmptyIfNull(itemIn.invoiceQuantity6)
            val amount6 = setToEmptyIfNull(itemIn.invoiceAmount6)
            val item7 = itemIn.invoiceItem7.toString()
            val unit7 = setToEmptyIfNull(itemIn.invoiceQuantity7)
            val amount7 = setToEmptyIfNull(itemIn.invoiceAmount7)
            val item8 = itemIn.invoiceItem8.toString()
            val unit8 = setToEmptyIfNull(itemIn.invoiceQuantity8)
            val amount8 = setToEmptyIfNull(itemIn.invoiceAmount8)
            val item9 = itemIn.invoiceItem9.toString()
            val unit9 = setToEmptyIfNull(itemIn.invoiceQuantity9)
            val amount9 = setToEmptyIfNull(itemIn.invoiceAmount9)
            val item10 = itemIn.invoiceItem10.toString()
            val unit10 = setToEmptyIfNull(itemIn.invoiceQuantity10)
            val amount10 = setToEmptyIfNull(itemIn.invoiceAmount10)
            val total = itemIn.getFormattedPrice()

            val invoiceNote = itemIn.invoiceNote
            val cleared = itemIn.cleared
            val fileName = ""

            val action =
                InvoiceNewClientFragmentDirections.actionInvoiceNewClientFragmentToInvoicePriceInfoFragment(
                    clientName,
                    clientPhone,
                    clientEmail,
                    clientAddress,
                    listType,
                    -1,
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
                    total,
                    calendarDate,
                    thisInvoiceNumber,
                    invoiceNote,
                    fileName,
                    clientName,
                    cleared,
                    invoiceId
                )
            this.findNavController().navigate(action)
        }
    }

    private fun bindToFields(item: Client, itemIn: Invoice) {
        binding.apply {
            clientName.setText(item.clientName, TextView.BufferType.SPANNABLE)
            clientPhone.setText(item.clientPhone, TextView.BufferType.SPANNABLE)
            clientEmail.setText(item.clientMail, TextView.BufferType.SPANNABLE)
            clientAddress.setText(item.clientAddress, TextView.BufferType.SPANNABLE)

            binding.nextButton.setOnClickListener {
                showSnackBar()
                passUpdatedInfoToNext(itemIn)
            }
        }
    }

    private fun passUpdatedInfoToNext(itemIn: Invoice) {
        if (isSaveClientPossible()) {
            val clientName = binding.clientName.text.toString()
            val clientPhone = binding.clientPhone.text.toString()
            val clientEmail = binding.clientEmail.text.toString()
            val clientAddress = binding.clientAddress.text.toString()
            val listType = itemIn.listType
            val invoiceId =
                777777777 //unique value to distinguish an invoice/receipt being sent to a new client as new
            val item1 = itemIn.invoiceItem1.toString()
            val unit1 = setToEmptyIfNull(itemIn.invoiceQuantity1)
            val amount1 = setToEmptyIfNull(itemIn.invoiceAmount1)
            val item2 = itemIn.invoiceItem2.toString()
            val unit2 = setToEmptyIfNull(itemIn.invoiceQuantity2)
            val amount2 = setToEmptyIfNull(itemIn.invoiceAmount2)
            val item3 = itemIn.invoiceItem3.toString()
            val unit3 = setToEmptyIfNull(itemIn.invoiceQuantity3)
            val amount3 = setToEmptyIfNull(itemIn.invoiceAmount3)
            val item4 = itemIn.invoiceItem4.toString()
            val unit4 = setToEmptyIfNull(itemIn.invoiceQuantity4)
            val amount4 = setToEmptyIfNull(itemIn.invoiceAmount4)
            val item5 = itemIn.invoiceItem5.toString()
            val unit5 = setToEmptyIfNull(itemIn.invoiceQuantity5)
            val amount5 = setToEmptyIfNull(itemIn.invoiceAmount5)
            val item6 = itemIn.invoiceItem6.toString()
            val unit6 = setToEmptyIfNull(itemIn.invoiceQuantity6)
            val amount6 = setToEmptyIfNull(itemIn.invoiceAmount6)
            val item7 = itemIn.invoiceItem7.toString()
            val unit7 = setToEmptyIfNull(itemIn.invoiceQuantity7)
            val amount7 = setToEmptyIfNull(itemIn.invoiceAmount7)
            val item8 = itemIn.invoiceItem8.toString()
            val unit8 = setToEmptyIfNull(itemIn.invoiceQuantity8)
            val amount8 = setToEmptyIfNull(itemIn.invoiceAmount8)
            val item9 = itemIn.invoiceItem9.toString()
            val unit9 = setToEmptyIfNull(itemIn.invoiceQuantity9)
            val amount9 = setToEmptyIfNull(itemIn.invoiceAmount9)
            val item10 = itemIn.invoiceItem10.toString()
            val unit10 = setToEmptyIfNull(itemIn.invoiceQuantity10)
            val amount10 = setToEmptyIfNull(itemIn.invoiceAmount10)
            val total = itemIn.getFormattedPrice()

            val invoiceNote = itemIn.invoiceNote
            val cleared = itemIn.cleared
            val fileName = ""

            val action =
                InvoiceNewClientFragmentDirections.actionInvoiceNewClientFragmentToInvoicePriceInfoFragment(
                    clientName,
                    clientPhone,
                    clientEmail,
                    clientAddress,
                    listType,
                    item.id,
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
                    total,
                    calendarDate,
                    thisInvoiceNumber,
                    invoiceNote,
                    fileName,
                    clientName,
                    cleared,
                    invoiceId
                )
            this.findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInvoiceNewClientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.clientId
        val idInvoice = navigationArgs.invoiceId

        if (id > 0) {
            viewModel.retrieveClient(id).observe(this.viewLifecycleOwner) { selectedClient ->
                item = selectedClient

                invoiceViewModel.retrieveInvoice(idInvoice)
                    .observe(this.viewLifecycleOwner) { selectedInvoice ->
                        itemIn = selectedInvoice

                        bindToFields(item, itemIn)
                    }
            }
        } else {

            invoiceViewModel.retrieveInvoice(idInvoice)
                .observe(this.viewLifecycleOwner) { selectedInvoice ->
                    itemIn = selectedInvoice

                    binding.nextButton.setOnClickListener {
                        showSnackBar()
                        passInfoToNext(itemIn)
                    }

                }
        }

        invoiceViewModel.retrieveInvoice(idInvoice)
            .observe(this.viewLifecycleOwner) { selectedInvoice ->
                itemIn = selectedInvoice
                showOptionsMenu(itemIn)
            }

        binding.backButton.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun showOptionsMenu(item: Invoice) {
        binding.choose.setOnClickListener {
            binding.choose.isEnabled = false
            Handler(Looper.getMainLooper()).postDelayed({
                val action =
                    InvoiceNewClientFragmentDirections.actionInvoiceNewClientFragmentToSavedClientsMenuDialog(
                        item.listType,
                        item.id
                    )
                this.findNavController().navigate(action)
                binding.choose.isEnabled = true
            }, 100)
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