package com.keylogic.mindi.helper

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.keylogic.mindi.R
import com.keylogic.mindi.dialogs.LoadingDialogFragment
import kotlinx.coroutines.*

class AdHelper {
    companion object {
        private var currRewardedAd: RewardedAd? = null
        private var currInterstitialAd: InterstitialAd? = null
        val INSTANCE = AdHelper()
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun loadBannerAd(adView: AdView) {
        if (CommonHelper.isAdsFree) {
            adView.visibility = android.view.View.GONE
            return
        }

        coroutineScope.launch {
            val adRequest = AdRequest.Builder().build()

            withContext(Dispatchers.Main) {
                adView.adListener = object : AdListener() {
                    override fun onAdFailedToLoad(error: LoadAdError) {
                        adView.visibility = android.view.View.GONE
                    }

                    override fun onAdLoaded() {
                        adView.visibility = android.view.View.VISIBLE
                    }
                }
                adView.loadAd(adRequest)
            }
        }
    }

    fun preloadRewardedAd(context: Activity) {
        if (currRewardedAd != null || CommonHelper.isAdsFree) return

        val adRequest = AdRequest.Builder().build()
        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                RewardedAd.load(
                    context,
                    context.getString(R.string.reward_ad_unit_id),
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

        if (currRewardedAd == null)
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

    fun preloadAllAdsInBackground(activity: Activity) {
        if (CommonHelper.isAdsFree) return

        coroutineScope.launch {
            withContext(Dispatchers.Main) {
                if (currRewardedAd == null)
                    preloadRewardedAd(activity)
                if (currInterstitialAd == null)
                    preloadInterstitialAd(activity)
            }
        }
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

    fun preloadInterstitialAd(context: Activity) {
        if (currInterstitialAd != null || CommonHelper.isAdsFree) return

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            context.getString(R.string.interstitial_ad_unit_id),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    currInterstitialAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    currInterstitialAd = null
                }
            }
        )
    }

    fun showInterstitialAdWithLoading(activity: Activity, onAdDismiss: () -> Unit) {
        if (!CommonHelper.INSTANCE.checkConnection(activity, true)) return

        if (currInterstitialAd == null)
            preloadInterstitialAd(activity)
        LoadingDialogFragment.show(activity, activity.getString(R.string.ad_loading))

        val handler = Handler(Looper.getMainLooper())
        var attemptCount = 0
        val maxAttempts = 5

        val checkAndShowAd = object : Runnable {
            override fun run() {
                val ad = currInterstitialAd
                if (ad != null) {
                    LoadingDialogFragment.dismiss(activity)
                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            onAdDismiss()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            onAdDismiss()
                        }

                        override fun onAdShowedFullScreenContent() {
                            currInterstitialAd = null
                        }
                    }
                    ad.show(activity)
                    currInterstitialAd = null
                    preloadInterstitialAd(activity)
                } else if (attemptCount >= maxAttempts) {
                    LoadingDialogFragment.dismiss(activity)
                    Toast.makeText(activity, "Failed to load ad. Please try again.", Toast.LENGTH_SHORT).show()
                    preloadInterstitialAd(activity)
                } else {
                    attemptCount++
                    handler.postDelayed(this, 1000)
                }
            }
        }

        handler.post(checkAndShowAd)
    }
}
