package com.keylogic.mindi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.keylogic.mindi.Enum.DeviceType
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.Helper.DeviceHelper
import com.keylogic.mindi.Helper.DisplayHelper
import com.keylogic.mindi.Helper.SystemUiHelper
import com.keylogic.mindi.Helper.VIPStoreHelper

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

        VIPStoreHelper.avatarList.clear()
        VIPStoreHelper.avatarList.addAll(VIPStoreHelper.INSTANCE.generateAvatarList())
        VIPStoreHelper.cardBackList.clear()
        VIPStoreHelper.cardBackList.addAll(VIPStoreHelper.INSTANCE.generateCardBackList())
        VIPStoreHelper.tablesList.clear()
        VIPStoreHelper.tablesList.addAll(VIPStoreHelper.INSTANCE.generateTableList())
        VIPStoreHelper.backgroundList.clear()
        VIPStoreHelper.backgroundList.addAll(VIPStoreHelper.INSTANCE.generateBackgroundList())

        DisplayHelper.INSTANCE.calculateScreenWH(this)

    }
}