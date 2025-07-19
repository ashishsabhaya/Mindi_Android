package com.keylogic.mindi.Helper

class CommonHelper {
    companion object {
        val INSTANCE = CommonHelper()

        var isMusicEnabled = true
        var isSoundEnabled = false
        var isVibrationEnabled = true
    }

    fun formatChip(num: Long, inGlobalFormat: Boolean = false): String {
        if (inGlobalFormat) {
            return when {
                num < 1_000L -> num.toString()
                num < 1_000_000L -> {
                    // Thousands
                    String.format("%.1fk", num / 1_000.0)
                        .trimEnd('0').trimEnd('.')
                }
                num < 1_000_000_000L -> {
                    // Millions
                    String.format("%.2fm", num / 1_000_000.0)
                        .trimEnd('0').trimEnd('.')
                }
                else -> {
                    // Billions
                    String.format("%.2fb", num / 1_000_000_000.0)
                        .trimEnd('0').trimEnd('.')
                }
            }
        }
        else {
            return when {
                num < 1_000L -> num.toString()
                num < 1_00_000L -> {
                    // Thousands
                    String.format("%.1fk", num / 1_000.0)
                        .trimEnd('0').trimEnd('.')
                }
                num < 1_00_00_000L -> {
                    // Lakhs
                    String.format("%.1fl", num / 1_00_000.0)
                        .trimEnd('0').trimEnd('.')
                }
                else -> {
                    // Crores
                    String.format("%.2fcr", num / 1_00_00_000.0)
                        .trimEnd('0').trimEnd('.')
                }
            }
        }
    }

    fun getChip(count: Long): String {
        return formatChip(count)
    }

    fun getTotalChip(): String {
        return formatChip(ProfileHelper.totalChips)
    }

    fun getLeftTime(timer: Long): String {
        return timer.toString()
    }

    fun getItemSelected(): String {
        return "Selected"
    }

}