package com.keylogic.mindi

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.navigation.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.keylogic.mindi.dialogs.LoadingDialogFragment
import com.keylogic.mindi.enums.DeviceType
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.gamePlay.helper.DeviceHelper
import com.keylogic.mindi.gamePlay.helper.DisplayHelper
import com.keylogic.mindi.helper.AdHelper
import com.keylogic.mindi.internet.NetworkMonitor

class MainActivity : BaseActivity(), NetworkMonitor.NetworkStateListener {
    private lateinit var networkMonitor: NetworkMonitor
    var shouldShowNetworkDialog: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        networkMonitor = NetworkMonitor(this,this)

        val deviceHelper = DeviceHelper(this)
        val sizeCategory = deviceHelper.getDeviceSizeCategory()

        CommonHelper.deviceType = when (sizeCategory) {
            DeviceHelper.DeviceSizeCategory.NORMAL -> DeviceType.NORMAL
            DeviceHelper.DeviceSizeCategory.LARGE_DEVICE -> DeviceType.LARGE
        }

        DisplayHelper.INSTANCE.calculateScreenWH(this)

        AppOpenAd.load(
            this,
            "AD_UNIT_ID",
            AdRequest.Builder().build(),
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                }
            },
        )

    }

    override fun onResume() {
        super.onResume()
        networkMonitor.register()
        AdHelper.INSTANCE.preloadAllAdsInBackground(this)
        val app = application as MyApplication
        app.appOpenAdManager.showAdIfAvailable(this)
    }

    override fun onPause() {
        super.onPause()
        networkMonitor.unregister()
    }

    override fun onNetworkAvailable() {
        runOnUiThread {
            LoadingDialogFragment.dismiss(this)
            val navController = findNavController(R.id.nav_graph_fragment)
            if (navController.currentDestination?.id == R.id.internetErrorDialogFragment) {
                navController.popBackStack()
            }
        }
    }

    override fun onNetworkLost() {
        if (!shouldShowNetworkDialog) return  //Don't show if flag is false

        runOnUiThread {
            val controller = findNavController(R.id.nav_graph_fragment)
            if (controller.currentDestination?.id != R.id.internetErrorDialogFragment) {
                controller.navigate(R.id.internetErrorDialogFragment)
            }
        }
    }

    fun recheckNetworkState() {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetwork
        val capabilities = cm.getNetworkCapabilities(activeNetwork)
        val isConnected =
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        if (!isConnected) {
            onNetworkLost()
        }
    }

}
