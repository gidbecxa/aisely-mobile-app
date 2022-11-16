package com.example.priceelist.account

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.priceelist.PricelistApplication
import com.example.priceelist.data.Account
import com.example.priceelist.databinding.FragmentAccountInformationBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class AccountInformationFragment : Fragment() {

    private val viewModel: AccountViewModel by activityViewModels {
        AccountViewModelFactory(
            (activity?.application as PricelistApplication).database.accountDao()
        )
    }

    private var _binding: FragmentAccountInformationBinding? = null
    private val binding get() = _binding!!

    lateinit var item: Account
    private val navigationArgs: AccountInformationFragmentArgs by navArgs()

    private fun bindBasicInfo(item: Account) {
        binding.apply {
            businessName.setText(item.businessName, TextView.BufferType.SPANNABLE)
            businessCategory.setText(item.businessCategory, TextView.BufferType.SPANNABLE)
            businessPhone.setText(item.businessPhone1, TextView.BufferType.SPANNABLE)
            businessPhone2.setText(item.businessPhone2, TextView.BufferType.SPANNABLE)
            businessEmail.setText(item.businessMail, TextView.BufferType.SPANNABLE)
            businessAddress.setText(item.businessAddress, TextView.BufferType.SPANNABLE)

            hidePhone2(item)
            displayAnotherPhone(binding.businessPhone2, binding.addAnother)
            displaySave()

            binding.save.setOnClickListener {
                showSnackBar()
                updateBasicInfo()
            }
        }
    }

    private fun bindPaymentInfo(item: Account) {
        binding.apply {
            institutionName.setText(item.institutionName, TextView.BufferType.SPANNABLE)
            paymentName.setText(item.bankAccName, TextView.BufferType.SPANNABLE)
            paymentId.setText(item.bankAccNo, TextView.BufferType.SPANNABLE)

            displaySave()

            binding.save.setOnClickListener {
                updatePaymentInfo()
                Snackbar.make(
                    requireView(),
                    "Account updated!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun showSnackBar() {
        when {
            binding.businessName.text.toString().isBlank() -> {
                Snackbar.make(
                    binding.coordinatorLayout,
                    "business name is required",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            binding.businessPhone.text.toString().isBlank() -> {
                Snackbar.make(
                    binding.coordinatorLayout,
                    "A business phone number is required",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            binding.businessAddress.text.toString().isBlank() -> {
                Snackbar.make(
                    binding.coordinatorLayout,
                    "business address is required",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            else -> {
                Snackbar.make(
                    requireView(),
                    "Account updated!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun isSavePossible(): Boolean {
        return viewModel.isInsertUserPossible(
            binding.businessName.text.toString(),
            binding.businessPhone.text.toString(),
            binding.businessAddress.text.toString()
        )
    }

    private fun updateBasicInfo() {
        if (isSavePossible()) {
            viewModel.updateBasicInfoData(
                binding.businessName.text.toString(),
                binding.businessCategory.text.toString(),
                binding.businessPhone.text.toString(),
                binding.businessPhone2.text.toString(),
                binding.businessEmail.text.toString(),
                binding.businessAddress.text.toString()
            )
            //val action = AccountInformationFragmentDirections.actionAccountInformationFragmentToAccountFragment()
            //this.findNavController().navigate(action)
        }
    }

    private fun updatePaymentInfo() {
        viewModel.updatePaymentInfoData(
            binding.institutionName.text.toString(),
            binding.paymentName.text.toString(),
            binding.paymentId.text.toString()
        )
        //val action = AccountInformationFragmentDirections.actionAccountInformationFragmentToAccountFragment()
        //this.findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (navigationArgs.section) {
            "basic" -> {
                binding.paymentInfoSection.visibility = View.GONE

                viewModel.getUserData().observe(this.viewLifecycleOwner) { user ->
                    item = user
                    bindBasicInfo(item)
                }
            }
            "payment" -> {
                binding.basicInfoSection.visibility = View.GONE

                viewModel.getUserData().observe(this.viewLifecycleOwner) { user ->
                    item = user
                    bindPaymentInfo(item)
                }
            }
        }

        binding.backButton.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun displaySave() {
        binding.businessName.doAfterTextChanged {
            binding.save.visibility = View.VISIBLE
        }
        binding.businessCategory.doAfterTextChanged {
            binding.save.visibility = View.VISIBLE
        }
        binding.businessPhone.doAfterTextChanged {
            binding.save.visibility = View.VISIBLE
        }
        binding.businessPhone2.doAfterTextChanged {
            binding.save.visibility = View.VISIBLE
        }
        binding.businessEmail.doAfterTextChanged {
            binding.save.visibility = View.VISIBLE
        }
        binding.businessAddress.doAfterTextChanged {
            binding.save.visibility = View.VISIBLE
        }
        binding.institutionName.doAfterTextChanged {
            binding.save.visibility = View.VISIBLE
        }
        binding.paymentName.doAfterTextChanged {
            binding.save.visibility = View.VISIBLE
        }
        binding.paymentId.doAfterTextChanged {
            binding.save.visibility = View.VISIBLE
        }
    }

    private fun displayAnotherPhone(textField: TextInputEditText, addAnother: TextView) {
        if (textField.visibility == View.GONE) {
            addAnother.setOnClickListener {
                textField.visibility = View.VISIBLE
                //binding.divider18.visibility = View.VISIBLE
                addAnother.visibility = View.GONE
            }
        }
    }

    private fun hidePhone2(item: Account) {
        if (item.businessPhone2.isBlank()) {
            binding.businessPhone2.visibility = View.GONE
            //binding.divider18.visibility = View.GONE
            binding.addAnother.visibility = View.VISIBLE
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