package com.keylogic.mindi.helper

import com.keylogic.mindi.models.ChipPlan

class ChipStoreHelper {
    companion object {
        val INSTANCE = ChipStoreHelper()

        val chipPlanList = ArrayList<ChipPlan>()
    }

    fun generateChipStorePlans() {
        chipPlanList.clear()
        chipPlanList.add(ChipPlan(
            planIndex = 0,
            totalChips = 25_000L,
            planPrice = "₹ 99",
            isRemoveAds = false,
            isRestore = false
        ))
        chipPlanList.add(ChipPlan(
            planIndex = 1,
            totalChips = 50_000L,
            planPrice = "₹ 199",
            isRemoveAds = false,
            isRestore = false
        ))
        chipPlanList.add(ChipPlan(
            planIndex = 2,
            totalChips = 1_50_000L,
            planPrice = "₹ 299",
            isRemoveAds = true,
            isRestore = false
        ))
        chipPlanList.add(ChipPlan(
            planIndex = 3,
            totalChips = 0L,
            planPrice = "",
            isRemoveAds = false,
            isRestore = true
        ))
    }

}