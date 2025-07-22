package com.keylogic.mindi.Ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keylogic.mindi.Helper.VIPStoreHelper
import com.keylogic.mindi.Models.Tables

class TableViewModel : ViewModel() {
    private val _tables = MutableLiveData<List<Tables>>()
    val tables: LiveData<List<Tables>> get() = _tables

    init {
        loadTables()
    }

    private fun loadTables() {
        _tables.value = VIPStoreHelper.tablesList
    }
}
