package com.keylogic.mindi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keylogic.mindi.helper.VIPStoreHelper
import com.keylogic.mindi.models.StoreItem

class TableViewModel : ViewModel() {
    private val _tables = MutableLiveData<List<StoreItem>>()
    val tables: LiveData<List<StoreItem>> get() = _tables

    init {
        loadTables()
    }

    private fun loadTables() {
        _tables.value = VIPStoreHelper.tablesList
    }

    fun updateTables() {
        _tables.value = VIPStoreHelper.tablesList
    }

}
