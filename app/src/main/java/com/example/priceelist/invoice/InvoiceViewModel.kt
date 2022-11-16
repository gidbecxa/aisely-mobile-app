package com.example.priceelist.invoice

import androidx.lifecycle.*
import com.example.priceelist.data.Invoice
import com.example.priceelist.data.InvoiceDao
import kotlinx.coroutines.launch

class InvoiceViewModel(private val invoiceDao: InvoiceDao) : ViewModel() {

    val allInvoices: LiveData<List<Invoice>> = invoiceDao.getInvoices(type = "In").asLiveData()

    val allReceipts: LiveData<List<Invoice>> = invoiceDao.getReceipts(type = "Re").asLiveData()

    private fun insertInvoice(item: Invoice) {
        viewModelScope.launch { invoiceDao.insertInvoice(item) }
    }

    private fun getNewInvoiceEntry(
        date: String, invoiceNumber: String, clientId: Int,
        invoiceItem1: String, invoiceQuantity1: String, invoiceAmount1: String,
        invoiceItem2: String, invoiceQuantity2: String, invoiceAmount2: String,
        invoiceItem3: String, invoiceQuantity3: String, invoiceAmount3: String,
        invoiceItem4: String, invoiceQuantity4: String, invoiceAmount4: String,
        invoiceItem5: String, invoiceQuantity5: String, invoiceAmount5: String,
        invoiceItem6: String, invoiceQuantity6: String, invoiceAmount6: String,
        invoiceItem7: String, invoiceQuantity7: String, invoiceAmount7: String,
        invoiceItem8: String, invoiceQuantity8: String, invoiceAmount8: String,
        invoiceItem9: String, invoiceQuantity9: String, invoiceAmount9: String,
        invoiceItem10: String, invoiceQuantity10: String, invoiceAmount10: String,
        invoiceTotal: String, listType: String, receiverNote: String,
        fileName: String, note: String
    ): Invoice {
        return Invoice(
            date = date,
            invoiceNumber = invoiceNumber,
            clientId = clientId,
            invoiceItem1 = invoiceItem1,
            invoiceQuantity1 = invoiceQuantity1.toDoubleOrNull(),
            invoiceAmount1 = invoiceAmount1.toDoubleOrNull(),
            invoiceItem2 = invoiceItem2,
            invoiceQuantity2 = invoiceQuantity2.toDoubleOrNull(),
            invoiceAmount2 = invoiceAmount2.toDoubleOrNull(),
            invoiceItem3 = invoiceItem3,
            invoiceQuantity3 = invoiceQuantity3.toDoubleOrNull(),
            invoiceAmount3 = invoiceAmount3.toDoubleOrNull(),
            invoiceItem4 = invoiceItem4,
            invoiceQuantity4 = invoiceQuantity4.toDoubleOrNull(),
            invoiceAmount4 = invoiceAmount4.toDoubleOrNull(),
            invoiceItem5 = invoiceItem5,
            invoiceQuantity5 = invoiceQuantity5.toDoubleOrNull(),
            invoiceAmount5 = invoiceAmount5.toDoubleOrNull(),
            invoiceItem6 = invoiceItem6,
            invoiceQuantity6 = invoiceQuantity6.toDoubleOrNull(),
            invoiceAmount6 = invoiceAmount6.toDoubleOrNull(),
            invoiceItem7 = invoiceItem7,
            invoiceQuantity7 = invoiceQuantity7.toDoubleOrNull(),
            invoiceAmount7 = invoiceAmount7.toDoubleOrNull(),
            invoiceItem8 = invoiceItem8,
            invoiceQuantity8 = invoiceQuantity8.toDoubleOrNull(),
            invoiceAmount8 = invoiceAmount8.toDoubleOrNull(),
            invoiceItem9 = invoiceItem9,
            invoiceQuantity9 = invoiceQuantity9.toDoubleOrNull(),
            invoiceAmount9 = invoiceAmount9.toDoubleOrNull(),
            invoiceItem10 = invoiceItem10,
            invoiceQuantity10 = invoiceQuantity10.toDoubleOrNull(),
            invoiceAmount10 = invoiceAmount10.toDoubleOrNull(),
            invoiceTotal = invoiceTotal.toDouble(),
            listType = listType,
            receiverNote = receiverNote,
            fileName = fileName,
            invoiceNote = note
        )
    }

