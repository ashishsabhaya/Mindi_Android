package com.keylogic.mindi.Dialogs

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.keylogic.mindi.Database.MyPreferences
import com.keylogic.mindi.Enum.VIPStore
import com.keylogic.mindi.Helper.AdHelper
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.Helper.ProfileHelper
import com.keylogic.mindi.Helper.VIPStoreHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.BuyStoreItemLayoutBinding
import java.util.Calendar

class BuyStoreItemDialogFragment : BaseDialogFragment() {

    private var _binding: BuyStoreItemLayoutBinding? = null
    private val binding get() = _binding!!

    override fun getContentView(inflater: LayoutInflater): View {
        _binding = BuyStoreItemLayoutBinding.inflate(inflater)
        setupDialogUI()
        return binding.root
    }

    private fun setupDialogUI() {
        if (tabIndex == -1 || itemIndex == -1) {
            dismiss()
            return
        }

        var titleString = ""
        when(tabIndex) {
            VIPStore.AVATAR.tabIndex -> {
                titleString = requireContext().resources.getString(R.string.buy_avatar_title)
            }
            VIPStore.CARDS.tabIndex -> {
                titleString = requireContext().resources.getString(R.string.buy_card_back_title)
            }
            VIPStore.TABLES.tabIndex -> {
                titleString = requireContext().resources.getString(R.string.buy_table_title)
            }
            VIPStore.BACKGROUNDS.tabIndex -> {
                titleString = requireContext().resources.getString(R.string.buy_background_title)
            }
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.cancelCons, onclick = {
            dismiss()
        })

        CommonHelper.INSTANCE.setScaleOnTouch(binding.todayWatchBtnCons, onclick = {
            AdHelper.INSTANCE.showRewardedAdWithLoading(requireActivity(), onAdDismiss = { isAdComplete ->
                if (onAdDismiss)
                    buyStoreItem(false)
            })
        })

        CommonHelper.INSTANCE.setScaleOnTouch(binding.weekBuyBtnCons, onclick = {
            buyStoreItem(true)
        })

        binding.titleTxt.setText(titleString)
        binding.weekChipCountTxt.setText(VIPStoreHelper.INSTANCE.getStoreItemPrice(tabIndex, itemIndex))
    }

    private fun buyStoreItem(forWeek: Boolean) {
        VIPStoreHelper.INSTANCE.buyOrSelectStoreItem(
            context = requireContext(),
            isBuyItem = true,
            tabIndex = tabIndex,
            itemIndex = itemIndex,
            forWeek = forWeek
        )
        dismissDialog()
    }

    fun dismissDialog() {
        var tabNameId = 0
        when (tabIndex) {
            VIPStore.AVATAR.tabIndex -> {
                tabNameId = VIPStore.AVATAR.tabName
                ProfileHelper.profileId = itemIndex
            }
            VIPStore.CARDS.tabIndex -> {
                tabNameId = VIPStore.CARDS.tabName
                ProfileHelper.cardBackId = itemIndex
            }
            VIPStore.TABLES.tabIndex -> {
                tabNameId = VIPStore.TABLES.tabName
            }
            VIPStore.BACKGROUNDS.tabIndex -> {
                tabNameId = VIPStore.BACKGROUNDS.tabName
            }
        }

        val result = Bundle().apply {
            putBoolean(KEY_IS_ITEM_PURCHASED, true)
            putInt(KEY_ITEM_INDEX, itemIndex)
        }
        parentFragmentManager.setFragmentResult(requireContext().resources.getString(tabNameId), result)
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_IS_ITEM_PURCHASED = "is_item_purchased"
        const val KEY_ITEM_INDEX = "item_index"
        private const val TAG = "BuyStoreItemDialogFragment"
        private var tabIndex = -1
        private var itemIndex = -1

        fun show(activity: Activity, fm: FragmentManager, tabPosition: Int, position: Int) {
            if (activity is FragmentActivity) {
                if (ShowDialog.INSTANCE.openDialogOnce(activity, fm,TAG)) {
                    tabIndex = tabPosition
                    itemIndex = position
                    val dialog = BuyStoreItemDialogFragment()
                    dialog.show(fm, TAG)
                }
            }
        }

        fun dismiss(activity: Activity) {
            ShowDialog.INSTANCE.dismiss(activity,TAG)
        }
    }
}
