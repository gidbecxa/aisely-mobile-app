package com.example.priceelist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class Account(
    @PrimaryKey(autoGenerate = false) val id: Int = 1,
    @ColumnInfo(name = "business_name") val businessName: String = "",
    @ColumnInfo(name = "business_category") val businessCategory: String = "",
    @ColumnInfo(name = "business_phone1") val businessPhone1: String = "",
    @ColumnInfo(name = "business_phone2") val businessPhone2: String = "",
    @ColumnInfo(name = "business_mail") val businessMail: String = "",
    @ColumnInfo(name = "business_address") val businessAddress: String = "",
    @ColumnInfo(name = "institution_name") val institutionName: String = "",
    @ColumnInfo(name = "bank_account_name") val bankAccName: String = "",
    @ColumnInfo(name = "bank_account_no") val bankAccNo: String = "",
    @ColumnInfo(name = "subscription") val subscription: String = "Free"
)