package com.keylogic.mindi.models

import com.keylogic.mindi.helper.CommonHelper

data class StoreItem(
    val uId: Int,
    var isSelected: Boolean,
    val price: Long,
    var purchaseDate: Long = 0,
    var purchaseEndDate: Long = 0
) {
    fun getFormatedChip(): String {
        return CommonHelper.INSTANCE.formatChip(price)
    }
}