    fun addNewInvoice(
        date: String, invoiceNumber: String, clientId: Int,
        invoiceItem1: String, invoiceQuantity1: String, invoiceAmount1: String,
        invoiceItem2: String, invoiceQuantity2: String, invoiceAmount2: String,
        invoiceItem3: String, invoiceQuantity3: String, invoiceAmount3: String,
        invoiceItem4: String, invoiceQuantity4: String, invoiceAmount4: String,
        invoiceItem5: String, invoiceQuantity5: String, invoiceAmount5: String,
        invoiceItem6: String, invoiceQuantity6: String, invoiceAmount6: String,
        invoiceItem7: String, invoiceQuantity7: String, invoiceAmount7: String,
        invoiceItem8: String, invoiceQuantity8: String, invoiceAmount8: String,
        invoiceItem9: String, invoiceQuantity9: String, invoiceAmount9: String,
        invoiceItem10: String, invoiceQuantity10: String, invoiceAmount10: String,
        invoiceTotal: String, listType: String, receiverNote: String,
        fileName: String, note: String
    ) {
        val newInvoice = getNewInvoiceEntry(
            date, invoiceNumber, clientId,
            invoiceItem1, invoiceQuantity1, invoiceAmount1,
            invoiceItem2, invoiceQuantity2, invoiceAmount2,
            invoiceItem3, invoiceQuantity3, invoiceAmount3,
            invoiceItem4, invoiceQuantity4, invoiceAmount4,
            invoiceItem5, invoiceQuantity5, invoiceAmount5,
            invoiceItem6, invoiceQuantity6, invoiceAmount6,
            invoiceItem7, invoiceQuantity7, invoiceAmount7,
            invoiceItem8, invoiceQuantity8, invoiceAmount8,
            invoiceItem9, invoiceQuantity9, invoiceAmount9,
            invoiceItem10, invoiceQuantity10, invoiceAmount10,
            invoiceTotal, listType, receiverNote, fileName, note
        )
        insertInvoice(newInvoice)
    }

    fun retrieveInvoice(id: Int): LiveData<Invoice> {
        return invoiceDao.getInvoice(id).asLiveData()
    }

    fun getLastInvoice(): LiveData<Invoice> {
        return invoiceDao.getLastInvoice("In").asLiveData()
    }

    fun getLastReceipt(): LiveData<Invoice> {
        return invoiceDao.getLastReceipt("Re").asLiveData()
    }

    fun getLastInvoiceItem(): LiveData<Invoice> {
        return invoiceDao.getLastInvoiceItem().asLiveData()
    }

    private fun updateInvoice(item: Invoice) {
        viewModelScope.launch {
            invoiceDao.updateInvoice(item)
        }
    }

