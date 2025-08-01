package com.keylogic.mindi.dialogs

import android.content.Context
import android.net.*
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.DialogFragmentInternetErrorBinding
import com.keylogic.mindi.helper.CommonHelper

class InternetErrorDialogFragment : BaseDialogFragment() {

    private var _binding: DialogFragmentInternetErrorBinding? = null
    private val binding get() = _binding!!

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    override fun getContentView(inflater: LayoutInflater): View {
        _binding = DialogFragmentInternetErrorBinding.inflate(inflater)
        setupDialogUI()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setupNetworkCallback()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterNetworkCallback()
        _binding = null
    }

    private fun setupDialogUI() {
        CommonHelper.INSTANCE.setScaleOnTouch(binding.positiveBtnCons) {
            val message = requireContext().resources.getString(R.string.check_internet)
            LoadingDialogFragment.show(requireActivity(), message)
            if (isInternetAvailable(requireContext())) {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupNetworkCallback() {
        connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // Internet is back, dismiss the dialog
                if (isInternetAvailable(requireContext())) {
                    dismissAllowingStateLoss()
                }
            }
        }

        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    private fun unregisterNetworkCallback() {
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isInternetAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
