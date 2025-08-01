package com.keylogic.mindi.models

data class DailyReward(
    var dayCount: Int,
    var chipCount: Long,
    var isCurrentDay: Boolean,
    var isCollected: Boolean
)