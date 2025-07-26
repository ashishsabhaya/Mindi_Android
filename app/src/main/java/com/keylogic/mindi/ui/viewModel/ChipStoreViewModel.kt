package com.keylogic.mindi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keylogic.mindi.helper.ChipStoreHelper
import com.keylogic.mindi.models.ChipPlan

class ChipStoreViewModel : ViewModel() {
    private val _chipPlans = MutableLiveData<List<ChipPlan>>()
    val chipPlans: LiveData<List<ChipPlan>> get() = _chipPlans

    init {
        loadChipPlans()
    }

    private fun loadChipPlans() {
        _chipPlans.value = ChipStoreHelper.chipPlanList
    }

    // Optional: add update/refresh methods here
}
