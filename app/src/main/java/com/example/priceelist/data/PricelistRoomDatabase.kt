package com.example.priceelist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Pricelist::class, Client::class, Invoice::class, Account::class, Stats::class], version = 10, exportSchema = false)
abstract class PricelistRoomDatabase: RoomDatabase() {

    abstract fun pricelistDao(): PricelistDao
    abstract fun clientDao(): ClientDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun accountDao(): AccountDao
    abstract fun statsDao(): StatsDao

    companion object {
        @Volatile
        private var INSTANCE: PricelistRoomDatabase? = null

        fun getDatabase(context: Context): PricelistRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PricelistRoomDatabase::class.java,
                    "priceelist_database"
                )
                    //.createFromAsset("databases/priceelist_database.db")
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}