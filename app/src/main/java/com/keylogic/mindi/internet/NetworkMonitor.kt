package com.keylogic.mindi.internet

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.keylogic.mindi.helper.CommonHelper

class NetworkMonitor(context: Context, private val listener: NetworkStateListener) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            listener.onNetworkAvailable()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            listener.onNetworkLost()
        }

        override fun onUnavailable() {
            super.onUnavailable()
            listener.onNetworkLost()
        }
    }

    fun register() {
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        val isConnected =
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        if (isConnected) {
            listener.onNetworkAvailable()
        } else {
            listener.onNetworkLost()
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    fun unregister() {
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } catch (_: Exception) {
            CommonHelper.print("NetworkMonitor = Callback not registered or already unregistered")
        }
    }

    interface NetworkStateListener {
        fun onNetworkAvailable()
        fun onNetworkLost()
    }
}
