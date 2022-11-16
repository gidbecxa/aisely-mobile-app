package com.example.priceelist.pricelist

import androidx.lifecycle.*
import com.example.priceelist.data.Pricelist
import com.example.priceelist.data.PricelistDao
import kotlinx.coroutines.launch

class PricelistViewModel(private val pricelistDao: PricelistDao) : ViewModel() {

    //The line below gets all lists from the db: the GET LISTS Section

    val allLists: LiveData<List<Pricelist>> = pricelistDao.getLists().asLiveData()

    //The INSERT NEW LIST Section : Functions for adding a new list begin here

    private fun insertList(item: Pricelist) {
        viewModelScope.launch { pricelistDao.insert(item) } //viewModelScope...
        // because a suspend fun can only be called from a coroutine or another suspend fun
    }

    /**
     * This function gets the user's entry to be sent into the databbase.
     * The user's entries are strings, while the database receives strings and doubles.
     * Hence, for the fields that are Strings, the function sends them untouched to the db.
     * However, for the fields that are meant to be saved as Doubles in the db, the function
     * converts them to Double.
     * Note however, the fields which take the user's list could be empty
     * (thereby returning empty strings i.e. null values). Therefore,
     * the program must check if a field contains a Double value or a null (and act accordingly).
     * Hence the use of .toDoubleOrNull()
     */
    private fun getNewListEntry(
        dbListTitle: String, date: String,
        dbItem1: String, dbQuantity1: String, dbAmount1: String,
        dbItem2: String, dbQuantity2: String, dbAmount2: String,
        dbItem3: String, dbQuantity3: String, dbAmount3: String,
        dbItem4: String, dbQuantity4: String, dbAmount4: String,
        dbItem5: String, dbQuantity5: String, dbAmount5: String,
        dbItem6: String, dbQuantity6: String, dbAmount6: String,
        dbItem7: String, dbQuantity7: String, dbAmount7: String,
        dbItem8: String, dbQuantity8: String, dbAmount8: String,
        dbItem9: String, dbQuantity9: String, dbAmount9: String,
        dbItem10: String, dbQuantity10: String, dbAmount10: String,
        dbItem11: String, dbQuantity11: String, dbAmount11: String,
        dbItem12: String, dbQuantity12: String, dbAmount12: String,
        dbItem13: String, dbQuantity13: String, dbAmount13: String,
        dbItem14: String, dbQuantity14: String, dbAmount14: String,
        dbItem15: String, dbQuantity15: String, dbAmount15: String,
        dbTotal: String, listType: String, note: String
    ): Pricelist {
        return Pricelist(
            dbListTitle = dbListTitle,
            date = date,
            dbItem1 = dbItem1,
            dbQuantity1 = dbQuantity1.toDoubleOrNull(),
            dbAmount1 = dbAmount1.toDoubleOrNull(),
            dbItem2 = dbItem2,
            dbQuantity2 = dbQuantity2.toDoubleOrNull(),
            dbAmount2 = dbAmount2.toDoubleOrNull(),
            dbItem3 = dbItem3,
            dbQuantity3 = dbQuantity3.toDoubleOrNull(),
            dbAmount3 = dbAmount3.toDoubleOrNull(),
            dbItem4 = dbItem4,
            dbQuantity4 = dbQuantity4.toDoubleOrNull(),
            dbAmount4 = dbAmount4.toDoubleOrNull(),
            dbItem5 = dbItem5,
            dbQuantity5 = dbQuantity5.toDoubleOrNull(),
            dbAmount5 = dbAmount5.toDoubleOrNull(),
            dbItem6 = dbItem6,
            dbQuantity6 = dbQuantity6.toDoubleOrNull(),
            dbAmount6 = dbAmount6.toDoubleOrNull(),
            dbItem7 = dbItem7,
            dbQuantity7 = dbQuantity7.toDoubleOrNull(),
            dbAmount7 = dbAmount7.toDoubleOrNull(),
            dbItem8 = dbItem8,
            dbQuantity8 = dbQuantity8.toDoubleOrNull(),
            dbAmount8 = dbAmount8.toDoubleOrNull(),
            dbItem9 = dbItem9,
            dbQuantity9 = dbQuantity9.toDoubleOrNull(),
            dbAmount9 = dbAmount9.toDoubleOrNull(),
            dbItem10 = dbItem10,
            dbQuantity10 = dbQuantity10.toDoubleOrNull(),
            dbAmount10 = dbAmount10.toDoubleOrNull(),
            dbItem11 = dbItem11,
            dbQuantity11 = dbQuantity11.toDoubleOrNull(),
            dbAmount11 = dbAmount11.toDoubleOrNull(),
            dbItem12 = dbItem12,
            dbQuantity12 = dbQuantity12.toDoubleOrNull(),
            dbAmount12 = dbAmount12.toDoubleOrNull(),
            dbItem13 = dbItem13,
            dbQuantity13 = dbQuantity13.toDoubleOrNull(),
            dbAmount13 = dbAmount13.toDoubleOrNull(),
            dbItem14 = dbItem14,
            dbQuantity14 = dbQuantity14.toDoubleOrNull(),
            dbAmount14 = dbAmount14.toDoubleOrNull(),
            dbItem15 = dbItem15,
            dbQuantity15 = dbQuantity15.toDoubleOrNull(),
            dbAmount15 = dbAmount15.toDoubleOrNull(),
            dbTotal = dbTotal.toDouble(),
            listType = listType,
            note = note
        )
    }

