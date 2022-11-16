package com.example.priceelist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

@Entity(tableName = "stats")
data class Stats(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "item_name") val itemName: String,
    @ColumnInfo(name = "item_type") val itemType: String,
    @ColumnInfo(name = "action") val action: String, //action can be either "created", "sent", or "cleared"
    @ColumnInfo(name = "action_date") val actionDate: String,
    @ColumnInfo(name = "amount")  val amount: Double?
)

fun Stats.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(amount)