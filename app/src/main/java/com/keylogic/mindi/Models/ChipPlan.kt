package com.keylogic.mindi.Models

data class ChipPlan(
    val planIndex: Int,
    val totalChips: Long,
    val planPrice: String,
    val isRemoveAds: Boolean = false,
    val isRestore: Boolean = false
)