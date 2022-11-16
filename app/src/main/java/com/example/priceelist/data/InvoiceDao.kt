package com.example.priceelist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInvoice(item: Invoice)

    @Update
    suspend fun updateInvoice(item: Invoice)

    @Delete
    suspend fun deleteInvoice(item: Invoice)

    @Query("SELECT * FROM invoice WHERE id = :id")
    fun getInvoice(id: Int): Flow<Invoice>

    @Query ("SELECT * FROM invoice WHERE type = :type ORDER BY id DESC LIMIT 1")
    fun getLastInvoice(type: String): Flow<Invoice>

    @Query ("SELECT * FROM invoice WHERE type = :type ORDER BY id DESC LIMIT 1")
    fun getLastReceipt(type: String): Flow<Invoice>

    @Query ("SELECT * FROM invoice ORDER BY id DESC LIMIT 1")
    fun getLastInvoiceItem(): Flow<Invoice>

    @Query("SELECT * FROM invoice WHERE type = :type ORDER BY id DESC")
    fun getInvoices(type: String): Flow<List<Invoice>>

    @Query("SELECT * FROM invoice WHERE type = :type ORDER BY id DESC")
    fun getReceipts(type: String): Flow<List<Invoice>>

    @Query("UPDATE invoice SET cleared = :cleared WHERE id = :id")
    suspend fun updateClrInvoice(cleared: Boolean, id: Int)
}