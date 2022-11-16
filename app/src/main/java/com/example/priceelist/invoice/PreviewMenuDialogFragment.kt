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
import com.example.priceelist.data.Invoice
import com.example.priceelist.databinding.FragmentPreviewMenuDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream


class PreviewMenuDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: InvoiceViewModel by activityViewModels {
        InvoiceViewModelFactory(
            (activity?.application as PricelistApplication).database.invoiceDao()
        )
    }

    private val statsViewModel: StatsViewModel by activityViewModels {
        StatsViewModelFactory(
            (activity?.application as PricelistApplication).database.statsDao()
        )
    }

    private var _binding: FragmentPreviewMenuDialogBinding? = null
    private val binding get() = _binding!!

    lateinit var item: Invoice
    private val navigationArgs: PreviewMenuDialogFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun bind(item: Invoice) {
        binding.apply {
            binding.menuTitle.text = item.invoiceNumber

            when (item.listType) {
                "In" -> {
                    binding.menuInvoiceIcon.setImageResource(R.drawable.ic_menu_invoice)
                    binding.type.text = getString(R.string.invoice_title)
                    binding.menuSendText.text = getString(R.string.invoice_menu_send)
                    binding.menuDownloadText.text = getString(R.string.invoice_menu_download)
                }
                "Re" -> {
                    binding.menuInvoiceIcon.setImageResource(R.drawable.ic_menu_receipt)
                    binding.type.text = getString(R.string.receipt_title)
                    binding.menuSendText.text = getString(R.string.receipt_menu_send)
                    binding.menuDownloadText.text = getString(R.string.receipt_menu_download)
                }
            }

            menuDownload.setOnClickListener {
                executeSaveAction()
                //findNavController().navigateUp()
                addDownloadStat(item)
            }

            menuSend.setOnClickListener {
                executeShareAction()
                //findNavController().navigateUp()
                addSentStat(item)
            }

            menuSendNew.setOnClickListener {
                val action =
                    PreviewMenuDialogFragmentDirections.actionPreviewMenuDialogFragmentToInvoiceNewClientFragment(
                        item.id
                    )
                findNavController().navigate(action)
            }

        }
    }

    private fun addSentStat(item: Invoice) {
        if (item.listType == "In") {
            statsViewModel.addStat(
                item.invoiceNumber,
                "In",
                "sent",
                item.date,
                item.invoiceTotal.toString()
            )
        } else {
            statsViewModel.addStat(
                item.invoiceNumber,
                "In",
                "sent",
                item.date,
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPreviewMenuDialogBinding.inflate(inflater, container, false)
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
                bind(item)
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

    private fun showInvoiceOrReceipt(type: String): String {
        return if (type == "Invoice") {
            "Invoice"
        } else
            "Receipt"
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