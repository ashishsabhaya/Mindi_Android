package com.keylogic.mindi.helper

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.keylogic.mindi.dialogs.LoadingDialogFragment
import com.keylogic.mindi.R
import kotlinx.coroutines.*

class AdHelper {
    companion object {
        private var currRewardedAd: RewardedAd? = null
        val INSTANCE = AdHelper()
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun preloadRewardedAd(context: Activity) {
        if (currRewardedAd != null || CommonHelper.isAdsFree) return

        val adRequest = AdRequest.Builder().build()
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                RewardedAd.load(
                    context,
                    context.getString(R.string.ads_unit_id),
                    adRequest,
                    object : RewardedAdLoadCallback() {
                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            currRewardedAd = null
                        }

                        override fun onAdLoaded(ad: RewardedAd) {
                            currRewardedAd = ad
                        }
                    }
                )
            }
        }
    }

    fun showRewardedAdWithLoading(activity: Activity, onAdDismiss: (Boolean) -> Unit) {
        if (!CommonHelper.INSTANCE.checkConnection(activity, true)) return

        // Start loading if not loaded already
        preloadRewardedAd(activity)
        LoadingDialogFragment.show(activity, activity.getString(R.string.ad_loading))

        val handler = Handler(Looper.getMainLooper())
        var attemptCount = 0
        val maxAttempts = 5

        val checkAndShowAd = object : Runnable {
            override fun run() {
                val ad = currRewardedAd
                if (ad != null) {
                    LoadingDialogFragment.dismiss(activity)
                    showRewardedAd(activity, ad, onAdDismiss)
                    currRewardedAd = null
                    preloadRewardedAd(activity)
                } else if (attemptCount >= maxAttempts) {
                    LoadingDialogFragment.dismiss(activity)
                    Toast.makeText(activity, "Failed to load ad. Please try again.", Toast.LENGTH_SHORT).show()
                    preloadRewardedAd(activity)
                } else {
                    attemptCount++
                    handler.postDelayed(this, 1000)
                }
            }
        }

        handler.post(checkAndShowAd)
    }

    private fun showRewardedAd(
        activity: Activity,
        ad: RewardedAd,
        onAdDismiss: (Boolean) -> Unit
    ) {
        var rewardEarned = false

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                onAdDismiss(rewardEarned)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                onAdDismiss(false)
            }

            override fun onAdShowedFullScreenContent() {
                currRewardedAd = null
            }
        }

        ad.show(activity) {
            rewardEarned = true
        }
    }
}
