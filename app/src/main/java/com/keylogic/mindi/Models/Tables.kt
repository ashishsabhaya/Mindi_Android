package com.keylogic.mindi.Models

import com.keylogic.mindi.Helper.CommonHelper

data class Tables(
    val uId: Int,
    val isSelected: Boolean,
    val price: Long,
    val purchaseDate: Long
) {
    fun getFormatedChip(): String {
        return CommonHelper.INSTANCE.formatChip(price)
    }
}
