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
        println("SplashFragment ==> onCreateView")

        ChipStoreHelper.INSTANCE.generateChipStorePlans()
        updateVIPStoreDetails()

        return binding.root
    }

    private fun updateVIPStoreDetails() {
        for (avatar in VIPStoreHelper.avatarList) {
            if (CommonHelper.INSTANCE.isTimePassed(System.currentTimeMillis(), avatar.purchaseEndDate)) {
                avatar.purchaseDate = 0
                avatar.purchaseEndDate = 0
                if (avatar.isSelected) {
                    ProfileHelper.profileId = -1
                    avatar.isSelected = false
                }
            }
        }

        for (cards in VIPStoreHelper.cardBackList) {
            if (CommonHelper.INSTANCE.isTimePassed(System.currentTimeMillis(), cards.purchaseEndDate)) {
                cards.purchaseDate = 0
                cards.purchaseEndDate = 0
                if (cards.isSelected) {
                    ProfileHelper.cardBackId = -1
                    cards.isSelected = false
                }
            }
        }

        for (table in VIPStoreHelper.tablesList) {
            if (CommonHelper.INSTANCE.isTimePassed(System.currentTimeMillis(), table.purchaseEndDate)) {
                table.purchaseDate = 0
                table.purchaseEndDate = 0
                if (table.isSelected) {
                    ProfileHelper.tableId = -1
                    table.isSelected = false
                }
            }
        }

        for (background in VIPStoreHelper.backgroundList) {
            if (CommonHelper.INSTANCE.isTimePassed(System.currentTimeMillis(), background.purchaseEndDate)) {
                background.purchaseDate = 0
                background.purchaseEndDate = 0
                if (background.isSelected) {
                    ProfileHelper.backgroundId = -1
                    background.isSelected = false
                }
            }
        }
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
