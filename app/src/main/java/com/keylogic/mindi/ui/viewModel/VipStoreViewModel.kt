package com.keylogic.mindi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keylogic.mindi.enums.VIPStore
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.helper.ProfileHelper

class VipStoreViewModel : ViewModel() {

    private val _tabNames = MutableLiveData<List<String>>()
    val tabNames: LiveData<List<String>> get() = _tabNames

    private val _chipCount = MutableLiveData(ProfileHelper.totalChips)
    val chipCount: LiveData<Long> get() = _chipCount

    private val _selectedTab = MutableLiveData<Int>()
    val selectedTab: LiveData<Int> get() = _selectedTab

    init {
        _tabNames.value = VIPStore.entries.map { it.tabName }
//        _chipCount.value = ProfileHelper.totalChips
        _selectedTab.value = 0
    }

    fun onTabSelected(index: Int) {
        _selectedTab.value = index
    }

    fun refreshChipCount() {
        println("B > ${_chipCount.value} ==> ${ProfileHelper.totalChips}")
        _chipCount.value = 0L
        println("AB > ${_chipCount.value} ==> ${ProfileHelper.totalChips}")
        _chipCount.value = ProfileHelper.totalChips
        println("A > ${_chipCount.value} ==> ${ProfileHelper.totalChips}")
    }
}
