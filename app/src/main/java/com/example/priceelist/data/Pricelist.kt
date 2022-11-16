package com.example.priceelist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat


@Entity(tableName = "pricelist")
data class Pricelist (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "title") val dbListTitle: String,

    @ColumnInfo(name = "date") val date: String,

    @ColumnInfo(name = "item_1") val dbItem1: String?,
    @ColumnInfo(name = "quantity_1") val dbQuantity1: Double?,
    @ColumnInfo(name = "amount_1") val dbAmount1: Double?,

    @ColumnInfo(name = "item_2") val dbItem2: String?,
    @ColumnInfo(name = "quantity_2") val dbQuantity2: Double?,
    @ColumnInfo(name = "amount_2") val dbAmount2: Double?,

    @ColumnInfo(name = "item_3") val dbItem3: String?,
    @ColumnInfo(name = "quantity_3") val dbQuantity3: Double?,
    @ColumnInfo(name = "amount_3") val dbAmount3: Double?,

    @ColumnInfo(name = "item_4") val dbItem4: String?,
    @ColumnInfo(name = "quantity_4") val dbQuantity4: Double?,
    @ColumnInfo(name = "amount_4") val dbAmount4: Double?,

    @ColumnInfo(name = "item_5") val dbItem5: String?,
    @ColumnInfo(name = "quantity_5") val dbQuantity5: Double?,
    @ColumnInfo(name = "amount_5") val dbAmount5: Double?,

    @ColumnInfo(name = "item_6") val dbItem6: String?,
    @ColumnInfo(name = "quantity_6") val dbQuantity6: Double?,
    @ColumnInfo(name = "amount_6") val dbAmount6: Double?,

    @ColumnInfo(name = "item_7") val dbItem7: String?,
    @ColumnInfo(name = "quantity_7") val dbQuantity7: Double?,
    @ColumnInfo(name = "amount_7") val dbAmount7: Double?,

    @ColumnInfo(name = "item_8") val dbItem8: String?,
    @ColumnInfo(name = "quantity_8") val dbQuantity8: Double?,
    @ColumnInfo(name = "amount_8") val dbAmount8: Double?,

    @ColumnInfo(name = "item_9") val dbItem9: String?,
    @ColumnInfo(name = "quantity_9") val dbQuantity9: Double?,
    @ColumnInfo(name = "amount_9") val dbAmount9: Double?,

    @ColumnInfo(name = "item_10") val dbItem10: String?,
    @ColumnInfo(name = "quantity_10") val dbQuantity10: Double?,
    @ColumnInfo(name = "amount_10") val dbAmount10: Double?,

    @ColumnInfo(name = "item_11") val dbItem11: String?,
    @ColumnInfo(name = "quantity_11") val dbQuantity11: Double?,
    @ColumnInfo(name = "amount_11") val dbAmount11: Double?,

    @ColumnInfo(name = "item_12") val dbItem12: String?,
    @ColumnInfo(name = "quantity_12") val dbQuantity12: Double?,
    @ColumnInfo(name = "amount_12") val dbAmount12: Double?,

    @ColumnInfo(name = "item_13") val dbItem13: String?,
    @ColumnInfo(name = "quantity_13") val dbQuantity13: Double?,
    @ColumnInfo(name = "amount_13") val dbAmount13: Double?,

    @ColumnInfo(name = "item_14") val dbItem14: String?,
    @ColumnInfo(name = "quantity_14") val dbQuantity14: Double?,
    @ColumnInfo(name = "amount_14") val dbAmount14: Double?,

    @ColumnInfo(name = "item_15") val dbItem15: String?,
    @ColumnInfo(name = "quantity_15") val dbQuantity15: Double?,
    @ColumnInfo(name = "amount_15") val dbAmount15: Double?,

    @ColumnInfo(name = "total") val dbTotal: Double,
    @ColumnInfo(name = "type") val listType: String,
    @ColumnInfo(name = "note") val note: String = ""
            )

fun Pricelist.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(dbTotal)