package com.example.priceelist.pricelist

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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.example.priceelist.data.Invoice
import com.example.priceelist.data.Pricelist
import com.example.priceelist.data.getFormattedPrice
import com.example.priceelist.databinding.FragmentShareDownloadListBinding
import com.google.android.material.snackbar.Snackbar
import java.io.FileOutputStream
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormat
import java.text.NumberFormat
import java.util.*


class ShareDownloadListFragment : Fragment() {

    private var _binding: FragmentShareDownloadListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PricelistViewModel by activityViewModels {
        PricelistViewModelFactory(
            (activity?.application as PricelistApplication).database.pricelistDao()
        )
    }

    private val statsViewModel: StatsViewModel by activityViewModels {
        StatsViewModelFactory(
            (activity?.application as PricelistApplication).database.statsDao()
        )
    }

    lateinit var item: Pricelist
    private val navigationArgs: ShareDownloadListFragmentArgs by navArgs()

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
        hideRow(binding.item11, binding.unit11, binding.amount11)
        hideRow(binding.item12, binding.unit12, binding.amount12)
        hideRow(binding.item13, binding.unit13, binding.amount13)
        hideRow(binding.item14, binding.unit14, binding.amount14)
        hideRow(binding.item15, binding.unit15, binding.amount15)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShareDownloadListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.listId

        viewModel.retrieveList(id).observe(this.viewLifecycleOwner) { selectedList ->
            item = selectedList
            bindToFields(item)
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

    @RequiresApi(Build.VERSION_CODES.Q)
    fun shareImage(bitmap: Bitmap) {
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
            //"${showInvoiceOrReceipt(binding.type.text.toString())} saved!",
            getString(R.string.saved_to_device),
            Snackbar.LENGTH_LONG
        ).show()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun bindToFields(item: Pricelist) {
        binding.apply {
            when (navigationArgs.shareOrDownload) {
                "download" -> {
                    binding.shareDownloadButton.text = resources.getString(R.string.download_list)
                }
                "share" -> {
                    binding.shareDownloadButton.text = resources.getString(R.string.share_list)
                }
            }

            title.text = item.dbListTitle
            currency.text = "($currencySymbol)"
            item1.text = item.dbItem1
            unit1.text = setToEmptyIfNull(item.dbQuantity1)
            amount1.text = setToEmptyIfNull(item.dbAmount1)
            item2.text = item.dbItem2
            unit2.text = setToEmptyIfNull(item.dbQuantity2)
            amount2.text = setToEmptyIfNull(item.dbAmount2)
            item3.text = item.dbItem3
            unit3.text = setToEmptyIfNull(item.dbQuantity3)
            amount3.text = setToEmptyIfNull(item.dbAmount3)
            item4.text = item.dbItem4
            unit4.text = setToEmptyIfNull(item.dbQuantity4)
            amount4.text = setToEmptyIfNull(item.dbAmount4)
            item5.text = item.dbItem5
            unit5.text = setToEmptyIfNull(item.dbQuantity5)
            amount5.text = setToEmptyIfNull(item.dbAmount5)
            item6.text = item.dbItem6
            unit6.text = setToEmptyIfNull(item.dbQuantity6)
            amount6.text = setToEmptyIfNull(item.dbAmount6)
            item7.text = item.dbItem7
            unit7.text = setToEmptyIfNull(item.dbQuantity7)
            amount7.text = setToEmptyIfNull(item.dbAmount7)
            item8.text = item.dbItem8
            unit8.text = setToEmptyIfNull(item.dbQuantity8)
            amount8.text = setToEmptyIfNull(item.dbAmount8)
            item9.text = item.dbItem9
            unit9.text = setToEmptyIfNull(item.dbQuantity9)
            amount9.text = setToEmptyIfNull(item.dbAmount9)
            item10.text = item.dbItem10
            unit10.text = setToEmptyIfNull(item.dbQuantity10)
            amount10.text = setToEmptyIfNull(item.dbAmount10)
            item11.text = item.dbItem11
            unit11.text = setToEmptyIfNull(item.dbQuantity11)
            amount11.text = setToEmptyIfNull(item.dbAmount11)
            item12.text = item.dbItem12
            unit12.text = setToEmptyIfNull(item.dbQuantity12)
            amount12.text = setToEmptyIfNull(item.dbAmount12)
            item13.text = item.dbItem13
            unit13.text = setToEmptyIfNull(item.dbQuantity13)
            amount13.text = setToEmptyIfNull(item.dbAmount13)
            item14.text = item.dbItem14
            unit14.text = setToEmptyIfNull(item.dbQuantity14)
            amount14.text = setToEmptyIfNull(item.dbAmount14)
            item15.text = item.dbItem15
            unit15.text = setToEmptyIfNull(item.dbQuantity15)
            amount15.text = setToEmptyIfNull(item.dbAmount15)
            total.text = item.getFormattedPrice()
            note.text = item.note
        }
        hideEmptyRows()
        //if (binding.note.text.toString().isBlank()) binding.note.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
            binding.imagePreview.setImageBitmap(makeLayoutBitmap(binding.previewFragmentLayout))


            binding.loadingPreview.visibility = View.GONE
            binding.bottomFrame.visibility = View.VISIBLE
            binding.imagePreviewFrame.visibility = View.VISIBLE
            binding.shareDownloadButton.setOnClickListener {
                downloadOrShare(item)
                //addSentStat(item)
            }
        }, 2500)
    }

    private val calendar = Calendar.getInstance().time
    private val calendarDate = DateFormat.getDateInstance().format(calendar).toString()

    private fun addSentStat(item: Pricelist) {
        statsViewModel.addStat(
            item.dbListTitle,
            "Li",
            "sent",
            calendarDate,
            item.dbTotal.toString()
        )
    }

    private fun addDownloadStat(item: Pricelist) {
        statsViewModel.addStat(
            item.dbListTitle,
            "Li",
            "downloaded",
            calendarDate,
            item.dbTotal.toString()
        )
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

                shareImage(makeLayoutBitmap(binding.previewFragmentLayout))
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

                saveImage(makeLayoutBitmap(binding.previewFragmentLayout))
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
            shareImage(makeLayoutBitmap(binding.previewFragmentLayout))
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
            saveImage(makeLayoutBitmap(binding.previewFragmentLayout))
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

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun downloadOrShare(item: Pricelist) {
        when (navigationArgs.shareOrDownload) {
            "download" -> {
                executeSaveAction()
                addDownloadStat(item)
            }
            "share" -> {
                executeShareAction()
                addSentStat(item)
            }
        }
    }

}