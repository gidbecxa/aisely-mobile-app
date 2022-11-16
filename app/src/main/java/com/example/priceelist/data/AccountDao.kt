package com.example.priceelist.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserData(item: Account)

    @Query("UPDATE account SET business_name = :businessName, business_category = :businessCategory, business_phone1 = :businessPhone1, business_phone2 = :businessPhone2, business_mail = :businessMail, business_address = :businessAddress WHERE id = 1")
    suspend fun updateBasicInfo(
        businessName: String,
        businessCategory: String,
        businessPhone1: String,
        businessPhone2: String,
        businessMail: String,
        businessAddress: String
    )

    @Query("UPDATE account SET institution_name = :institutionName, bank_account_name = :bankAccName, bank_account_no = :bankAccNo WHERE id = 1")
    suspend fun updatePaymentInfo(institutionName: String, bankAccName: String, bankAccNo: String)

    @Query("SELECT * FROM account")
    fun getUserData(): Flow<Account>
}