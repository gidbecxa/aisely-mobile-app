package com.example.priceelist.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.priceelist.PricelistApplication
import com.example.priceelist.R
import com.example.priceelist.data.Account
import com.example.priceelist.databinding.FragmentAccountBinding


class AccountFragment : Fragment() {

    private val viewModel: AccountViewModel by activityViewModels {
        AccountViewModelFactory(
            (activity?.application as PricelistApplication).database.accountDao()
        )
    }

    private var _binding:FragmentAccountBinding? = null
    private val binding get() = _binding!!

    lateinit var item: Account

    fun getNameInitial(name: String): Char {
        return name.first()
    }

    private fun bind(item: Account) {
        binding.apply {
            binding.profilePicInitial.text = getNameInitial(item.businessName).toString()
            businessName.text = item.businessName
            //businessName.setTextColor(#1C1C1C)
            businessCategory.text = item.businessCategory
            subscriptionType.text = item.subscription
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserData().observe(this.viewLifecycleOwner) {user ->
            if (user != null) {
                binding.emptyView.visibility = View.GONE
                binding.fab.visibility = View.GONE

                item = user
                bind(item)
            } else {
                binding.mainView.visibility = View.GONE
            }
        }

        binding.basicInformationLayout.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToAccountInformationFragment(
                "basic"
            )
            this.findNavController().navigate(action)
        }

        binding.paymentInformationLayout.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToAccountInformationFragment(
                "payment"
            )
            this.findNavController().navigate(action)
        }

        binding.statsLayout.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToStatsFragment()
            this.findNavController().navigate(action)
        }

        binding.fab.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToCreateAccountFragment()
            this.findNavController().navigate(action)
        }

        binding.backButton.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }
}