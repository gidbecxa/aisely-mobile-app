package com.example.priceelist.invoice

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.priceelist.PricelistApplication
import com.example.priceelist.R
import com.example.priceelist.account.AccountViewModel
import com.example.priceelist.account.AccountViewModelFactory
import com.example.priceelist.account.StatsViewModel
import com.example.priceelist.account.StatsViewModelFactory
import com.example.priceelist.client.ClientViewModel
import com.example.priceelist.client.ClientViewModelFactory
import com.example.priceelist.data.Account
import com.example.priceelist.data.Client
import com.example.priceelist.data.Invoice
import com.example.priceelist.databinding.FragmentInvoicePreviewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class InvoicePreviewFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback {
    private var _binding: FragmentInvoicePreviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var invoiceItem: Invoice
    private lateinit var clientItem: Client
    private lateinit var user: Account

    private val navigationArgs: InvoicePreviewFragmentArgs by navArgs()

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

    private val statsViewModel: StatsViewModel by activityViewModels {
        StatsViewModelFactory(
            (activity?.application as PricelistApplication).database.statsDao()
        )
    }

    private val calendar = Calendar.getInstance().time
    private val calendarDate = DateFormat.getDateInstance().format(calendar).toString()

    @RequiresApi(Build.VERSION_CODES.N)
    fun generateCode(): String {
        //println("Random code...")

        val sourceChars = "0123456789"
        val randomCode = Random().ints(8, 0, sourceChars.length)
            .toArray()
            .map(sourceChars::get)
            .joinToString("")
        return "PL$randomCode"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    val thisInvoiceNumber: String = generateCode()

    private fun getSumTotal(): Double {
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

        return sumTotal
        //return displayCurrencyValue(sumTotal)
    }

    private fun displayCurrencyValue(number: Double): String {
        return NumberFormat.getCurrencyInstance().format(number)
    }

    private val currency = displayCurrencyValue(100.00).first().toString()

    private fun saveNewClient() {
        clientViewModel.addNewClient(
            navigationArgs.clientName, navigationArgs.clientPhone,
            navigationArgs.clientEmail, navigationArgs.clientAddress
        )
    }

    private fun updateClient() {
        clientViewModel.updateClient(
            navigationArgs.clientId,
            navigationArgs.clientName, navigationArgs.clientPhone,
            navigationArgs.clientEmail, navigationArgs.clientAddress
        )
    }

    private fun saveClientDetails() {
        if (navigationArgs.clientId > 0) {
            updateClient()
        } else {
            saveNewClient()
        }
    }

    private fun addNewInvoice() {
        if (navigationArgs.clientId > 0) {
            binding.doneButton.setOnClickListener {
                //
                invoiceViewModel.getLastInvoiceItem()
                    .observe(this.viewLifecycleOwner) { lastInvoice ->

                        if (lastInvoice == null) {
                            saveNewInvoice()
                            saveClientDetails()
                            addCreatedStat()
                            doAfterSave()
                        } else {
                            invoiceItem = lastInvoice

                            if (invoiceItem.invoiceNumber == binding.invoiceNumber.text.toString()) {
                                updateInvoice()
                            } else {
                                saveNewInvoice()
                                saveClientDetails()
                                addCreatedStat()

                                doAfterSave()
                            }
                        }
                    }
            }
        } else {
            clientViewModel.getLastClient().observe(this.viewLifecycleOwner) { thisClient ->
                when (thisClient) {
                    null -> {
                        binding.doneButton.setOnClickListener {

                            invoiceViewModel.getLastInvoiceItem()
                                .observe(this.viewLifecycleOwner) { lastInvoice ->

                                    //To avoid a null error which would result from invoiceItem = lastInvoice
                                    // the condition below is necessary
                                    if (lastInvoice == null) {
                                        saveClientDetails()

                                        Handler(Looper.getMainLooper()).postDelayed({
                                            clientViewModel.getLastClient()
                                                .observe(this.viewLifecycleOwner) { lastClient ->
                                                    clientItem = lastClient
                                                    val newClientId = clientItem.id
                                                    fun saveNewInvoiceFirstClient() {
                                                        invoiceViewModel.addNewInvoice(
                                                            calendarDate,
                                                            thisInvoiceNumber,
                                                            newClientId,
                                                            navigationArgs.item1,
                                                            navigationArgs.unit1,
                                                            navigationArgs.amount1,
                                                            navigationArgs.item2,
                                                            navigationArgs.unit2,
                                                            navigationArgs.amount2,
                                                            navigationArgs.item3,
                                                            navigationArgs.unit3,
                                                            navigationArgs.amount3,
                                                            navigationArgs.item4,
                                                            navigationArgs.unit4,
                                                            navigationArgs.amount4,
                                                            navigationArgs.item5,
                                                            navigationArgs.unit5,
                                                            navigationArgs.amount5,
                                                            navigationArgs.item6,
                                                            navigationArgs.unit6,
                                                            navigationArgs.amount6,
                                                            navigationArgs.item7,
                                                            navigationArgs.unit7,
                                                            navigationArgs.amount7,
                                                            navigationArgs.item8,
                                                            navigationArgs.unit8,
                                                            navigationArgs.amount8,
                                                            navigationArgs.item9,
                                                            navigationArgs.unit9,
                                                            navigationArgs.amount9,
                                                            navigationArgs.item10,
                                                            navigationArgs.unit10,
                                                            navigationArgs.amount10,
                                                            getSumTotal().toString(),
                                                            navigationArgs.listType,
                                                            navigationArgs.clientName,
                                                            saveImageToInternal(
                                                                makeLayoutBitmap(
                                                                    binding.previewLayout
                                                                )
                                                            ).toString(),
                                                            navigationArgs.invoiceNote
                                                        )
                                                    }
                                                    saveNewInvoiceFirstClient()
                                                    addCreatedStat()
                                                    doAfterSave()
                                                }
                                        }, 250)

                                    } else {
                                        invoiceItem = lastInvoice
                                        if (invoiceItem.invoiceNumber == binding.invoiceNumber.text.toString()) {
                                            updateInvoice()
                                        } else {
                                            saveClientDetails()

                                            Handler(Looper.getMainLooper()).postDelayed({
                                                clientViewModel.getLastClient()
                                                    .observe(this.viewLifecycleOwner) { lastClient ->
                                                        clientItem = lastClient
                                                        val newClientId = clientItem.id
                                                        fun saveNewInvoiceFirstClient() {
                                                            invoiceViewModel.addNewInvoice(
                                                                calendarDate,
                                                                thisInvoiceNumber,
                                                                newClientId,
                                                                navigationArgs.item1,
                                                                navigationArgs.unit1,
                                                                navigationArgs.amount1,
                                                                navigationArgs.item2,
                                                                navigationArgs.unit2,
                                                                navigationArgs.amount2,
                                                                navigationArgs.item3,
                                                                navigationArgs.unit3,
                                                                navigationArgs.amount3,
                                                                navigationArgs.item4,
                                                                navigationArgs.unit4,
                                                                navigationArgs.amount4,
                                                                navigationArgs.item5,
                                                                navigationArgs.unit5,
                                                                navigationArgs.amount5,
                                                                navigationArgs.item6,
                                                                navigationArgs.unit6,
                                                                navigationArgs.amount6,
                                                                navigationArgs.item7,
                                                                navigationArgs.unit7,
                                                                navigationArgs.amount7,
                                                                navigationArgs.item8,
                                                                navigationArgs.unit8,
                                                                navigationArgs.amount8,
                                                                navigationArgs.item9,
                                                                navigationArgs.unit9,
                                                                navigationArgs.amount9,
                                                                navigationArgs.item10,
                                                                navigationArgs.unit10,
                                                                navigationArgs.amount10,
                                                                getSumTotal().toString(),
                                                                navigationArgs.listType,
                                                                navigationArgs.clientName,
                                                                saveImageToInternal(
                                                                    makeLayoutBitmap(
                                                                        binding.previewLayout
                                                                    )
                                                                ).toString(),
                                                                navigationArgs.invoiceNote
                                                            )
                                                        }
                                                        saveNewInvoiceFirstClient()
                                                        addCreatedStat()
                                                        doAfterSave()
                                                    }
                                            }, 250)
                                        }
                                    }//
                                }
                        }
                    }
                    else -> {
                        clientItem = thisClient
                        val clientId = clientItem.id + 1
                        fun saveNewInvoiceLatestClient() {
                            invoiceViewModel.addNewInvoice(
                                calendarDate,
                                thisInvoiceNumber,
                                clientId,
                                navigationArgs.item1,
                                navigationArgs.unit1,
                                navigationArgs.amount1,
                                navigationArgs.item2,
                                navigationArgs.unit2,
                                navigationArgs.amount2,
                                navigationArgs.item3,
                                navigationArgs.unit3,
                                navigationArgs.amount3,
                                navigationArgs.item4,
                                navigationArgs.unit4,
                                navigationArgs.amount4,
                                navigationArgs.item5,
                                navigationArgs.unit5,
                                navigationArgs.amount5,
                                navigationArgs.item6,
                                navigationArgs.unit6,
                                navigationArgs.amount6,
                                navigationArgs.item7,
                                navigationArgs.unit7,
                                navigationArgs.amount7,
                                navigationArgs.item8,
                                navigationArgs.unit8,
                                navigationArgs.amount8,
                                navigationArgs.item9,
                                navigationArgs.unit9,
                                navigationArgs.amount9,
                                navigationArgs.item10,
                                navigationArgs.unit10,
                                navigationArgs.amount10,
                                getSumTotal().toString(),
                                navigationArgs.listType,
                                navigationArgs.clientName,
                                saveImageToInternal(makeLayoutBitmap(binding.previewLayout)).toString(),
                                navigationArgs.invoiceNote
                            )
                        }
                        binding.doneButton.setOnClickListener {

                            invoiceViewModel.getLastInvoiceItem()
                                .observe(this.viewLifecycleOwner) { lastInvoice ->

                                    if (lastInvoice == null) {
                                        saveClientDetails()
                                        saveNewInvoiceLatestClient()
                                        addCreatedStat()
                                        doAfterSave()
                                    } else {
                                        invoiceItem = lastInvoice

                                        if (invoiceItem.invoiceNumber == binding.invoiceNumber.text.toString()) {
                                            updateInvoice()
                                        } else {
                                            saveClientDetails()
                                            saveNewInvoiceLatestClient()
                                            addCreatedStat()

                                            doAfterSave()
                                        }
                                    }
                                }
                        }
                    }
                }
            } //end of getLastClient
        } //end of else
    }

    private fun doAfterSave() {
        //binding.discardButton.visibility = View.GONE
        //binding.homeButton.visibility = View.VISIBLE

        Snackbar.make(
            requireView(),
            "${showInvoiceOrReceipt(navigationArgs.listType)} saved!",
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun addCreatedStat() {
        if (navigationArgs.listType == "In") {
            statsViewModel.addStat(
                binding.invoiceNumber.text.toString(),
                "In",
                "created",
                calendarDate,
                getSumTotal().toString()
            )
        } else {
            statsViewModel.addStat(
                binding.invoiceNumber.text.toString(),
                "Re",
                "created",
                calendarDate,
                getSumTotal().toString()
            )
        }
    }

    private fun addSentStat() {
        if (navigationArgs.listType == "In") {
            statsViewModel.addStat(
                binding.invoiceNumber.text.toString(),
                "In",
                "sent",
                calendarDate,
                getSumTotal().toString()
            )
        } else {
            statsViewModel.addStat(
                binding.invoiceNumber.text.toString(),
                "Re",
                "sent",
                calendarDate,
                getSumTotal().toString()
            )
        }
    }

    private fun addDownloadStat() {
        if (navigationArgs.listType == "In") {
            statsViewModel.addStat(
                binding.invoiceNumber.text.toString(),
                "In",
                "downloaded",
                calendarDate,
                getSumTotal().toString()
            )
        } else {
            statsViewModel.addStat(
                binding.invoiceNumber.text.toString(),
                "Re",
                "downloaded",
                calendarDate,
                getSumTotal().toString()
            )
        }
    }

    private fun saveNewInvoice() {
        val invoiceNumber = thisInvoiceNumber
        invoiceViewModel.addNewInvoice(
            calendarDate,
            invoiceNumber,
            navigationArgs.clientId,
            navigationArgs.item1,
            navigationArgs.unit1,
            navigationArgs.amount1,
            navigationArgs.item2,
            navigationArgs.unit2,
            navigationArgs.amount2,
            navigationArgs.item3,
            navigationArgs.unit3,
            navigationArgs.amount3,
            navigationArgs.item4,
            navigationArgs.unit4,
            navigationArgs.amount4,
            navigationArgs.item5,
            navigationArgs.unit5,
            navigationArgs.amount5,
            navigationArgs.item6,
            navigationArgs.unit6,
            navigationArgs.amount6,
            navigationArgs.item7,
            navigationArgs.unit7,
            navigationArgs.amount7,
            navigationArgs.item8,
            navigationArgs.unit8,
            navigationArgs.amount8,
            navigationArgs.item9,
            navigationArgs.unit9,
            navigationArgs.amount9,
            navigationArgs.item10,
            navigationArgs.unit10,
            navigationArgs.amount10,
            getSumTotal().toString(),
            navigationArgs.listType,
            navigationArgs.clientName,
            saveImageToInternal(makeLayoutBitmap(binding.previewLayout)).toString(),
            navigationArgs.invoiceNote
        )
    }

    private fun updateInvoice() {
        invoiceViewModel.updateInvoice(
            navigationArgs.invoiceId,
            navigationArgs.date,
            navigationArgs.invoiceNumber,
            navigationArgs.clientId,
            navigationArgs.item1,
            navigationArgs.unit1,
            navigationArgs.amount1,
            navigationArgs.item2,
            navigationArgs.unit2,
            navigationArgs.amount2,
            navigationArgs.item3,
            navigationArgs.unit3,
            navigationArgs.amount3,
            navigationArgs.item4,
            navigationArgs.unit4,
            navigationArgs.amount4,
            navigationArgs.item5,
            navigationArgs.unit5,
            navigationArgs.amount5,
            navigationArgs.item6,
            navigationArgs.unit6,
            navigationArgs.amount6,
            navigationArgs.item7,
            navigationArgs.unit7,
            navigationArgs.amount7,
            navigationArgs.item8,
            navigationArgs.unit8,
            navigationArgs.amount8,
            navigationArgs.item9,
            navigationArgs.unit9,
            navigationArgs.amount9,
            navigationArgs.item10,
            navigationArgs.unit10,
            navigationArgs.amount10,
            getSumTotal().toString(),
            navigationArgs.listType,
            navigationArgs.receiverNote,
            saveImageToInternal(makeLayoutBitmap(binding.previewLayout)).toString(),
            navigationArgs.invoiceNote,
            navigationArgs.cleared
        )
    }

    private fun saveImageToInternal(bitmap: Bitmap): File? {
        val pictureFileDir = File(requireContext().filesDir, "Priceelist")
        if (!pictureFileDir.exists()) {
            val isDirCreated = pictureFileDir.mkdirs()
            if (!isDirCreated)
                Log.i("SAVE", "Can't create directory!")
            return null
        }
        val fileName = pictureFileDir.path + File.separator + System.currentTimeMillis() + ".png"
        val pictureFile = File(fileName)
        //val bitmapImg = makeLayoutBitmap(binding.previewFragmentLayout)
        try {
            pictureFile.createNewFile()
            val oStream = FileOutputStream(pictureFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream)
            oStream.flush()
            oStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("TAG", "Oops! There was an issue saving the file!")
        }
        return pictureFile
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInvoicePreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.invoiceId
        if (id > 0) {
            /**
             * Except in the case where a user discards an update operation, update is done
             * when the user exits this fragment...
             */
            bindForUpdate()

        } else {
            bind()
            addNewInvoice()
        }

        accountViewModel.getUserData().observe(this.viewLifecycleOwner) { thisUser ->
            user = thisUser
            bindUserInfo(user)
        }

        binding.backButton.setOnClickListener { this.findNavController().navigateUp() }

        binding.homeButton.setOnClickListener {
            //Display confirmation message and navigate based on document type
            when (navigationArgs.listType) {
                "In" -> {
                    confirmInvoiceDiscardDialog()
                }
                "Re" -> {
                    confirmReceiptDiscardDialog()
                }
            }
        }

        /*binding.homeButton.setOnClickListener {
            when (navigationArgs.listType) {
                "Re" -> {
                    val action =
                        InvoicePreviewFragmentDirections.actionInvoicePreviewFragmentToReceiptsListFragment()
                    findNavController().navigate(action)
                }
                "In" -> {
                    val action =
                        InvoicePreviewFragmentDirections.actionInvoicePreviewFragmentToInvoicesListFragment()
                    findNavController().navigate(action)
                }
            }
        }*/

        binding.downloadButton.setOnClickListener {
            executeSaveAction()
        }

        binding.sendButton.setOnClickListener {
            executeShareAction()
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

                shareImage(makeLayoutBitmap(binding.previewLayout))
                addSentStat()
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

                saveImage(makeLayoutBitmap(binding.previewLayout))
                addDownloadStat()
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
            shareImage(makeLayoutBitmap(binding.previewLayout))
            addSentStat()
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
            saveImage(makeLayoutBitmap(binding.previewLayout))
            addSentStat()
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

    /**
     * Creating bitmaps ...
     */

    private fun makeLayoutBitmap(layout: View): Bitmap {
        val bitmapImage: Bitmap =
            Bitmap.createBitmap(layout.width, layout.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmapImage)
        val bgDrawable: Drawable = layout.background
        bgDrawable.draw(canvas)
        layout.draw(canvas)
        return bitmapImage
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
            getString(R.string.saved_to_device),
            Snackbar.LENGTH_LONG
        ).show()
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

    @SuppressLint("SetTextI18n")
    private fun bind() {

        val clientName = navigationArgs.clientName
        val clientPhone = navigationArgs.clientPhone
        val clientEmail = navigationArgs.clientEmail
        val clientAddress = navigationArgs.clientAddress
        val item1 = navigationArgs.item1
        val unit1 = navigationArgs.unit1
        val amount1 = navigationArgs.amount1
        val item2 = navigationArgs.item2
        val unit2 = navigationArgs.unit2
        val amount2 = navigationArgs.amount2
        val item3 = navigationArgs.item3
        val unit3 = navigationArgs.unit3
        val amount3 = navigationArgs.amount3
        val item4 = navigationArgs.item4
        val unit4 = navigationArgs.unit4
        val amount4 = navigationArgs.amount4
        val item5 = navigationArgs.item5
        val unit5 = navigationArgs.unit5
        val amount5 = navigationArgs.amount5
        val item6 = navigationArgs.item6
        val unit6 = navigationArgs.unit6
        val amount6 = navigationArgs.amount6
        val item7 = navigationArgs.item7
        val unit7 = navigationArgs.unit7
        val amount7 = navigationArgs.amount7
        val item8 = navigationArgs.item8
        val unit8 = navigationArgs.unit8
        val amount8 = navigationArgs.amount8
        val item9 = navigationArgs.item9
        val unit9 = navigationArgs.unit9
        val amount9 = navigationArgs.amount9
        val item10 = navigationArgs.item10
        val unit10 = navigationArgs.unit10
        val amount10 = navigationArgs.amount10
        val invoiceNote = navigationArgs.invoiceNote
        val listType = navigationArgs.listType


        binding.clientNameTextView.text = clientName; binding.clientAddressTextView.text =
            clientAddress
        binding.clientPhoneTextView.text = clientPhone; binding.clientEmailTextView.text =
            clientEmail
        binding.invoiceNumber.text = thisInvoiceNumber; binding.date.text =
            calendarDate; binding.currency.text = "($currency)"

        when (listType) {
            "In" -> {
                binding.invoiceTitle.text = resources.getString(R.string.invoice_title)
            }
            "Re" -> {
                binding.invoiceTitle.text = resources.getString(R.string.receipt_title)
            }
        }

        binding.item1.text = item1; binding.unit1.text = unit1; binding.amount1.text =
            formatDouble(amount1)
        binding.item2.text = item2; binding.unit2.text = unit2; binding.amount2.text =
            formatDouble(amount2)
        binding.item3.text = item3; binding.unit3.text = unit3; binding.amount3.text =
            formatDouble(amount3)
        binding.item4.text = item4; binding.unit4.text = unit4; binding.amount4.text =
            formatDouble(amount4)
        binding.item5.text = item5; binding.unit5.text = unit5; binding.amount5.text =
            formatDouble(amount5)
        binding.item6.text = item6; binding.unit6.text = unit6; binding.amount6.text =
            formatDouble(amount6)
        binding.item7.text = item7; binding.unit7.text = unit7; binding.amount7.text =
            formatDouble(amount7)
        binding.item8.text = item8; binding.unit8.text = unit8; binding.amount8.text =
            formatDouble(amount8)
        binding.item9.text = item9; binding.unit9.text = unit9; binding.amount9.text =
            formatDouble(amount9)
        binding.item10.text = item10; binding.unit10.text = unit10; binding.amount10.text =
            formatDouble(amount10)
        binding.total.text = displayCurrencyValue(getSumTotal())
        binding.invoiceNote.text = invoiceNote

        when (listType) {
            "In" -> {
                binding.footerDetails.visibility = View.VISIBLE
            }
            "Re" -> {
                binding.footerDetails.visibility = View.GONE
            }
        }

        hideEmptyRows()

        Handler(Looper.getMainLooper()).postDelayed({
            binding.imagePreview.setImageBitmap(makeLayoutBitmap(binding.previewLayout))
            binding.loadingPreview.visibility = View.GONE
            binding.imagePreviewFrame.visibility = View.VISIBLE
        }, 4000)
    }


    @SuppressLint("SetTextI18n")
    private fun bindForUpdate() {
        val clientName = navigationArgs.clientName
        val clientPhone = navigationArgs.clientPhone
        val clientEmail = navigationArgs.clientEmail
        val clientAddress = navigationArgs.clientAddress
        //val clientId = navigationArgs.clientId

        val date = navigationArgs.date
        val invoiceNumber = navigationArgs.invoiceNumber
        val item1 = navigationArgs.item1
        val unit1 = navigationArgs.unit1
        val amount1 = navigationArgs.amount1
        val item2 = navigationArgs.item2
        val unit2 = navigationArgs.unit2
        val amount2 = navigationArgs.amount2
        val item3 = navigationArgs.item3
        val unit3 = navigationArgs.unit3
        val amount3 = navigationArgs.amount3
        val item4 = navigationArgs.item4
        val unit4 = navigationArgs.unit4
        val amount4 = navigationArgs.amount4
        val item5 = navigationArgs.item5
        val unit5 = navigationArgs.unit5
        val amount5 = navigationArgs.amount5
        val item6 = navigationArgs.item6
        val unit6 = navigationArgs.unit6
        val amount6 = navigationArgs.amount6
        val item7 = navigationArgs.item7
        val unit7 = navigationArgs.unit7
        val amount7 = navigationArgs.amount7
        val item8 = navigationArgs.item8
        val unit8 = navigationArgs.unit8
        val amount8 = navigationArgs.amount8
        val item9 = navigationArgs.item9
        val unit9 = navigationArgs.unit9
        val amount9 = navigationArgs.amount9
        val item10 = navigationArgs.item10
        val unit10 = navigationArgs.unit10
        val amount10 = navigationArgs.amount10

        val listType = navigationArgs.listType
        val invoiceNote = navigationArgs.invoiceNote
        //val receiverNote = navigationArgs.receiverNote; val invoiceId = navigationArgs.invoiceId

        binding.clientNameTextView.text = clientName; binding.clientAddressTextView.text =
            clientAddress
        binding.clientPhoneTextView.text = clientPhone; binding.clientEmailTextView.text =
            clientEmail
        binding.invoiceNumber.text = invoiceNumber; binding.date.text =
            date; binding.currency.text = "($currency)"

        when (listType) {
            "In" -> {
                binding.invoiceTitle.text = resources.getString(R.string.invoice_title)
            }
            "Re" -> {
                binding.invoiceTitle.text = resources.getString(R.string.receipt_title)
            }
        }

        binding.item1.text = item1; binding.unit1.text = unit1; binding.amount1.text =
            formatDouble(amount1)
        binding.item2.text = item2; binding.unit2.text = unit2; binding.amount2.text =
            formatDouble(amount2)
        binding.item3.text = item3; binding.unit3.text = unit3; binding.amount3.text =
            formatDouble(amount3)
        binding.item4.text = item4; binding.unit4.text = unit4; binding.amount4.text =
            formatDouble(amount4)
        binding.item5.text = item5; binding.unit5.text = unit5; binding.amount5.text =
            formatDouble(amount5)
        binding.item6.text = item6; binding.unit6.text = unit6; binding.amount6.text =
            formatDouble(amount6)
        binding.item7.text = item7; binding.unit7.text = unit7; binding.amount7.text =
            formatDouble(amount7)
        binding.item8.text = item8; binding.unit8.text = unit8; binding.amount8.text =
            formatDouble(amount8)
        binding.item9.text = item9; binding.unit9.text = unit9; binding.amount9.text =
            formatDouble(amount9)
        binding.item10.text = item10; binding.unit10.text = unit10; binding.amount10.text =
            formatDouble(amount10)
        binding.total.text = displayCurrencyValue(getSumTotal())
        binding.invoiceNote.text = invoiceNote

        when (listType) {
            "In" -> {
                binding.footerDetails.visibility = View.VISIBLE
            }
            "Re" -> {
                binding.footerDetails.visibility = View.GONE
            }
        }

        hideEmptyRows()

        Handler(Looper.getMainLooper()).postDelayed({
            binding.imagePreview.setImageBitmap(makeLayoutBitmap(binding.previewLayout))
            binding.loadingPreview.visibility = View.GONE
            binding.imagePreviewFrame.visibility = View.VISIBLE
        }, 3000)

        binding.doneButton.setOnClickListener {
            updateInvoice()
            binding.discardButton.visibility = View.GONE
            binding.homeButton.visibility = View.VISIBLE
            Snackbar.make(
                requireView(),
                "${showInvoiceOrReceipt(navigationArgs.listType)} updated!",
                Snackbar.LENGTH_LONG
            ).show()
        }

        binding.backButton.setOnClickListener {
            updateInvoice()
            this.findNavController().navigateUp()
        }
    }

    private fun showInvoiceOrReceipt(type: String): String {
        return if (type == "In") {
            "Invoice"
        } else
            "Receipt"
    }

    private fun confirmInvoiceDiscardDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.discard_invoice_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                val action =
                    InvoicePreviewFragmentDirections.actionInvoicePreviewFragmentToInvoicesListFragment()
                this.findNavController().navigate(action)
            }
            .show()
    }

    private fun confirmReceiptDiscardDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.discard_receipt_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                val action =
                    InvoicePreviewFragmentDirections.actionInvoicePreviewFragmentToReceiptsListFragment()
                this.findNavController().navigate(action)
            }
            .show()
    }

    private fun formatDouble(value: String): String {
        return if (value.toDoubleOrNull() == null) {
            ""
        } else {
            val valueAsDouble = value.toDouble()
            val doubleDecimalFormat = DecimalFormat("00.00")
            doubleDecimalFormat.format(valueAsDouble).toString()
        }
    }

}