    fun addnewList(
        dbListTitle: String, date: String,
        dbItem1: String, dbQuantity1: String, dbAmount1: String,
        dbItem2: String, dbQuantity2: String, dbAmount2: String,
        dbItem3: String, dbQuantity3: String, dbAmount3: String,
        dbItem4: String, dbQuantity4: String, dbAmount4: String,
        dbItem5: String, dbQuantity5: String, dbAmount5: String,
        dbItem6: String, dbQuantity6: String, dbAmount6: String,
        dbItem7: String, dbQuantity7: String, dbAmount7: String,
        dbItem8: String, dbQuantity8: String, dbAmount8: String,
        dbItem9: String, dbQuantity9: String, dbAmount9: String,
        dbItem10: String, dbQuantity10: String, dbAmount10: String,
        dbItem11: String, dbQuantity11: String, dbAmount11: String,
        dbItem12: String, dbQuantity12: String, dbAmount12: String,
        dbItem13: String, dbQuantity13: String, dbAmount13: String,
        dbItem14: String, dbQuantity14: String, dbAmount14: String,
        dbItem15: String, dbQuantity15: String, dbAmount15: String,
        dbTotal: String, listType: String, note: String
    ) {
        val newList = getNewListEntry(
            dbListTitle, date,
            dbItem1, dbQuantity1, dbAmount1,
            dbItem2, dbQuantity2, dbAmount2,
            dbItem3, dbQuantity3, dbAmount3,
            dbItem4, dbQuantity4, dbAmount4,
            dbItem5, dbQuantity5, dbAmount5,
            dbItem6, dbQuantity6, dbAmount6,
            dbItem7, dbQuantity7, dbAmount7,
            dbItem8, dbQuantity8, dbAmount8,
            dbItem9, dbQuantity9, dbAmount9,
            dbItem10, dbQuantity10, dbAmount10,
            dbItem11, dbQuantity11, dbAmount11,
            dbItem12, dbQuantity12, dbAmount12,
            dbItem13, dbQuantity13, dbAmount13,
            dbItem14, dbQuantity14, dbAmount14,
            dbItem15, dbQuantity15, dbAmount15, dbTotal, listType, note
        )
        insertList(newList) //calling here the insertList() fun declared above, but without the viewModelScope.launch...
    }

    //The INSERT NEW LIST Section ends ...

    //RETRIEVE A LIST Section: the functions below aim to get
    // entries (columns) of a single list ( as clicked by the user)

    fun retrieveList(id: Int): LiveData<Pricelist> {
        return pricelistDao.getList(id).asLiveData()
    }

    //That's all for RETRIEVE A LIST section: section ends here.

    //The UPDATE A LIST Section : Functions for editing & updating a selected list begin here

    private fun updateList(item: Pricelist) {
        viewModelScope.launch {
            pricelistDao.update(item)
        }
    } //viewModelScope...
    // because a suspend fun can only be called from a coroutine or another suspend fun

