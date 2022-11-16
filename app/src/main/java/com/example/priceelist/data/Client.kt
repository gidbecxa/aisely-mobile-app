package com.example.priceelist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "client",
    indices = [
        Index(
            value = ["id"],
            //value = ["client_name"],
            name = "idx_id",
            //name = "idx_client",
            unique = true
        )]
)
data class Client (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "client_name") val clientName: String,
    @ColumnInfo(name = "client_phone") val clientPhone: String,
    @ColumnInfo(name = "client_mail") val clientMail: String,
    @ColumnInfo(name = "client_address") val clientAddress: String?
        )