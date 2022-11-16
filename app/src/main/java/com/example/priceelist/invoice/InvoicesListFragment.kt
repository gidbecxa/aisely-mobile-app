package com.example.priceelist.invoice

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
import com.example.priceelist.R
import com.example.priceelist.account.AccountViewModel
import com.example.priceelist.account.AccountViewModelFactory
import com.example.priceelist.account.StatsViewModel
import com.example.priceelist.account.StatsViewModelFactory
import com.example.priceelist.data.Invoice
import com.example.priceelist.databinding.FragmentInvoicesListBinding
import java.text.DateFormat
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class InvoicesListFragment : Fragment() {

    private val viewModel: InvoiceViewModel by activityViewModels {
        InvoiceViewModelFactory(
            (activity?.application as PricelistApplication).database.invoiceDao()
        )
    }

    private val accountViwModel: AccountViewModel by activityViewModels {
        AccountViewModelFactory(
            (activity?.application as PricelistApplication).database.accountDao()
        )
    }

    private val statsViewModel: StatsViewModel by activityViewModels {
        StatsViewModelFactory(
            (activity?.application as PricelistApplication).database.statsDao()
        )
    }

    private var _binding: FragmentInvoicesListBinding? = null
    private val binding get() = _binding!!

    lateinit var item: Invoice

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInvoicesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = InvoicesListAdapter({
            val action =
                InvoicesListFragmentDirections.actionInvoicesListFragmentToPreviewFragment(
                    it.id
                )
            this.findNavController().navigate(action)
        },
            {
                //val fileName = it.fileName
                Handler(Looper.getMainLooper()).postDelayed({
                    val action =
                        InvoicesListFragmentDirections.actionInvoicesListFragmentToInvoiceMenuDialogFragment(
                            it.id, it.fileName
                        )
                    this.findNavController().navigate(action)
                }, 500)
            })

        binding.recyclerView.adapter = adapter
        viewModel.allInvoices.observe(this.viewLifecycleOwner) { invoices ->
            invoices.let {
                adapter.submitList(it)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.setHasFixedSize(true)

        accountViwModel.getUserData().observe(this.viewLifecycleOwner) { user ->
            if (user == null) {
                binding.fab.visibility = View.GONE
                //binding.title.visibility = View.GONE
                binding.fabNewUser.setOnClickListener {
                    val action =
                        InvoicesListFragmentDirections.actionInvoicesListFragmentToAccountFragment()
                    this.findNavController().navigate(action)
                }
            } else {
                viewModel.getLastInvoice().observe(this.viewLifecycleOwner) { invoice ->
                    if (invoice == null) {
                        //binding.title.visibility = View.GONE
                        binding.fab.visibility = View.VISIBLE
                        binding.fabNewUser.visibility = View.GONE
                        binding.toGetStarted.visibility = View.GONE

                        binding.fab.setOnClickListener {
                            val action =
                                InvoicesListFragmentDirections.actionInvoicesListFragmentToInvoiceClientInfoFragment(
                                    "In"
                                )
                            this.findNavController().navigate(action)
                        }
                    } else {
                        //binding.title.visibility = View.VISIBLE
                        binding.fab.visibility = View.VISIBLE
                        binding.emptyView.visibility = View.GONE

                        binding.fab.setOnClickListener {
                            val action =
                                InvoicesListFragmentDirections.actionInvoicesListFragmentToInvoiceClientInfoFragment(
                                    "In"
                                )
                            this.findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }

}