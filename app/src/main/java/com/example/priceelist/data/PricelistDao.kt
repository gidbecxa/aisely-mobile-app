package com.example.priceelist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PricelistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Pricelist)

    @Update
    suspend fun update(item: Pricelist)

    @Delete
    suspend fun delete(item: Pricelist)

    @Query("SELECT * FROM pricelist WHERE id = :id")
    fun getList(id: Int): Flow<Pricelist>

    @Query("SELECT * FROM pricelist ORDER BY id DESC")
    fun getLists(): Flow<List<Pricelist>>

    @Query("SELECT * FROM pricelist ORDER BY id DESC LIMIT 1")
    fun getLastList(): Flow<Pricelist>

}