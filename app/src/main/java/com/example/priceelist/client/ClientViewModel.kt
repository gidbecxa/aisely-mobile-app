package com.example.priceelist.client

import androidx.lifecycle.*
import com.example.priceelist.data.Client
import com.example.priceelist.data.ClientDao
import kotlinx.coroutines.launch

/**
 * Refer to PriceListViewModel to understand usages of functions, methods and variables employed:
 * This is a semblance of the aforementioned.
 */

class ClientViewModel(private val clientDao: ClientDao): ViewModel() {

    val allClients: LiveData<List<Client>> = clientDao.getClients().asLiveData()

    private fun insertNewClient(item: Client) {
        viewModelScope.launch {
            clientDao.insertClient(item)
        }
    }

    private fun getNewClientEntry(
        clientName: String, clientPhone: String, clientMail: String, clientAddress: String
    ): Client {
        return Client(
            clientName = clientName, clientPhone = clientPhone, clientMail = clientMail, clientAddress = clientAddress
        )
    }

    fun addNewClient(
        clientName: String, clientPhone: String, clientMail: String, clientAddress: String
    ) {
        val newClient = getNewClientEntry(clientName, clientPhone, clientMail, clientAddress)
        insertNewClient(newClient)
    }

    fun retrieveClient(id: Int): LiveData<Client> {
        return clientDao.getClient(id).asLiveData()
    }

    fun getLastClient(): LiveData<Client> {
        return clientDao.getLastClient().asLiveData()
    }


    /*fun getSpecificClient(clientName: String, clientPhone: String,
                          clientMail: String, clientAddress: String): LiveData<Client> {
        return clientDao.getSpecificClient(clientName, clientPhone, clientMail, clientAddress).asLiveData()
    }*/

    private fun updateClient(item: Client) {
        viewModelScope.launch {
            clientDao.updateClient(item)
        }
    }

    private fun getClientUpdateEntry(
        clientId: Int, clientName: String, clientPhone: String, clientMail: String, clientAddress: String
    ): Client {
        return Client(
            id = clientId, clientName = clientName, clientPhone = clientPhone, clientMail = clientMail, clientAddress = clientAddress
        )
    }

    fun updateClient(
        clientId: Int, clientName: String, clientPhone: String, clientMail: String, clientAddress: String
    ) {
        val updatedClient = getClientUpdateEntry(clientId, clientName, clientPhone, clientMail, clientAddress)
        updateClient(updatedClient)
    }

    fun deleteClient(item: Client) {
        viewModelScope.launch {
            clientDao.deleteClient(item)
        }
    }

    fun isClientSavePossible(clientName: String, clientAddress: String, clientPhone: String): Boolean {
        if (clientName.isBlank() || clientPhone.isBlank() || clientAddress.isBlank()) { return false}
        //else
        return true
    }

}

class ClientViewModelFactory(private val clientDao: ClientDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClientViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClientViewModel(clientDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}