    private fun getUpdatedInvoiceEntry(
        invoiceId: Int, date: String, invoiceNumber: String, clientId: Int,
        invoiceItem1: String, invoiceQuantity1: String, invoiceAmount1: String,
        invoiceItem2: String, invoiceQuantity2: String, invoiceAmount2: String,
        invoiceItem3: String, invoiceQuantity3: String, invoiceAmount3: String,
        invoiceItem4: String, invoiceQuantity4: String, invoiceAmount4: String,
        invoiceItem5: String, invoiceQuantity5: String, invoiceAmount5: String,
        invoiceItem6: String, invoiceQuantity6: String, invoiceAmount6: String,
        invoiceItem7: String, invoiceQuantity7: String, invoiceAmount7: String,
        invoiceItem8: String, invoiceQuantity8: String, invoiceAmount8: String,
        invoiceItem9: String, invoiceQuantity9: String, invoiceAmount9: String,
        invoiceItem10: String, invoiceQuantity10: String, invoiceAmount10: String,
        invoiceTotal: String, listType: String, receiverNote: String,
        fileName: String, note: String, cleared: Boolean
    ): Invoice {
        return Invoice(
            id = invoiceId,
            date = date,
            invoiceNumber = invoiceNumber,
            clientId = clientId,
            invoiceItem1 = invoiceItem1,
            invoiceQuantity1 = invoiceQuantity1.toDoubleOrNull(),
            invoiceAmount1 = invoiceAmount1.toDoubleOrNull(),
            invoiceItem2 = invoiceItem2,
            invoiceQuantity2 = invoiceQuantity2.toDoubleOrNull(),
            invoiceAmount2 = invoiceAmount2.toDoubleOrNull(),
            invoiceItem3 = invoiceItem3,
            invoiceQuantity3 = invoiceQuantity3.toDoubleOrNull(),
            invoiceAmount3 = invoiceAmount3.toDoubleOrNull(),
            invoiceItem4 = invoiceItem4,
            invoiceQuantity4 = invoiceQuantity4.toDoubleOrNull(),
            invoiceAmount4 = invoiceAmount4.toDoubleOrNull(),
            invoiceItem5 = invoiceItem5,
            invoiceQuantity5 = invoiceQuantity5.toDoubleOrNull(),
            invoiceAmount5 = invoiceAmount5.toDoubleOrNull(),
            invoiceItem6 = invoiceItem6,
            invoiceQuantity6 = invoiceQuantity6.toDoubleOrNull(),
            invoiceAmount6 = invoiceAmount6.toDoubleOrNull(),
            invoiceItem7 = invoiceItem7,
            invoiceQuantity7 = invoiceQuantity7.toDoubleOrNull(),
            invoiceAmount7 = invoiceAmount7.toDoubleOrNull(),
            invoiceItem8 = invoiceItem8,
            invoiceQuantity8 = invoiceQuantity8.toDoubleOrNull(),
            invoiceAmount8 = invoiceAmount8.toDoubleOrNull(),
            invoiceItem9 = invoiceItem9,
            invoiceQuantity9 = invoiceQuantity9.toDoubleOrNull(),
            invoiceAmount9 = invoiceAmount9.toDoubleOrNull(),
            invoiceItem10 = invoiceItem10,
            invoiceQuantity10 = invoiceQuantity10.toDoubleOrNull(),
            invoiceAmount10 = invoiceAmount10.toDoubleOrNull(),
            invoiceTotal = invoiceTotal.toDouble(),
            listType = listType,
            receiverNote = receiverNote,
            fileName = fileName,
            invoiceNote = note,
            cleared = cleared
        )
    }

