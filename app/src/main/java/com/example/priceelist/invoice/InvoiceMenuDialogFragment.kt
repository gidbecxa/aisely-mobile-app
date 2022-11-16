package com.example.priceelist.invoice

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.priceelist.PricelistApplication
import com.example.priceelist.R
import com.example.priceelist.account.StatsViewModel
import com.example.priceelist.account.StatsViewModelFactory
import com.example.priceelist.client.ClientViewModel
import com.example.priceelist.client.ClientViewModelFactory
import com.example.priceelist.data.Client
import com.example.priceelist.data.Invoice
import com.example.priceelist.data.getFormattedPrice
import com.example.priceelist.databinding.FragmentInvoiceMenuDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormat
import java.util.*

class InvoiceMenuDialogFragment : BottomSheetDialogFragment() {

    private val statsViewModel: StatsViewModel by activityViewModels {
        StatsViewModelFactory(
            (activity?.application as PricelistApplication).database.statsDao()
        )
    }

    private val viewModel: InvoiceViewModel by activityViewModels {
        InvoiceViewModelFactory(
            (activity?.application as PricelistApplication).database.invoiceDao()
        )
    }

    private val clientViewModel: ClientViewModel by activityViewModels {
        ClientViewModelFactory(
            (activity?.application as PricelistApplication).database.clientDao()
        )
    }

    private var _binding: FragmentInvoiceMenuDialogBinding? = null
    private val binding get() = _binding!!

    lateinit var item: Invoice
    private lateinit var clientItem: Client
    private val navigationArgs: InvoiceMenuDialogFragmentArgs by navArgs()

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

    private fun addSentStat(item: Invoice) {
        if (item.listType == "In") {
            statsViewModel.addStat(
                item.invoiceNumber,
                "In",
                "sent",
                calendarDate,
                item.invoiceTotal.toString()
            )
        } else {
            statsViewModel.addStat(
                item.invoiceNumber,
                "Re",
                "sent",
                calendarDate,
                item.invoiceTotal.toString()
            )
        }
    }

    private fun addDownloadStat(item: Invoice) {
        if (item.listType == "In") {
            statsViewModel.addStat(
                item.invoiceNumber,
                "In",
                "downloaded",
                item.date,
                item.invoiceTotal.toString()
            )
        } else {
            statsViewModel.addStat(
                item.invoiceNumber,
                "Re",
                "downloaded",
                item.date,
                item.invoiceTotal.toString()
            )
        }
    }

