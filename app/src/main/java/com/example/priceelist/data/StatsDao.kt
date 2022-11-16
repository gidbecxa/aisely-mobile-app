package com.example.priceelist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StatsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Stats)

    @Query("SELECT * FROM stats WHERE `action` = 'sent' AND item_type = :itemType ORDER BY id DESC")
    fun getItemHistory(itemType: String): Flow<List<Stats>>

    @Query("SELECT * FROM stats WHERE `action` = 'sent' OR `action` = 'cleared' OR `action` = 'downloaded' ORDER BY id DESC")
    fun getHistory(): Flow<List<Stats>>

    @Query("SELECT COUNT(id) FROM stats WHERE item_type = :itemType AND `action` = 'created' ")
    fun getCreatedCount(itemType: String): LiveData<Int>

    @Query("SELECT COUNT(id) FROM stats WHERE item_type = :itemType AND `action` = 'sent' OR `action` = 'downloaded'")
    fun getSentCount(itemType: String): LiveData<Int>

    @Query("SELECT COUNT(id) FROM stats WHERE item_type = :itemType AND `action` = 'cleared' ")
    fun getClearedCount(itemType: String): LiveData<Int>

    @Query("SELECT SUM(amount) FROM stats WHERE item_type = :itemType AND `action` = 'created' ")
    fun getSumCreated(itemType: String): LiveData<Double>

    @Query("SELECT SUM(amount) FROM stats WHERE item_type = :itemType AND `action` = 'sent' OR `action` = 'downloaded'")
    fun getSumSent(itemType: String): LiveData<Double>

    @Query("SELECT SUM(amount) FROM stats WHERE item_type = :itemType AND `action` = 'cleared' ")
    fun getSumCleared(itemType: String): LiveData<Double>
}