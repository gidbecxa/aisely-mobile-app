package com.example.priceelist.data

import androidx.room.*
import java.text.NumberFormat

@Entity(
    tableName = "invoice",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"],
            childColumns = ["client_id"],
            //parentColumns = ["client_name"],
            //childColumns = ["client_name"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )],
    indices = [
        Index(
            value = ["client_id"],
            //value = ["client_name"],
            name = "idx_client_id",
            //name = "idx_client",
            unique = false
        )]
    //primaryKeys = ["id", "invoice_number"]
)
data class Invoice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "invoice_number") val invoiceNumber: String,

    @ColumnInfo(name = "client_id") val clientId: Int,

    @ColumnInfo(name = "item_1") val invoiceItem1: String?,
    @ColumnInfo(name = "quantity_1") val invoiceQuantity1: Double?,
    @ColumnInfo(name = "amount_1") val invoiceAmount1: Double?,

    @ColumnInfo(name = "item_2") val invoiceItem2: String?,
    @ColumnInfo(name = "quantity_2") val invoiceQuantity2: Double?,
    @ColumnInfo(name = "amount_2") val invoiceAmount2: Double?,

    @ColumnInfo(name = "item_3") val invoiceItem3: String?,
    @ColumnInfo(name = "quantity_3") val invoiceQuantity3: Double?,
    @ColumnInfo(name = "amount_3") val invoiceAmount3: Double?,

    @ColumnInfo(name = "item_4") val invoiceItem4: String?,
    @ColumnInfo(name = "quantity_4") val invoiceQuantity4: Double?,
    @ColumnInfo(name = "amount_4") val invoiceAmount4: Double?,

    @ColumnInfo(name = "item_5") val invoiceItem5: String?,
    @ColumnInfo(name = "quantity_5") val invoiceQuantity5: Double?,
    @ColumnInfo(name = "amount_5") val invoiceAmount5: Double?,

    @ColumnInfo(name = "item_6") val invoiceItem6: String?,
    @ColumnInfo(name = "quantity_6") val invoiceQuantity6: Double?,
    @ColumnInfo(name = "amount_6") val invoiceAmount6: Double?,

    @ColumnInfo(name = "item_7") val invoiceItem7: String?,
    @ColumnInfo(name = "quantity_7") val invoiceQuantity7: Double?,
    @ColumnInfo(name = "amount_7") val invoiceAmount7: Double?,

    @ColumnInfo(name = "item_8") val invoiceItem8: String?,
    @ColumnInfo(name = "quantity_8") val invoiceQuantity8: Double?,
    @ColumnInfo(name = "amount_8") val invoiceAmount8: Double?,

    @ColumnInfo(name = "item_9") val invoiceItem9: String?,
    @ColumnInfo(name = "quantity_9") val invoiceQuantity9: Double?,
    @ColumnInfo(name = "amount_9") val invoiceAmount9: Double?,

    @ColumnInfo(name = "item_10") val invoiceItem10: String?,
    @ColumnInfo(name = "quantity_10") val invoiceQuantity10: Double?,
    @ColumnInfo(name = "amount_10") val invoiceAmount10: Double?,

    @ColumnInfo(name = "total") val invoiceTotal: Double,
    @ColumnInfo(name = "type") val listType: String = "In",
    @ColumnInfo(name = "note") val receiverNote: String = "",
    @ColumnInfo(name = "file_name") val fileName: String = "",
    @ColumnInfo(name = "invoice_note") val invoiceNote: String = "",
    @ColumnInfo(name = "cleared") val cleared: Boolean = false
)

fun Invoice.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(invoiceTotal)

//INSERT INTO invoice (date, invoice_number, client_id, item_1, quantity_1, amount_1, total, type)
// VALUES ("Sep 7, 2021", 7777, 2, "Macbook Pro", 1.0, 1000.0, 1000.0, "In")