package com.keylogic.mindi.Helper

import android.content.Context
import android.content.res.Configuration
import kotlin.math.sqrt

class DeviceHelper(val context: Context) {

    enum class DType(val device: String) {
        MOBILE("Mobile"),
        TABLET("Tablet")
    }

    enum class DeviceSizeCategory {
        NORMAL,
        LARGE_DEVICE
    }

    fun getDeviceSizeCategory(): DeviceSizeCategory {
        return try {
            val screenLayout = context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
            if (screenLayout >= Configuration.SCREENLAYOUT_SIZE_LARGE || getDeviceDiagonalInches(context) >= 7.0) {
                DeviceSizeCategory.LARGE_DEVICE
            } else {
                DeviceSizeCategory.NORMAL
            }
        } catch (e: Exception) {
            DeviceSizeCategory.NORMAL
        }
    }

    private fun getDeviceDiagonalInches(context: Context): Double {
        return try {
            val displayMetrics = context.resources.displayMetrics
            val xInches = displayMetrics.widthPixels / displayMetrics.xdpi
            val yInches = displayMetrics.heightPixels / displayMetrics.ydpi
            sqrt((xInches * xInches + yInches * yInches).toDouble())
        } catch (e: Exception) {
            0.0
        }
    }
}
