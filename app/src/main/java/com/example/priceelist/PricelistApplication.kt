package com.example.priceelist

import android.app.Application
import com.example.priceelist.data.PricelistRoomDatabase

class PricelistApplication: Application() {
    val database: PricelistRoomDatabase by lazy { PricelistRoomDatabase.getDatabase(this) }
}