    private fun getUpdatedEntry(
        itemId: Int,
        dbListTitle: String, date: String,
        dbItem1: String, dbQuantity1: String, dbAmount1: String,
        dbItem2: String, dbQuantity2: String, dbAmount2: String,
        dbItem3: String, dbQuantity3: String, dbAmount3: String,
        dbItem4: String, dbQuantity4: String, dbAmount4: String,
        dbItem5: String, dbQuantity5: String, dbAmount5: String,
        dbItem6: String, dbQuantity6: String, dbAmount6: String,
        dbItem7: String, dbQuantity7: String, dbAmount7: String,
        dbItem8: String, dbQuantity8: String, dbAmount8: String,
        dbItem9: String, dbQuantity9: String, dbAmount9: String,
        dbItem10: String, dbQuantity10: String, dbAmount10: String,
        dbItem11: String, dbQuantity11: String, dbAmount11: String,
        dbItem12: String, dbQuantity12: String, dbAmount12: String,
        dbItem13: String, dbQuantity13: String, dbAmount13: String,
        dbItem14: String, dbQuantity14: String, dbAmount14: String,
        dbItem15: String, dbQuantity15: String, dbAmount15: String,
        dbTotal: String, listType: String, note: String
    ): Pricelist {
        return Pricelist(
            id = itemId,
            dbListTitle = dbListTitle,
            date = date,
            dbItem1 = dbItem1,
            dbQuantity1 = dbQuantity1.toDoubleOrNull(),
            dbAmount1 = dbAmount1.toDoubleOrNull(),
            dbItem2 = dbItem2,
            dbQuantity2 = dbQuantity2.toDoubleOrNull(),
            dbAmount2 = dbAmount2.toDoubleOrNull(),
            dbItem3 = dbItem3,
            dbQuantity3 = dbQuantity3.toDoubleOrNull(),
            dbAmount3 = dbAmount3.toDoubleOrNull(),
            dbItem4 = dbItem4,
            dbQuantity4 = dbQuantity4.toDoubleOrNull(),
            dbAmount4 = dbAmount4.toDoubleOrNull(),
            dbItem5 = dbItem5,
            dbQuantity5 = dbQuantity5.toDoubleOrNull(),
            dbAmount5 = dbAmount5.toDoubleOrNull(),
            dbItem6 = dbItem6,
            dbQuantity6 = dbQuantity6.toDoubleOrNull(),
            dbAmount6 = dbAmount6.toDoubleOrNull(),
            dbItem7 = dbItem7,
            dbQuantity7 = dbQuantity7.toDoubleOrNull(),
            dbAmount7 = dbAmount7.toDoubleOrNull(),
            dbItem8 = dbItem8,
            dbQuantity8 = dbQuantity8.toDoubleOrNull(),
            dbAmount8 = dbAmount8.toDoubleOrNull(),
            dbItem9 = dbItem9,
            dbQuantity9 = dbQuantity9.toDoubleOrNull(),
            dbAmount9 = dbAmount9.toDoubleOrNull(),
            dbItem10 = dbItem10,
            dbQuantity10 = dbQuantity10.toDoubleOrNull(),
            dbAmount10 = dbAmount10.toDoubleOrNull(),
            dbItem11 = dbItem11,
            dbQuantity11 = dbQuantity11.toDoubleOrNull(),
            dbAmount11 = dbAmount11.toDoubleOrNull(),
            dbItem12 = dbItem12,
            dbQuantity12 = dbQuantity12.toDoubleOrNull(),
            dbAmount12 = dbAmount12.toDoubleOrNull(),
            dbItem13 = dbItem13,
            dbQuantity13 = dbQuantity13.toDoubleOrNull(),
            dbAmount13 = dbAmount13.toDoubleOrNull(),
            dbItem14 = dbItem14,
            dbQuantity14 = dbQuantity14.toDoubleOrNull(),
            dbAmount14 = dbAmount14.toDoubleOrNull(),
            dbItem15 = dbItem15,
            dbQuantity15 = dbQuantity15.toDoubleOrNull(),
            dbAmount15 = dbAmount15.toDoubleOrNull(),
            dbTotal = dbTotal.toDouble(),
            listType = listType,
            note = note
        )
    }

