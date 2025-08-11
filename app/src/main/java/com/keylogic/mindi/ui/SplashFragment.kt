package com.keylogic.mindi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.keylogic.mindi.database.MyPreferences
import com.keylogic.mindi.helper.ChipStoreHelper
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.helper.ProfileHelper
import com.keylogic.mindi.helper.VIPStoreHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.FragmentSplashBinding
import com.keylogic.mindi.helper.DailyRewardHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        ChipStoreHelper.INSTANCE.generateChipStorePlans()
        DailyRewardHelper.INSTANCE.generateDailyRewards()
        DailyRewardHelper.INSTANCE.updateTodayReward()
        VIPStoreHelper.INSTANCE.updateVIPStoreDetails()

        CommonHelper.shouldShowNetworkDialog = false
        ProfileHelper.totalChips = 50_500L

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(2_000L)
            proceedAfterSplash()
        }
    }

    private fun proceedAfterSplash() {
        val currentDateMillis = System.currentTimeMillis()

        CommonHelper.isNewDay = !isSameDay(CommonHelper.lastSavedDate, currentDateMillis)

        if (CommonHelper.isNewDay) {
            CommonHelper.lastSavedDate = currentDateMillis
            MyPreferences.INSTANCE.saveGameSettings(requireContext())
        }

        if (CommonHelper.isNewUser)
            findNavController().navigate(R.id.action_splashFragment_to_profileSelectionFragment)
        else
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
    }

    private fun isSameDay(time1: Long, time2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply { timeInMillis = time1 }
        val cal2 = Calendar.getInstance().apply { timeInMillis = time2 }

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
