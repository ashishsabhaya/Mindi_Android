package com.keylogic.mindi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.keylogic.mindi.enums.DeviceType
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.helper.DeviceHelper
import com.keylogic.mindi.helper.DisplayHelper

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val deviceHelper = DeviceHelper(this)
        val sizeCategory = deviceHelper.getDeviceSizeCategory()

        CommonHelper.deviceType = when (sizeCategory) {
            DeviceHelper.DeviceSizeCategory.NORMAL -> {
                DeviceType.NORMAL
            }

            DeviceHelper.DeviceSizeCategory.LARGE_DEVICE -> {
                DeviceType.LARGE
            }
        }

        DisplayHelper.INSTANCE.calculateScreenWH(this)

    }
}