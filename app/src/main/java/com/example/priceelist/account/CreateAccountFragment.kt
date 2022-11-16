package com.example.priceelist.account

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.priceelist.PricelistApplication
import com.example.priceelist.R
import com.example.priceelist.data.Account
import com.example.priceelist.databinding.FragmentCreateAccountBinding
import com.google.android.material.snackbar.Snackbar

class CreateAccountFragment : Fragment() {
    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AccountViewModel by activityViewModels {
        AccountViewModelFactory(
            (activity?.application as PricelistApplication).database.accountDao()
        )
    }

    lateinit var item: Account

    private fun isInsertPossible(): Boolean {
        return viewModel.isInsertUserPossible(
            binding.businessName.text.toString(),
            binding.businessPhone.text.toString(),
            binding.businessAddress.text.toString()
        )
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
                    "business phone number is required",
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
        }
    }

    private fun insertUser() {
        if (isInsertPossible()) {
            viewModel.insertUserData(
                binding.businessName.text.toString(),
                binding.businessCategory.text.toString(),
                binding.businessPhone.text.toString(),
                binding.businessPhone2.text.toString(),
                binding.businessEmail.text.toString(),
                binding.businessAddress.text.toString(),
                binding.institutionName.text.toString(),
                binding.paymentName.text.toString(),
                binding.paymentId.text.toString()
            )
            val action =
                CreateAccountFragmentDirections.actionCreateAccountFragmentToAccountFragment()
            this.findNavController().navigate(action)
        }
    }

    private val categories = listOf(
        "Advertising/Marketing",
        "Amateur Sports Team",
        "Appliance Repair/Maintenance Service",
        "Art",
        "Arts & Entertainment",
        "Automotive Repair Shop",
        "Baby & Children’s Clothing Store",
        "Baby Goods/Kids Goods",
        "Banking",
        "Bar",
        "Beauty, Cosmetic & Personal Care",
        "Beauty Salon & Spa",
        "Bookshop/Bookstore",
        "Building & Construction",
        "Business Center",
        "Business Service",
        "Christian Organisation",
        "Clothing (Brand)",
        "Clothing Store",
        "College & University",
        "Commercial & Industrial",
        "Commercial Bank",
        "Commercial Equipment",
        "Community Organization",
        "Consulting Agency",
        "Consumer Electronics",
        "Content Creator",
        "Contractor",
        "Convenience Store",
        "Credit Union",
        "Dance/Choreography",
        "Design",
        "E-commerce",
        "Education",
        "Engineering Service",
        "Entertainment",
        "Entrepreneurship",
        "Event Planning & Management",
        "Fashion Design",
        "Fashion Model",
        "Fashion Store",
        "Fast Food Restaurant",
        "Financial Service",
        "Food & Beverage",
        "Government Organization",
        "Graphic & Digital Design",
        "Grocery Store",
        "Health/Beauty",
        "Heating, Ventilating & Air Conditioning Service",
        "Home Decor",
        "Hospital & Health Care",
        "Hotel & Lodging",
        "In-Home Service",
        "Industrial Company",
        "Information Technology Company",
        "Insurance Company",
        "Internet Company",
        "Internet Marketing Service",
        "Jewelry & Watches Company",
        "Journalism",
        "Kitchen/Cooking",
        "Lawyer & Law Firm",
        "Library",
        "Loan Service",
        "Local Service",
        "Marketing Agency",
        "Media",
        "Media/News Company",
        "Mobile Development",
        "Movie",
        "Musician/Band",
        "News & Media Website",
        "Nonprofit Organization",
        "Non-Governmental Organization (NGO)",
        "Office Supplies",
        "Other",
        "Outdoor & Sporting Goods Company",
        "Personal Blog",
        "Photography",
        "Product/Service",
        "Public & Government Service",
        "Public Figure",
        "Public Utility Company",
        "Real Estate Agency",
        "Real Estate Developer",
        "Record Label",
        "Religious Organization",
        "Restaurant Service",
        "School",
        "Science, Technology & Engineering",
        "Shopping & Retail",
        "Software Development",
        "Sports & Recreation",
        "Teens & Kids Website",
        "Telemarketing Service",
        "Tutor/Teacher",
        "TV Channel",
        "University & Tertiary",
        "Veterinary",
        "Video & Game Creator",
        "Videography",
        "Web Design & Development",
        "Women’s Clothing Store",
        "Writer"

    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_list_item, categories)
        (binding.businessCategoryLayout.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.save.setOnClickListener {
            showSnackBar()
            insertUser()
        }

        binding.backButton.setOnClickListener {
            this.findNavController().navigateUp()
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