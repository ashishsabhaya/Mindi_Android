package com.keylogic.mindi.ad

import android.app.Activity
import android.app.Application
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Date
import java.util.concurrent.TimeUnit

class AppOpenAdManager(private val app: Application) {
    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    private var isShowingAd = false
    private var loadTime: Long = 0
    private val AD_UNIT_ID = "ca-app-pub-3940256099942544/9257395921"

    private val isAdAvailable: Boolean
        get() = appOpenAd != null && (Date().time - loadTime) < TimeUnit.HOURS.toMillis(4)

    fun loadAd() {
        if (isLoadingAd || isAdAvailable) return
        isLoadingAd = true
        AppOpenAd.load(
            app,
            AD_UNIT_ID,
            AdRequest.Builder().build(),
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    loadTime = Date().time
                    isLoadingAd = false
                    Log.d("AppOpenAd", "Loaded")
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    isLoadingAd = false
                    Log.w("AppOpenAd", "Load failed: ${error.message}")
                }
            }
        )
    }

    fun showAdIfAvailable(activity: Activity, onComplete: () -> Unit = {}) {
        if (isShowingAd) return
        if (!isAdAvailable) {
            onComplete()
            loadAd()
            return
        }
        isShowingAd = true
        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
                isShowingAd = false
                onComplete()
                loadAd() // preload next ad
            }
        }
        appOpenAd?.show(activity)
    }
}
