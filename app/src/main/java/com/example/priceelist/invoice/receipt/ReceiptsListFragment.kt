package com.example.priceelist.invoice.receipt

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.priceelist.PricelistApplication
import com.example.priceelist.account.AccountViewModel
import com.example.priceelist.account.AccountViewModelFactory
import com.example.priceelist.data.Invoice
import com.example.priceelist.databinding.FragmentReceiptsListBinding
import com.example.priceelist.invoice.InvoiceViewModel
import com.example.priceelist.invoice.InvoiceViewModelFactory
import com.example.priceelist.invoice.InvoicesListFragmentDirections

/**
 * A fragment representing a list of Items.
 */
class ReceiptsListFragment : Fragment() {

    val viewModel: InvoiceViewModel by activityViewModels {
        InvoiceViewModelFactory(
            (activity?.application as PricelistApplication).database.invoiceDao()
        )
    }

    private val accountViwModel: AccountViewModel by activityViewModels {
        AccountViewModelFactory(
            (activity?.application as PricelistApplication).database.accountDao()
        )
    }

    private var _binding: FragmentReceiptsListBinding? = null
    private val binding get() = _binding!!

    lateinit var item: Invoice

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReceiptsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ReceiptsListAdapter({
            val action =
                ReceiptsListFragmentDirections.actionReceiptsListFragmentToPreviewFragment(it.id)
            this.findNavController().navigate(action)
        },
            {
                val fileName = it.fileName
                Handler(Looper.getMainLooper()).postDelayed({
                    val action =
                        ReceiptsListFragmentDirections.actionReceiptsListFragmentToInvoiceMenuDialogFragment(
                            it.id, fileName
                        )
                    this.findNavController().navigate(action)
                }, 500)
            })

        binding.recyclerView.adapter = adapter
        viewModel.allReceipts.observe(this.viewLifecycleOwner) { receipts ->
            receipts.let {
                adapter.submitList(it)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.setHasFixedSize(true)

        accountViwModel.getUserData().observe(this.viewLifecycleOwner) { user ->
            if (user == null) {
                //binding.title.visibility = View.GONE
                binding.fab.visibility = View.GONE
                binding.fabNewUser.setOnClickListener {
                    val action =
                        ReceiptsListFragmentDirections.actionReceiptsListFragmentToAccountFragment()
                    this.findNavController().navigate(action)
                }
            } else {
                viewModel.getLastReceipt().observe(this.viewLifecycleOwner) { receipt ->
                    if (receipt == null) {
                        //binding.title.visibility = View.GONE
                        binding.fab.visibility = View.VISIBLE
                        binding.fabNewUser.visibility = View.GONE
                        binding.toGetStarted.visibility = View.GONE

                        binding.fab.setOnClickListener {
                            val action =
                                ReceiptsListFragmentDirections.actionReceiptsListFragmentToInvoiceClientInfoFragment(
                                    "Re"
                                )
                            this.findNavController().navigate(action)
                        }
                    } else {
                        //binding.title.visibility = View.VISIBLE
                        binding.fab.visibility = View.VISIBLE
                        binding.emptyView.visibility = View.GONE

                        binding.fab.setOnClickListener {
                            val action =
                                ReceiptsListFragmentDirections.actionReceiptsListFragmentToInvoiceClientInfoFragment(
                                    "Re"
                                )
                            this.findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }

}