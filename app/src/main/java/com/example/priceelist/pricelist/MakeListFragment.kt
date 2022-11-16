package com.example.priceelist.pricelist

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.priceelist.PricelistApplication
import com.example.priceelist.R
import com.example.priceelist.data.Pricelist
import com.example.priceelist.data.getFormattedPrice
import com.example.priceelist.databinding.FragmentMakeListBinding
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormat.getDateInstance
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class MakeListFragment : Fragment() {

    private val viewModel: PricelistViewModel by activityViewModels {
        PricelistViewModelFactory(
            (activity?.application as PricelistApplication).database.pricelistDao()
        )
    }

    lateinit var item: Pricelist //used to store information about a single entity

    private val navigationArgs: MakeListFragmentArgs by navArgs()

    private val calendar = Calendar.getInstance().time
    private val calendarDate = getDateInstance().format(calendar).toString()

    @SuppressLint("SimpleDateFormat")
    private val dateDigits = SimpleDateFormat("ddMMyyyy").format(calendar).toString()
    private val defaultTitle: String = "Priceelist $dateDigits"
    private val listTypePricelist: String = "Li"

    private var _binding: FragmentMakeListBinding? = null
    private val binding get() = _binding!!

    private fun isSavePossible(): Boolean {
        return viewModel.isSavePossible(
            binding.item1.text.toString(),
            binding.unit1.text.toString(),
            binding.amount1.text.toString(),
            binding.item2.text.toString(),
            binding.unit2.text.toString(),
            binding.amount2.text.toString(),
            binding.item3.text.toString(),
            binding.unit3.text.toString(),
            binding.amount3.text.toString(),
            binding.item4.text.toString(),
            binding.unit4.text.toString(),
            binding.amount4.text.toString(),
            binding.item5.text.toString(),
            binding.unit5.text.toString(),
            binding.amount5.text.toString(),
            binding.item6.text.toString(),
            binding.unit6.text.toString(),
            binding.amount6.text.toString(),
            binding.item7.text.toString(),
            binding.unit7.text.toString(),
            binding.amount7.text.toString(),
            binding.item8.text.toString(),
            binding.unit8.text.toString(),
            binding.amount8.text.toString(),
            binding.item9.text.toString(),
            binding.unit9.text.toString(),
            binding.amount9.text.toString(),
            binding.item10.text.toString(),
            binding.unit10.text.toString(),
            binding.amount10.text.toString(),
            binding.item11.text.toString(),
            binding.unit11.text.toString(),
            binding.amount11.text.toString(),
            binding.item12.text.toString(),
            binding.unit12.text.toString(),
            binding.amount12.text.toString(),
            binding.item13.text.toString(),
            binding.unit13.text.toString(),
            binding.amount13.text.toString(),
            binding.item14.text.toString(),
            binding.unit14.text.toString(),
            binding.amount14.text.toString(),
            binding.item15.text.toString(),
            binding.unit15.text.toString(),
            binding.amount15.text.toString()
        )
    }

    //The ADD NEW LIST Section : Functions for adding a new list begin here

    /**
     * The first getSumTotal() function (commented) functioned appropriately.
     *It is efficient, in that it is short.
     * However, a major issue is that, when the total textview is blank i.e. as long as the user
     * has not yet displayed the total field by pressing the total button; the total obtained and
     * saved into the db is 0.00, irrespective of the amount(s) the user's list contains.
     * This is not a good logic however, the system must get the sum total of the
     * user's list whether he/she displays the total field or not, hence the second and longer
     * function.
     */
    /*private fun getSumTotal(): String {
        var sumTotalString = binding.total.text.toString()
        //return if (sumTotalString == null) {
        return if (sumTotalString == "") {
            sumTotalString = "0.0"
            sumTotalString
        } else {
        //use a Regex object to remove currency symbols and replace comma with a dot
            val reg = Regex("[^0-9,.]")
            val sumTotalString2 = reg.replace(sumTotalString, "")
            val reg2 = Regex("[^0-9]")
            val sumTotalString3 = reg2.replace(sumTotalString2, ".")
            sumTotalString3
        }
    }*/

    private fun getSumTotal(): String {
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

        var amount11 = binding.amount11.text.toString().toDoubleOrNull()
        if (amount11 == null) {
            amount11 = 0.0
        }

        var unit11 = binding.unit11.text.toString().toDoubleOrNull()
        if (unit11 == null) {
            unit11 = 1.0
        }

        val finalAmount11 = amount11 * unit11
        sumTotal += finalAmount11

        var amount12 = binding.amount12.text.toString().toDoubleOrNull()
        if (amount12 == null) {
            amount12 = 0.0
        }

        var unit12 = binding.unit12.text.toString().toDoubleOrNull()
        if (unit12 == null) {
            unit12 = 1.0
        }

        val finalAmount12 = amount12 * unit12
        sumTotal += finalAmount12

        var amount13 = binding.amount13.text.toString().toDoubleOrNull()
        if (amount13 == null) {
            amount13 = 0.0
        }

        var unit13 = binding.unit13.text.toString().toDoubleOrNull()
        if (unit13 == null) {
            unit13 = 1.0
        }

        val finalAmount13 = amount13 * unit13
        sumTotal += finalAmount13

        var amount14 = binding.amount14.text.toString().toDoubleOrNull()
        if (amount14 == null) {
            amount14 = 0.0
        }

        var unit14 = binding.unit14.text.toString().toDoubleOrNull()
        if (unit14 == null) {
            unit14 = 1.0
        }

        val finalAmount14 = amount14 * unit14
        sumTotal += finalAmount14

        var amount15 = binding.amount15.text.toString().toDoubleOrNull()
        if (amount15 == null) {
            amount15 = 0.0
        }

        var unit15 = binding.unit15.text.toString().toDoubleOrNull()
        if (unit15 == null) {
            unit15 = 1.0
        }

        val finalAmount15 = amount15 * unit15
        sumTotal += finalAmount15

        return sumTotal.toString()
    }

    /**
     * The function below checks whether the title field is empty. If it is, the system generates
     * a default title.
     * Previously the expression { if (listTitle == "") } was used. This works fine when the user
     * enters nothing into the field. But in a case where the user enters no visible character i.e.
     * when the user has only entered a few white spaces, the system saves the white spaces still.
     * Thus, the list displayed on the home page has no readable title. This is bad for UX as well.
     * Hence, isBlank() fun is used to avoid empty or only white space entries
     */
    private fun createListTitle(): String {
        var listTitle: String = binding.title.text.toString()
        return if (listTitle.isBlank()) {
            listTitle = defaultTitle
            listTitle
        } else {
            listTitle
        }
    }

    private fun addNewList() {
        /**
         * Wrapping the addNewList in the if isSavePossible() makes it impossible for the user to save
         * a list if empty i.e. no entry at all has een made. Hence, the button is diasbled (by default).
         */
        if (isSavePossible()) {

            viewModel.addnewList(
                createListTitle(),
                calendarDate,
                binding.item1.text.toString(),
                binding.unit1.text.toString(),
                binding.amount1.text.toString(),
                binding.item2.text.toString(),
                binding.unit2.text.toString(),
                binding.amount2.text.toString(),
                binding.item3.text.toString(),
                binding.unit3.text.toString(),
                binding.amount3.text.toString(),
                binding.item4.text.toString(),
                binding.unit4.text.toString(),
                binding.amount4.text.toString(),
                binding.item5.text.toString(),
                binding.unit5.text.toString(),
                binding.amount5.text.toString(),
                binding.item6.text.toString(),
                binding.unit6.text.toString(),
                binding.amount6.text.toString(),
                binding.item7.text.toString(),
                binding.unit7.text.toString(),
                binding.amount7.text.toString(),
                binding.item8.text.toString(),
                binding.unit8.text.toString(),
                binding.amount8.text.toString(),
                binding.item9.text.toString(),
                binding.unit9.text.toString(),
                binding.amount9.text.toString(),
                binding.item10.text.toString(),
                binding.unit10.text.toString(),
                binding.amount10.text.toString(),
                binding.item11.text.toString(),
                binding.unit11.text.toString(),
                binding.amount11.text.toString(),
                binding.item12.text.toString(),
                binding.unit12.text.toString(),
                binding.amount12.text.toString(),
                binding.item13.text.toString(),
                binding.unit13.text.toString(),
                binding.amount13.text.toString(),
                binding.item14.text.toString(),
                binding.unit14.text.toString(),
                binding.amount14.text.toString(),
                binding.item15.text.toString(),
                binding.unit15.text.toString(),
                binding.amount15.text.toString(),
                getSumTotal(),
                listTypePricelist, binding.addNoteText.text.toString()
            )
            Handler(Looper.getMainLooper()).postDelayed({
                val action = MakeListFragmentDirections.actionMakeListFragmentToSavedListsFragment()
                findNavController().navigate(action)
            }, 100)
        }
    }

    //The ADD NEW LIST Section ends ...

    //NEW SECTION! The next Section binds the entries from the db
    // to the fragment's fields for editing...

    private val emptyString: String = "" //to bind to fields that are null in the db...
    // implemented this as a solution to the "null" which the app displayed previously

    //previously used the commented function (below) ...
    // before recreating it as the one below. It'd require creating for all the fields which could
    //return null. Hence, a really long process.
    /**private fun setToEmptyIfNull5a (item: Pricelist): String {
    return if (item.dbQuantity5.toString() == "null") {
    emptyString
    } else {
    item.dbQuantity5.toString()
    }
    }*/

    private fun formatDouble(double: Double): String {

        //val doubleDecimalFormat = DecimalFormat("00.00")
        //val doubleToDecimal = doubleDecimalFormat.format(double)
        return BigDecimal(double).setScale(2, RoundingMode.HALF_UP).toString()
    }

    //this function simplifies the process of converting the null string returned by the db
    //Unlike the fun above, by taking a parameter, it could be used throughout the fields to bind
    private fun setToEmptyIfNull(dbField: Double?): String {
        return if (dbField.toString() == "null") {
            emptyString
        } else {

            formatDouble(dbField!!.toDouble())
        }
    }

    private fun bindToFields(item: Pricelist) {
        binding.apply {
            title.setText(item.dbListTitle, TextView.BufferType.SPANNABLE)

            item1.setText(item.dbItem1, TextView.BufferType.SPANNABLE)
            unit1.setText(setToEmptyIfNull(item.dbQuantity1), TextView.BufferType.SPANNABLE)
            amount1.setText(setToEmptyIfNull(item.dbAmount1), TextView.BufferType.SPANNABLE)

            item2.setText(item.dbItem2, TextView.BufferType.SPANNABLE)
            unit2.setText(setToEmptyIfNull(item.dbQuantity2), TextView.BufferType.SPANNABLE)
            amount2.setText(setToEmptyIfNull(item.dbAmount2), TextView.BufferType.SPANNABLE)

            item3.setText(item.dbItem3, TextView.BufferType.SPANNABLE)
            unit3.setText(setToEmptyIfNull(item.dbQuantity3), TextView.BufferType.SPANNABLE)
            amount3.setText(setToEmptyIfNull(item.dbAmount3), TextView.BufferType.SPANNABLE)

            item4.setText(item.dbItem4, TextView.BufferType.SPANNABLE)
            unit4.setText(setToEmptyIfNull(item.dbQuantity4), TextView.BufferType.SPANNABLE)
            amount4.setText(setToEmptyIfNull(item.dbAmount4), TextView.BufferType.SPANNABLE)

            item5.setText(item.dbItem5, TextView.BufferType.SPANNABLE)
            unit5.setText(setToEmptyIfNull(item.dbQuantity5), TextView.BufferType.SPANNABLE)
            amount5.setText(setToEmptyIfNull(item.dbAmount5), TextView.BufferType.SPANNABLE)

            item6.setText(item.dbItem6, TextView.BufferType.SPANNABLE)
            unit6.setText(setToEmptyIfNull(item.dbQuantity6), TextView.BufferType.SPANNABLE)
            amount6.setText(setToEmptyIfNull(item.dbAmount6), TextView.BufferType.SPANNABLE)

            item7.setText(item.dbItem7, TextView.BufferType.SPANNABLE)
            unit7.setText(setToEmptyIfNull(item.dbQuantity7), TextView.BufferType.SPANNABLE)
            amount7.setText(setToEmptyIfNull(item.dbAmount7), TextView.BufferType.SPANNABLE)

            item8.setText(item.dbItem8, TextView.BufferType.SPANNABLE)
            unit8.setText(setToEmptyIfNull(item.dbQuantity8), TextView.BufferType.SPANNABLE)
            amount8.setText(setToEmptyIfNull(item.dbAmount8), TextView.BufferType.SPANNABLE)

            item9.setText(item.dbItem9, TextView.BufferType.SPANNABLE)
            unit9.setText(setToEmptyIfNull(item.dbQuantity9), TextView.BufferType.SPANNABLE)
            amount9.setText(setToEmptyIfNull(item.dbAmount9), TextView.BufferType.SPANNABLE)

            item10.setText(item.dbItem10, TextView.BufferType.SPANNABLE)
            unit10.setText(setToEmptyIfNull(item.dbQuantity10), TextView.BufferType.SPANNABLE)
            amount10.setText(setToEmptyIfNull(item.dbAmount10), TextView.BufferType.SPANNABLE)

            item11.setText(item.dbItem11, TextView.BufferType.SPANNABLE)
            unit11.setText(setToEmptyIfNull(item.dbQuantity11), TextView.BufferType.SPANNABLE)
            amount11.setText(setToEmptyIfNull(item.dbAmount11), TextView.BufferType.SPANNABLE)

            item12.setText(item.dbItem12, TextView.BufferType.SPANNABLE)
            unit12.setText(setToEmptyIfNull(item.dbQuantity12), TextView.BufferType.SPANNABLE)
            amount12.setText(setToEmptyIfNull(item.dbAmount12), TextView.BufferType.SPANNABLE)

            item13.setText(item.dbItem13, TextView.BufferType.SPANNABLE)
            unit13.setText(setToEmptyIfNull(item.dbQuantity13), TextView.BufferType.SPANNABLE)
            amount13.setText(setToEmptyIfNull(item.dbAmount13), TextView.BufferType.SPANNABLE)

            item14.setText(item.dbItem14, TextView.BufferType.SPANNABLE)
            unit14.setText(setToEmptyIfNull(item.dbQuantity14), TextView.BufferType.SPANNABLE)
            amount14.setText(setToEmptyIfNull(item.dbAmount14), TextView.BufferType.SPANNABLE)

            item15.setText(item.dbItem15, TextView.BufferType.SPANNABLE)
            unit15.setText(setToEmptyIfNull(item.dbQuantity15), TextView.BufferType.SPANNABLE)
            amount15.setText(setToEmptyIfNull(item.dbAmount15), TextView.BufferType.SPANNABLE)

            total.setText(item.getFormattedPrice(), TextView.BufferType.SPANNABLE)
            addNoteText.setText(item.note, TextView.BufferType.SPANNABLE)

            binding.doneButton.setOnClickListener { updateList() }
        }
    }
    //The BINDING Section ends here...
    // next below is the UPDATE Section which links with the BINDING Section tho...

    private fun updateList() {

        if (isSavePossible()) {
            viewModel.updateList(
                this.navigationArgs.itemId,
                createListTitle(),
                calendarDate,
                binding.item1.text.toString(),
                binding.unit1.text.toString(),
                binding.amount1.text.toString(),
                binding.item2.text.toString(),
                binding.unit2.text.toString(),
                binding.amount2.text.toString(),
                binding.item3.text.toString(),
                binding.unit3.text.toString(),
                binding.amount3.text.toString(),
                binding.item4.text.toString(),
                binding.unit4.text.toString(),
                binding.amount4.text.toString(),
                binding.item5.text.toString(),
                binding.unit5.text.toString(),
                binding.amount5.text.toString(),
                binding.item6.text.toString(),
                binding.unit6.text.toString(),
                binding.amount6.text.toString(),
                binding.item7.text.toString(),
                binding.unit7.text.toString(),
                binding.amount7.text.toString(),
                binding.item8.text.toString(),
                binding.unit8.text.toString(),
                binding.amount8.text.toString(),
                binding.item9.text.toString(),
                binding.unit9.text.toString(),
                binding.amount9.text.toString(),
                binding.item10.text.toString(),
                binding.unit10.text.toString(),
                binding.amount10.text.toString(),
                binding.item11.text.toString(),
                binding.unit11.text.toString(),
                binding.amount11.text.toString(),
                binding.item12.text.toString(),
                binding.unit12.text.toString(),
                binding.amount12.text.toString(),
                binding.item13.text.toString(),
                binding.unit13.text.toString(),
                binding.amount13.text.toString(),
                binding.item14.text.toString(),
                binding.unit14.text.toString(),
                binding.amount14.text.toString(),
                binding.item15.text.toString(),
                binding.unit15.text.toString(),
                binding.amount15.text.toString(),
                getSumTotal(),
                listTypePricelist,
                binding.addNoteText.text.toString()
            )
            Handler(Looper.getMainLooper()).postDelayed({
                val action = MakeListFragmentDirections.actionMakeListFragmentToSavedListsFragment()
                findNavController().navigate(action)

            }, 500)
        }
    }
    //The UPDATE LIST Section ends ...

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the preview_layout for this fragment

        _binding = FragmentMakeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.totalButton.setOnClickListener { calculateTotal() }

        val id = navigationArgs.itemId

        if (id > 0) {
            viewModel.retrieveList(id).observe(this.viewLifecycleOwner) { selectedList ->
                item = selectedList
                bindToFields(item)
            }
        } else {
            binding.doneButton.setOnClickListener {
                showSnackbar()
                addNewList()
            }
        }

        //implement the back arrow as up button...
        binding.backButton.setOnClickListener {
            this.findNavController().navigateUp()
        }

    }

    private fun showSnackbar() {
        if (!isSavePossible()) {
            Snackbar.make(this.requireView(), "List can not be empty", Snackbar.LENGTH_LONG)
                .show()
        }
    }

    private fun calculateTotal() {
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
        //displayTotal(sumTotal)

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
        //displayTotal(sumTotal)

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
        //displayTotal(sumTotal)

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
        //displayTotal(sumTotal)

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

        var amount11 = binding.amount11.text.toString().toDoubleOrNull()
        if (amount11 == null) {
            amount11 = 0.0
        }

        var unit11 = binding.unit11.text.toString().toDoubleOrNull()
        if (unit11 == null) {
            unit11 = 1.0
        }

        val finalAmount11 = amount11 * unit11
        sumTotal += finalAmount11

        var amount12 = binding.amount12.text.toString().toDoubleOrNull()
        if (amount12 == null) {
            amount12 = 0.0
        }

        var unit12 = binding.unit12.text.toString().toDoubleOrNull()
        if (unit12 == null) {
            unit12 = 1.0
        }

        val finalAmount12 = amount12 * unit12
        sumTotal += finalAmount12

        var amount13 = binding.amount13.text.toString().toDoubleOrNull()
        if (amount13 == null) {
            amount13 = 0.0
        }

        var unit13 = binding.unit13.text.toString().toDoubleOrNull()
        if (unit13 == null) {
            unit13 = 1.0
        }

        val finalAmount13 = amount13 * unit13
        sumTotal += finalAmount13

        var amount14 = binding.amount14.text.toString().toDoubleOrNull()
        if (amount14 == null) {
            amount14 = 0.0
        }

        var unit14 = binding.unit14.text.toString().toDoubleOrNull()
        if (unit14 == null) {
            unit14 = 1.0
        }

        val finalAmount14 = amount14 * unit14
        sumTotal += finalAmount14

        var amount15 = binding.amount15.text.toString().toDoubleOrNull()
        if (amount15 == null) {
            amount15 = 0.0
        }

        var unit15 = binding.unit15.text.toString().toDoubleOrNull()
        if (unit15 == null) {
            unit15 = 1.0
        }

        val finalAmount15 = amount15 * unit15
        sumTotal += finalAmount15

        //sumTotal = amount1 + amount2 + amount3 + amount4 + amount5... (at first, I used this method)

        /**
         * Line belowcommented was used before the discovery of displayTotal()
         * binding.total.text = sumTotal.toString()
         */

        displayTotal(sumTotal)
    }

    private fun displayTotal(sumTotal: Double) {
        val formattedTotal = NumberFormat.getCurrencyInstance()
            .format(sumTotal) //here, sumTotal is the parameter above
        binding.total.text = getString(R.string.total_amount, formattedTotal)
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}