    fun updateInvoice(
        invoiceId: Int, date: String, invoiceNumber: String, clientId: Int,
        invoiceItem1: String, invoiceQuantity1: String, invoiceAmount1: String,
        invoiceItem2: String, invoiceQuantity2: String, invoiceAmount2: String,
        invoiceItem3: String, invoiceQuantity3: String, invoiceAmount3: String,
        invoiceItem4: String, invoiceQuantity4: String, invoiceAmount4: String,
        invoiceItem5: String, invoiceQuantity5: String, invoiceAmount5: String,
        invoiceItem6: String, invoiceQuantity6: String, invoiceAmount6: String,
        invoiceItem7: String, invoiceQuantity7: String, invoiceAmount7: String,
        invoiceItem8: String, invoiceQuantity8: String, invoiceAmount8: String,
        invoiceItem9: String, invoiceQuantity9: String, invoiceAmount9: String,
        invoiceItem10: String, invoiceQuantity10: String, invoiceAmount10: String,
        invoiceTotal: String, listType: String, receiverNote: String,
        fileName: String, note: String, cleared: Boolean
    ) {
        val updatedInvoice = getUpdatedInvoiceEntry(
            invoiceId, date, invoiceNumber, clientId,
            invoiceItem1, invoiceQuantity1, invoiceAmount1,
            invoiceItem2, invoiceQuantity2, invoiceAmount2,
            invoiceItem3, invoiceQuantity3, invoiceAmount3,
            invoiceItem4, invoiceQuantity4, invoiceAmount4,
            invoiceItem5, invoiceQuantity5, invoiceAmount5,
            invoiceItem6, invoiceQuantity6, invoiceAmount6,
            invoiceItem7, invoiceQuantity7, invoiceAmount7,
            invoiceItem8, invoiceQuantity8, invoiceAmount8,
            invoiceItem9, invoiceQuantity9, invoiceAmount9,
            invoiceItem10, invoiceQuantity10, invoiceAmount10,
            invoiceTotal, listType, receiverNote, fileName, note, cleared
        )
        updateInvoice(updatedInvoice)
    }

    private fun updateClrInvoice(id: Int, cleared: Boolean) {
        viewModelScope.launch {
            invoiceDao.updateClrInvoice(cleared, id)
        }
    }

    fun clrInvoice(id: Int, cleared: Boolean) {
        updateClrInvoice(id, cleared)
    }

    fun deleteInvoice(item: Invoice) {
        viewModelScope.launch {
            invoiceDao.deleteInvoice(item)
        }
    }

    fun isToPreviewPossible(
        invoiceItem1: String, invoiceQuantity1: String, invoiceAmount1: String,
        invoiceItem2: String, invoiceQuantity2: String, invoiceAmount2: String,
        invoiceItem3: String, invoiceQuantity3: String, invoiceAmount3: String,
        invoiceItem4: String, invoiceQuantity4: String, invoiceAmount4: String,
        invoiceItem5: String, invoiceQuantity5: String, invoiceAmount5: String,
        invoiceItem6: String, invoiceQuantity6: String, invoiceAmount6: String,
        invoiceItem7: String, invoiceQuantity7: String, invoiceAmount7: String,
        invoiceItem8: String, invoiceQuantity8: String, invoiceAmount8: String,
        invoiceItem9: String, invoiceQuantity9: String, invoiceAmount9: String,
        invoiceItem10: String, invoiceQuantity10: String, invoiceAmount10: String,
    ): Boolean {
        if (
            (invoiceItem1.isBlank() || invoiceQuantity1.isBlank() || invoiceAmount1.isBlank()) &&
            (invoiceItem2.isBlank() || invoiceQuantity2.isBlank() || invoiceAmount2.isBlank()) &&
            (invoiceItem3.isBlank() || invoiceQuantity3.isBlank() || invoiceAmount3.isBlank()) &&
            (invoiceItem4.isBlank() || invoiceQuantity4.isBlank() || invoiceAmount4.isBlank()) &&
            (invoiceItem5.isBlank() || invoiceQuantity5.isBlank() || invoiceAmount5.isBlank()) &&
            (invoiceItem6.isBlank() || invoiceQuantity6.isBlank() || invoiceAmount6.isBlank()) &&
            (invoiceItem7.isBlank() || invoiceQuantity7.isBlank() || invoiceAmount7.isBlank()) &&
            (invoiceItem8.isBlank() || invoiceQuantity8.isBlank() || invoiceAmount8.isBlank()) &&
            (invoiceItem9.isBlank() || invoiceQuantity9.isBlank() || invoiceAmount9.isBlank()) &&
            (invoiceItem10.isBlank() || invoiceQuantity10.isBlank() || invoiceAmount10.isBlank())
        ) {
            return false
        }
        //else
        return true
    }

}

class InvoiceViewModelFactory(private val invoiceDao: InvoiceDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InvoiceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InvoiceViewModel(invoiceDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}