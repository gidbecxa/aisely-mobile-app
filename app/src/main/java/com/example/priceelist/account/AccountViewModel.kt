package com.example.priceelist.account

import androidx.lifecycle.*
import com.example.priceelist.data.Account
import com.example.priceelist.data.AccountDao
import kotlinx.coroutines.launch

class AccountViewModel(private val accountDao: AccountDao) : ViewModel() {
    fun getUserData(): LiveData<Account> {
        return accountDao.getUserData().asLiveData()
    }

    private fun insertUser(item: Account) {
        viewModelScope.launch {
            accountDao.insertUserData(item)
        }
    }

    private fun getUserEntry(
        businessName: String,
        businessCategory: String,
        businessPhone1: String,
        businessPhone2: String,
        businessMail: String,
        businessAddress: String,
        institutionName: String,
        bankAccName: String,
        bankAccNo: String
    ): Account {
        return Account(
            businessName = businessName,
            businessCategory = businessCategory,
            businessPhone1 = businessPhone1,
            businessPhone2 = businessPhone2,
            businessMail = businessMail,
            businessAddress = businessAddress,
            institutionName = institutionName,
            bankAccName = bankAccName,
            bankAccNo = bankAccNo
        )
    }

    fun insertUserData(
        businessName: String,
        businessCategory: String,
        businessPhone1: String,
        businessPhone2: String,
        businessMail: String,
        businessAddress: String,
        institutionName: String,
        bankAccName: String,
        bankAccNo: String
    ) {
        val user = getUserEntry(businessName, businessCategory, businessPhone1, businessPhone2, businessMail, businessAddress, institutionName, bankAccName, bankAccNo)
        insertUser(user)
    }

    private fun updateBasicInfo(
        businessName: String,
        businessCategory: String,
        businessPhone1: String,
        businessPhone2: String,
        businessMail: String,
        businessAddress: String
    ) {
        viewModelScope.launch {
            accountDao.updateBasicInfo(businessName, businessCategory, businessPhone1, businessPhone2, businessMail, businessAddress)
        }
    }

    private fun updatePaymentInfo(
        institutionName: String,
        bankAccName: String,
        bankAccNo: String
    ) {
        viewModelScope.launch {
            accountDao.updatePaymentInfo(institutionName, bankAccName, bankAccNo)
        }
    }
    
    /*fun updateBasicInfoData(
        businessName: String,
        businessCategory: String,
        businessPhone1: String,
        businessPhone2: String,
        businessMail: String,
        businessAddress: String
    ) {
        return accountDao.updateBasicInfo(businessName, businessCategory, businessPhone1, businessPhone2, businessMail, businessAddress)
    }*/
    
    fun updateBasicInfoData(
        businessName: String,
        businessCategory: String,
        businessPhone1: String,
        businessPhone2: String,
        businessMail: String,
        businessAddress: String
    ) {
        updateBasicInfo(businessName, businessCategory, businessPhone1, businessPhone2, businessMail, businessAddress)
    }
    
    fun updatePaymentInfoData(
        institutionName: String,
        bankAccName: String,
        bankAccNo: String
    ) {
        updatePaymentInfo(institutionName, bankAccName, bankAccNo)
    }
    
    fun isInsertUserPossible(businessName: String, businessPhone1: String, businessAddress: String): Boolean {
        if (businessName.isBlank() || businessPhone1.isBlank() || businessAddress.isBlank()) {
            return false
        }
        //else
        return true
    }
}

class AccountViewModelFactory(private val accountDao: AccountDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountViewModel(accountDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}