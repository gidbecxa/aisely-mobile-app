package com.example.priceelist.account

import androidx.lifecycle.*
import com.example.priceelist.data.Stats
import com.example.priceelist.data.StatsDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class StatsViewModel(private val statsDao: StatsDao) : ViewModel() {

    val allHistory: LiveData<List<Stats>> = statsDao.getHistory().asLiveData()

    val listHistory: LiveData<List<Stats>> = statsDao.getItemHistory("Li").asLiveData()
    val invoiceHistory: LiveData<List<Stats>> = statsDao.getItemHistory("In").asLiveData()
    val receiptHistory: LiveData<List<Stats>> = statsDao.getItemHistory("Re").asLiveData()

    fun getInvNumCreated(): LiveData<Int> {
        return statsDao.getCreatedCount("In")
    }

    fun getRctNumCreated(): LiveData<Int> {
        return statsDao.getCreatedCount("Re")
    }

    fun getInvNumSent(): LiveData<Int> {
        return statsDao.getSentCount("In")
    }

    fun getRctNumSent(): LiveData<Int> {
        return statsDao.getSentCount("Re")
    }

    fun getInvNumCleared(): LiveData<Int> {
        return statsDao.getClearedCount("In")
    }

    fun getInvSumCreated(): LiveData<Double> {
        return statsDao.getSumCreated("In")
    }

    fun getRctSumCreated(): LiveData<Double> {
        return statsDao.getSumCreated("Re")
    }

    fun getInvSumSent(): LiveData<Double> {
        return statsDao.getSumSent("In")
    }

    fun getRctSumSent(): LiveData<Double> {
        return statsDao.getSumSent("Re")
    }

    fun getInvSumCleared(): LiveData<Double> {
        return statsDao.getSumCleared("In")
    }

    private fun insertStat(item: Stats) {
        viewModelScope.launch { statsDao.insert(item) }
    }

    private fun getStatEntry(
        itemName: String,
        itemType: String,
        action: String,
        actionDate: String,
        amount: String
    ): Stats {
        return Stats(
            itemName = itemName,
            itemType = itemType,
            action = action,
            actionDate = actionDate,
            amount = amount.toDoubleOrNull()
        )
    }

    fun addStat(
        itemName: String,
        itemType: String,
        action: String,
        actionDate: String,
        amount: String
    ) {
        val stat = getStatEntry(itemName, itemType, action, actionDate, amount)
        insertStat(stat)
    }
}

class StatsViewModelFactory(private val statsDao: StatsDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatsViewModel(statsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}