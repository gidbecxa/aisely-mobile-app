package com.example.priceelist.invoice

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.priceelist.PricelistApplication
import com.example.priceelist.R
import com.example.priceelist.account.AccountViewModel
import com.example.priceelist.account.AccountViewModelFactory
import com.example.priceelist.client.ClientViewModel
import com.example.priceelist.client.ClientViewModelFactory
import com.example.priceelist.data.Account
import com.example.priceelist.data.Client
import com.example.priceelist.data.Invoice
import com.example.priceelist.data.getFormattedPrice
import com.example.priceelist.databinding.FragmentPreviewBinding
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat


class PreviewFragment : Fragment() {
    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var invoiceItem: Invoice
    private lateinit var clientItem: Client
    private lateinit var user: Account

    private val navigationArgs: PreviewFragmentArgs by navArgs()

    private val clientViewModel: ClientViewModel by activityViewModels {
        ClientViewModelFactory(
            (activity?.application as PricelistApplication).database.clientDao()
        )
    }

    private val invoiceViewModel: InvoiceViewModel by activityViewModels {
        InvoiceViewModelFactory(
            (activity?.application as PricelistApplication).database.invoiceDao()
        )
    }

    private val accountViewModel: AccountViewModel by activityViewModels {
        AccountViewModelFactory(
            (activity?.application as PricelistApplication).database.accountDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.invoiceId

        invoiceViewModel.retrieveInvoice(id)
            .observe(this.viewLifecycleOwner) { selectedInvoice ->
                invoiceItem = selectedInvoice //
                val clientId = invoiceItem.clientId

                clientViewModel.retrieveClient(clientId)
                    .observe(this.viewLifecycleOwner) { thisClient ->
                        clientItem = thisClient

                        bindToClientFields(clientItem)
                        bindToInvoiceFields(invoiceItem)
                        passArgs(invoiceItem, clientItem)
                    }

                binding.overflowButton.setOnClickListener {
                    binding.overflowButton.isEnabled = false
                    val fileName = invoiceItem.fileName
                    //saveImageToInternal(makeLayoutBitmap(binding.previewFragmentLayout)).toString()
                    Handler(Looper.getMainLooper()).postDelayed({
                        val action =
                            PreviewFragmentDirections.actionPreviewFragmentToPreviewMenuDialogFragment(
                                invoiceItem.id, fileName
                            )
                        this.findNavController().navigate(action)
                        binding.overflowButton.isEnabled = true
                    }, 500)
                }
            }

        accountViewModel.getUserData().observe(this.viewLifecycleOwner) { thisUser ->
            user = thisUser
            bindUserInfo(user)
        }

        binding.previewBackButton.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun makeLayoutBitmap(layout: View): Bitmap {
        val bitmapImage: Bitmap =
            Bitmap.createBitmap(layout.width, layout.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmapImage)
        val bgDrawable: Drawable = layout.background
        bgDrawable.draw(canvas)
        layout.draw(canvas)
        return bitmapImage
    }

    private fun showInvoiceOrReceipt(type: String): String {
        return if (type == "INVOICE") {
            "Invoice"
        } else
            "Receipt"
    }

    private fun hideRow(column1: TextView, column2: TextView, column3: TextView) {
        if (column1.text.toString().isBlank() && column2.text.toString()
                .isBlank() && column3.text.toString().isBlank()
        ) {
            column1.visibility = View.GONE
            column2.visibility = View.GONE
            column3.visibility = View.GONE
        }
    }

    private fun hideEmptyRows() {
        hideRow(binding.item1, binding.unit1, binding.amount1)
        hideRow(binding.item2, binding.unit2, binding.amount2)
        hideRow(binding.item3, binding.unit3, binding.amount3)
        hideRow(binding.item4, binding.unit4, binding.amount4)
        hideRow(binding.item5, binding.unit5, binding.amount5)
        hideRow(binding.item6, binding.unit6, binding.amount6)
        hideRow(binding.item7, binding.unit7, binding.amount7)
        hideRow(binding.item8, binding.unit8, binding.amount8)
        hideRow(binding.item9, binding.unit9, binding.amount9)
        hideRow(binding.item10, binding.unit10, binding.amount10)
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

    private fun displayCurrencyValue(number: Double): String {
        return NumberFormat.getCurrencyInstance().format(number)
    }

    private val currencySymbol = displayCurrencyValue(100.00).first().toString()

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun bindToInvoiceFields(item: Invoice) {
        binding.apply {

            when (item.listType) {
                "In" -> {
                    binding.invoiceTitle.text = resources.getString(R.string.invoice_title)
                }
                "Re" -> {
                    binding.invoiceTitle.text = resources.getString(R.string.receipt_title)
                }
            }

            invoiceNumber.text = item.invoiceNumber
            date.text = item.date
            currency.text = "($currencySymbol)"

            item1.text = item.invoiceItem1
            unit1.text = setToEmptyIfNull(item.invoiceQuantity1)
            amount1.text = setToEmptyIfNull(item.invoiceAmount1)
            item2.text = item.invoiceItem2
            unit2.text = setToEmptyIfNull(item.invoiceQuantity2)
            amount2.text = setToEmptyIfNull(item.invoiceAmount2)
            item3.text = item.invoiceItem3
            unit3.text = setToEmptyIfNull(item.invoiceQuantity3)
            amount3.text = setToEmptyIfNull(item.invoiceAmount3)
            item4.text = item.invoiceItem4
            unit4.text = setToEmptyIfNull(item.invoiceQuantity4)
            amount4.text = setToEmptyIfNull(item.invoiceAmount4)
            item5.text = item.invoiceItem5
            unit5.text = setToEmptyIfNull(item.invoiceQuantity5)
            amount5.text = setToEmptyIfNull(item.invoiceAmount5)
            item6.text = item.invoiceItem6
            unit6.text = setToEmptyIfNull(item.invoiceQuantity6)
            amount6.text = setToEmptyIfNull(item.invoiceAmount6)
            item7.text = item.invoiceItem7
            unit7.text = setToEmptyIfNull(item.invoiceQuantity7)
            amount7.text = setToEmptyIfNull(item.invoiceAmount7)
            item8.text = item.invoiceItem8
            unit8.text = setToEmptyIfNull(item.invoiceQuantity8)
            amount8.text = setToEmptyIfNull(item.invoiceAmount8)
            item9.text = item.invoiceItem9
            unit9.text = setToEmptyIfNull(item.invoiceQuantity9)
            amount9.text = setToEmptyIfNull(item.invoiceAmount9)
            item10.text = item.invoiceItem10
            unit10.text = setToEmptyIfNull(item.invoiceQuantity10)
            amount10.text = setToEmptyIfNull(item.invoiceAmount10)
            total.text = item.getFormattedPrice()
            invoiceNote.text = item.invoiceNote
        }

        when (item.listType) {
            "In" -> {
                binding.footerDetails.visibility = View.VISIBLE
            }
            "Re" -> {
                binding.footerDetails.visibility = View.GONE
            }
        }

        hideEmptyRows()

        Handler(Looper.getMainLooper()).postDelayed({
            binding.imagePreview.setImageBitmap(makeLayoutBitmap(binding.previewFragmentLayout))
            binding.loadingPreview.visibility = View.GONE
            binding.imagePreviewFrame.visibility = View.VISIBLE
        }, 4000)
    }

    private fun bindToClientFields(item: Client) {
        binding.apply {
            clientNameTextView.text = item.clientName
            clientAddressTextView.text = item.clientAddress
            clientPhoneTextView.text = item.clientPhone
            clientEmailTextView.text = item.clientMail
        }
    }

    private fun bindUserInfo(item: Account) {
        binding.businessName.text = item.businessName
        binding.businessName.isAllCaps = true

        binding.businessAddressTextView.text = item.businessAddress

        binding.accountName.text = item.bankAccName
        binding.accountName.isAllCaps = true

        binding.accountNo.text = item.bankAccNo
        binding.accountNo.isAllCaps = true

        binding.bankName.text = item.institutionName
        binding.bankName.isAllCaps = true
    }

    private fun passArgs(itemA: Invoice, itemB: Client) {
        val clientName = itemB.clientName
        val clientAddress = itemB.clientAddress.toString()
        val clientPhone = itemB.clientPhone
        val clientEmail = itemB.clientMail

        val invoiceNumber = itemA.invoiceNumber
        val date = itemA.date
        val clientId = itemA.clientId
        val item1 = itemA.invoiceItem1.toString()
        val unit1 = setToEmptyIfNull(itemA.invoiceQuantity1)
        val amount1 = setToEmptyIfNull(itemA.invoiceAmount1)
        val item2 = itemA.invoiceItem2.toString()
        val unit2 = setToEmptyIfNull(itemA.invoiceQuantity2)
        val amount2 = setToEmptyIfNull(itemA.invoiceAmount2)
        val item3 = itemA.invoiceItem3.toString()
        val unit3 = setToEmptyIfNull(itemA.invoiceQuantity3)
        val amount3 = setToEmptyIfNull(itemA.invoiceAmount3)
        val item4 = itemA.invoiceItem4.toString()
        val unit4 = setToEmptyIfNull(itemA.invoiceQuantity4)
        val amount4 = setToEmptyIfNull(itemA.invoiceAmount4)
        val item5 = itemA.invoiceItem5.toString()
        val unit5 = setToEmptyIfNull(itemA.invoiceQuantity5)
        val amount5 = setToEmptyIfNull(itemA.invoiceAmount5)
        val item6 = itemA.invoiceItem6.toString()
        val unit6 = setToEmptyIfNull(itemA.invoiceQuantity6)
        val amount6 = setToEmptyIfNull(itemA.invoiceAmount6)
        val item7 = itemA.invoiceItem7.toString()
        val unit7 = setToEmptyIfNull(itemA.invoiceQuantity7)
        val amount7 = setToEmptyIfNull(itemA.invoiceAmount7)
        val item8 = itemA.invoiceItem8.toString()
        val unit8 = setToEmptyIfNull(itemA.invoiceQuantity8)
        val amount8 = setToEmptyIfNull(itemA.invoiceAmount8)
        val item9 = itemA.invoiceItem9.toString()
        val unit9 = setToEmptyIfNull(itemA.invoiceQuantity9)
        val amount9 = setToEmptyIfNull(itemA.invoiceAmount9)
        val item10 = itemA.invoiceItem10.toString()
        val unit10 = setToEmptyIfNull(itemA.invoiceQuantity10)
        val amount10 = setToEmptyIfNull(itemA.invoiceAmount10)
        val total = itemA.getFormattedPrice()
        val listType = itemA.listType
        val invoiceNote = itemA.invoiceNote
        val fileName = itemA.fileName
        val receiverNote = itemA.receiverNote
        val cleared = itemA.cleared
        val invoiceId = itemA.id

        binding.editButton.setOnClickListener {
            val action = PreviewFragmentDirections.actionPreviewFragmentToInvoicePriceInfoFragment(
                clientName,
                clientPhone,
                clientEmail,
                clientAddress,
                listType,
                clientId,
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
                date,
                invoiceNumber,
                invoiceNote,
                fileName,
                receiverNote,
                cleared,
                invoiceId
            )
            this.findNavController().navigate(action)
        }
    }

}