package com.example.priceelist.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClient(item: Client)

    @Update
    suspend fun updateClient(item: Client)

    @Delete
    suspend fun deleteClient(item: Client)

    @Query("SELECT * FROM client WHERE id = :id")
    fun getClient(id: Int): Flow<Client>

    @Query("SELECT * FROM client ORDER BY id DESC LIMIT 1")
    fun getLastClient(): Flow<Client>

    @Query("SELECT * FROM client ORDER BY client_name")
    fun getClients(): Flow<List<Client>>

    /*@Query("SELECT * FROM client WHERE client_name = :clientName AND client_phone = :clientPhone AND client_mail = :clientMail AND client_address = :clientAddress")
    fun getSpecificClient(clientName: String, clientPhone: String,
                          clientMail: String, clientAddress: String): Flow<Client>*/
}