    private fun clrInvoiceStat(item: Invoice) {
        statsViewModel.addStat(
            item.invoiceNumber,
            item.listType,
            "cleared",
            calendarDate,
            item.invoiceTotal.toString()
        )

        viewModel.clrInvoice(item.id, cleared = true)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun bind(item1: Invoice, item2: Client) {
        binding.apply {
            binding.menuTitle.text = item1.invoiceNumber

            when (item1.listType) {
                "In" -> {
                    binding.menuInvoiceIcon.setImageResource(R.drawable.ic_menu_invoice)
                    binding.type.text = getString(R.string.invoice_title)
                    binding.menuEditText.text = getString(R.string.edit_invoice)
                    binding.menuSendText.text = getString(R.string.invoice_menu_send)
                    binding.menuDeleteInvoiceText.text = getString(R.string.invoice_menu_delete)
                    binding.menuDownloadText.text = getString(R.string.invoice_menu_download)
                }
                "Re" -> {
                    binding.menuInvoiceIcon.setImageResource(R.drawable.ic_menu_receipt)
                    binding.type.text = getString(R.string.receipt_title)
                    binding.menuEditText.text = getString(R.string.edit_receipt)
                    binding.menuSendText.text = getString(R.string.receipt_menu_send)
                    binding.menuDeleteInvoiceText.text = getString(R.string.receipt_menu_delete)
                    binding.menuDownloadText.text = getString(R.string.receipt_menu_download)
                    binding.menuMakeReceipt.visibility = View.GONE
                    binding.menuClearInvoice.visibility = View.GONE
                }
            }

            if (item1.cleared) {
                binding.menuClearInvoice.visibility = View.GONE
            }

            menuEditInvoice.setOnClickListener {
                passArgs(item1, item2)
            }

            menuSend.setOnClickListener {
                executeShareAction()
                //findNavController().navigateUp()
                addSentStat(item1)
            }

            menuSendNew.setOnClickListener {
                val action =
                    InvoiceMenuDialogFragmentDirections.actionInvoiceMenuDialogFragmentToInvoiceNewClientFragment(
                        item1.id
                    )
                findNavController().navigate(action)
            }

            menuMakeReceipt.setOnClickListener {
                makeReceipt(item1, item2)
            }

            menuDeleteInvoice.setOnClickListener {
                when (item1.listType) {
                    "In" -> showConfirmationDialog1()
                    "Re" -> showConfirmationDialog2()
                }
            }

            menuClearInvoice.setOnClickListener {
                showClrConfirmationDialog(item1)
            }

            menuDownload.setOnClickListener {
                executeSaveAction()
                addDownloadStat(item1)
            }

        }
    }

    /**
     * Requesting Storage permissions ...
     */

    @RequiresApi(Build.VERSION_CODES.Q)
    private val requestSharePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                //permission is granted...execute action
                Snackbar.make(
                    requireView(),
                    getString(R.string.storage_permission_granted),
                    Snackbar.LENGTH_LONG
                ).show()

                shareImage(makeBitmap(navigationArgs.fileName))
            } else {
                //permission denied...
                Snackbar.make(
                    requireView(),
                    getString(R.string.storage_permission_denied),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.Q)
    private val requestSavePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                //permission is granted...execute action
                Snackbar.make(
                    requireView(),
                    getString(R.string.storage_permission_granted),
                    Snackbar.LENGTH_LONG
                ).show()

                saveImage(makeBitmap(navigationArgs.fileName))
            } else {
                //permission denied...
                Snackbar.make(
                    requireView(),
                    getString(R.string.storage_permission_denied),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun executeShareAction() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            //permission is granted...execute action
            shareImage(makeBitmap(navigationArgs.fileName))
        } else requestPermission()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun executeSaveAction() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            //permission is granted...execute action
            saveImage(makeBitmap(navigationArgs.fileName))
        } else requestPermission2()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestPermission() {
        //val colorWhite: Int = Color.parseColor("#ffffff")

        //request the missing permission by providing an additional rationale to the user
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(
                binding.root,
                getString(R.string.request_permission_rationale),
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(getString(R.string.ok)) {
                    requestSharePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                //.setBackgroundTint(colorWhite)
                .show()
        } else {
            //ask directly for permission
            requestSharePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestPermission2() {
        //val colorWhite: Int = Color.parseColor("#ffffff")

        //request the missing permission by providing an additional rationale to the user
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(
                binding.root,
                getString(R.string.request_permission_rationale),
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(getString(R.string.ok)) {
                    requestSavePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
                //.setBackgroundTint(colorWhite)
                .show()
        } else {
            //ask directly for permission
            requestSharePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
    //requesting permissions end here ...

    private fun makeBitmap(fileName: String): Bitmap {
        val file = File(fileName)
        val filePath = file.path
        val fileToBitmap: Bitmap = BitmapFactory.decodeFile(filePath)
        return Bitmap.createBitmap(fileToBitmap)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun shareImage(bitmap: Bitmap) {
        //val fileName = "PL_" + System.currentTimeMillis() + ".png"
        var outputStream: FileOutputStream? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context?.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(
                        MediaStore.Images.Media.DISPLAY_NAME,
                        "PL_${System.currentTimeMillis()}.png"
                    )
                    put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //val appDirUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                val imageUri: Uri? =
                    resolver.insert(
                        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
                        contentValues
                    )
                outputStream = imageUri?.let {
                    resolver.openOutputStream(it)
                } as FileOutputStream?

                val uri: Uri = Uri.parse(imageUri.toString())
                val share = Intent(Intent.ACTION_SEND)
                    .setType("image/png")
                    .putExtra(Intent.EXTRA_STREAM, uri)
                activity?.startActivity(Intent.createChooser(share, "Share"))
            }
        } else {
            context?.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(
                        MediaStore.Images.Media.DISPLAY_NAME,
                        "PL_${System.currentTimeMillis()}.png"
                    )
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                }

                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                outputStream = imageUri?.let {
                    resolver.openOutputStream(it)
                } as FileOutputStream?

                val uri: Uri = Uri.parse(imageUri.toString())
                val share = Intent(Intent.ACTION_SEND)
                    .setType("image/png")
                    .putExtra(Intent.EXTRA_STREAM, uri)
                activity?.startActivity(Intent.createChooser(share, "Share"))
            }
        }
        outputStream.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        Snackbar.make(
            binding.coordinatorLayout,
            getString(R.string.saved_to_device),
            Snackbar.LENGTH_LONG
        ).show()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveImage(bitmap: Bitmap) {
        //val fileName = "PL_" + System.currentTimeMillis() + ".png"
        var outputStream: FileOutputStream? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context?.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(
                        MediaStore.Images.Media.DISPLAY_NAME,
                        "PL_${System.currentTimeMillis()}.png"
                    )
                    put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //val appDirUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                val imageUri: Uri? =
                    resolver.insert(
                        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
                        contentValues
                    )
                outputStream = imageUri?.let {
                    resolver.openOutputStream(it)
                } as FileOutputStream?

            }
        } else {
            context?.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(
                        MediaStore.Images.Media.DISPLAY_NAME,
                        "PL_${System.currentTimeMillis()}.png"
                    )
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                }

                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                outputStream = imageUri?.let {
                    resolver.openOutputStream(it)
                } as FileOutputStream?

            }
        }
        outputStream.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        Snackbar.make(
            requireView(),
            //"${showInvoiceOrReceipt(binding.type.text.toString())} saved!",
            getString(R.string.saved_to_device),
            Snackbar.LENGTH_LONG
        ).show()
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

        val action =
            InvoiceMenuDialogFragmentDirections.actionInvoiceMenuDialogFragmentToInvoicePriceInfoFragment(
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

    private fun makeReceipt(itemA: Invoice, itemB: Client) {
        val clientName = itemB.clientName
        val clientAddress = itemB.clientAddress.toString()
        val clientPhone = itemB.clientPhone
        val clientEmail = itemB.clientMail

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
        val listType = "Re"
        val invoiceNote = itemA.invoiceNote
        val total = itemA.getFormattedPrice()
        val receiverNote = itemA.receiverNote
        val invoiceNumber = itemA.invoiceNumber
        val date = itemA.date
        val fileName = itemA.fileName
        val cleared = itemA.cleared
        val invoiceId =
            777777777

        /*val action = InvoiceMenuDialogFragmentDirections.actionInvoiceMenuDialogFragmentToInvoicePreviewFragment(
            clientId, clientName, clientPhone, clientEmail, clientAddress,
            item1, unit1, amount1, item2, unit2, amount2,
            item3, unit3, amount3, item4, unit4, amount4,
            item5, unit5, amount5, item6, unit6, amount6,
            item7, unit7, amount7, item8, unit8, amount8,
            item9, unit9, amount9, item10, unit10, amount10, listType, invoiceNote
        )*/
        val action =
            InvoiceMenuDialogFragmentDirections.actionInvoiceMenuDialogFragmentToInvoicePriceInfoFragment(
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInvoiceMenuDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.invoiceId

        viewModel.retrieveInvoice(id).observe(this.viewLifecycleOwner) { selectedInvoice ->

            /**
             * see note @ priceelist.MenuDialogFrament
             */
            if (selectedInvoice == null) {
                dismiss()
            } else {
                item = selectedInvoice
                //bind(item)

                val clientId = item.clientId
                clientViewModel.retrieveClient(clientId)
                    .observe(this.viewLifecycleOwner) { selectedClient ->
                        clientItem = selectedClient

                        bind(item, clientItem)
                    } //retrieve client details from invoice ends...
            } //else clause ends...
        } //retrieve invoice method ends...
    }

    private fun deleteInvoice() {
        viewModel.deleteInvoice(item)
        findNavController().navigateUp()
    }

    private fun showClrConfirmationDialog(item: Invoice) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.clear_invoice_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                clrInvoiceStat(item)
            }
            .show()
    }

    private fun showConfirmationDialog1() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_invoice_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteInvoice()
            }
            .show()
    }

    private fun showConfirmationDialog2() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_receipt_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteInvoice()
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