    fun updateList(
        itemId: Int,
        dbListTitle: String, date: String,
        dbItem1: String, dbQuantity1: String, dbAmount1: String,
        dbItem2: String, dbQuantity2: String, dbAmount2: String,
        dbItem3: String, dbQuantity3: String, dbAmount3: String,
        dbItem4: String, dbQuantity4: String, dbAmount4: String,
        dbItem5: String, dbQuantity5: String, dbAmount5: String,
        dbItem6: String, dbQuantity6: String, dbAmount6: String,
        dbItem7: String, dbQuantity7: String, dbAmount7: String,
        dbItem8: String, dbQuantity8: String, dbAmount8: String,
        dbItem9: String, dbQuantity9: String, dbAmount9: String,
        dbItem10: String, dbQuantity10: String, dbAmount10: String,
        dbItem11: String, dbQuantity11: String, dbAmount11: String,
        dbItem12: String, dbQuantity12: String, dbAmount12: String,
        dbItem13: String, dbQuantity13: String, dbAmount13: String,
        dbItem14: String, dbQuantity14: String, dbAmount14: String,
        dbItem15: String, dbQuantity15: String, dbAmount15: String,
        dbTotal: String, listType: String, note: String
    ) {
        val updatedList = getUpdatedEntry(
            itemId, dbListTitle, date,
            dbItem1, dbQuantity1, dbAmount1,
            dbItem2, dbQuantity2, dbAmount2,
            dbItem3, dbQuantity3, dbAmount3,
            dbItem4, dbQuantity4, dbAmount4,
            dbItem5, dbQuantity5, dbAmount5,
            dbItem6, dbQuantity6, dbAmount6,
            dbItem7, dbQuantity7, dbAmount7,
            dbItem8, dbQuantity8, dbAmount8,
            dbItem9, dbQuantity9, dbAmount9,
            dbItem10, dbQuantity10, dbAmount10,
            dbItem11, dbQuantity11, dbAmount11,
            dbItem12, dbQuantity12, dbAmount12,
            dbItem13, dbQuantity13, dbAmount13,
            dbItem14, dbQuantity14, dbAmount14,
            dbItem15, dbQuantity15, dbAmount15,
            dbTotal, listType, note
        )
        updateList(updatedList) //NB: this updateList called here is the private fun above
    }

    //The UPDATE LIST Section ends ...

    fun getLastList(): LiveData<Pricelist> {
        return pricelistDao.getLastList().asLiveData()
    }

    //DELETE A LIST section...
    fun deleteList(item: Pricelist) {
        viewModelScope.launch {
            pricelistDao.delete(item)
        }
    }

    fun isSavePossible(
        dbItem1: String, dbQuantity1: String, dbAmount1: String,
        dbItem2: String, dbQuantity2: String, dbAmount2: String,
        dbItem3: String, dbQuantity3: String, dbAmount3: String,
        dbItem4: String, dbQuantity4: String, dbAmount4: String,
        dbItem5: String, dbQuantity5: String, dbAmount5: String,
        dbItem6: String, dbQuantity6: String, dbAmount6: String,
        dbItem7: String, dbQuantity7: String, dbAmount7: String,
        dbItem8: String, dbQuantity8: String, dbAmount8: String,
        dbItem9: String, dbQuantity9: String, dbAmount9: String,
        dbItem10: String, dbQuantity10: String, dbAmount10: String,
        dbItem11: String, dbQuantity11: String, dbAmount11: String,
        dbItem12: String, dbQuantity12: String, dbAmount12: String,
        dbItem13: String, dbQuantity13: String, dbAmount13: String,
        dbItem14: String, dbQuantity14: String, dbAmount14: String,
        dbItem15: String, dbQuantity15: String, dbAmount15: String
    ): Boolean {
        if (dbItem1.isBlank() && dbQuantity1.isBlank() && dbAmount1.isBlank()
            && dbItem2.isBlank() && dbQuantity2.isBlank() && dbAmount2.isBlank()
            && dbItem3.isBlank() && dbQuantity3.isBlank() && dbAmount3.isBlank()
            && dbItem4.isBlank() && dbQuantity4.isBlank() && dbAmount4.isBlank()
            && dbItem5.isBlank() && dbQuantity5.isBlank() && dbAmount5.isBlank()
            && dbItem6.isBlank() && dbQuantity6.isBlank() && dbAmount6.isBlank()
            && dbItem7.isBlank() && dbQuantity7.isBlank() && dbAmount7.isBlank()
            && dbItem8.isBlank() && dbQuantity8.isBlank() && dbAmount8.isBlank()
            && dbItem9.isBlank() && dbQuantity9.isBlank() && dbAmount9.isBlank()
            && dbItem10.isBlank() && dbQuantity10.isBlank() && dbAmount10.isBlank()
            && dbItem11.isBlank() && dbQuantity11.isBlank() && dbAmount11.isBlank()
            && dbItem12.isBlank() && dbQuantity12.isBlank() && dbAmount12.isBlank()
            && dbItem13.isBlank() && dbQuantity13.isBlank() && dbAmount13.isBlank()
            && dbItem14.isBlank() && dbQuantity14.isBlank() && dbAmount14.isBlank()
            && dbItem15.isBlank() && dbQuantity15.isBlank() && dbAmount15.isBlank()
        ) {
            return false
        }
        //else
        return true
    }

} //viewModel ends here...

class PricelistViewModelFactory(private val pricelistDao: PricelistDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PricelistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PricelistViewModel(pricelistDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}