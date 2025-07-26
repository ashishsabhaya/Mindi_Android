package com.keylogic.mindi.dialogs

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.keylogic.mindi.enums.VIPStore
import com.keylogic.mindi.helper.AdHelper
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.helper.ProfileHelper
import com.keylogic.mindi.helper.VIPStoreHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.BuyStoreItemLayoutBinding

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
            AdHelper.INSTANCE.showRewardedAdWithLoading(requireActivity(), onAdDismiss = { isAdDismiss ->
                if (isAdDismiss)
                    buyStoreItem(false)
            })
        })

        CommonHelper.INSTANCE.setScaleOnTouch(binding.weekBuyBtnCons, onclick = {
            val currItem = VIPStoreHelper.INSTANCE.getStoreItem(tabIndex, itemIndex)
            if (currItem != null) {
                if (currItem.price <= ProfileHelper.totalChips) {
                    ProfileHelper.totalChips -= currItem.price
                    buyStoreItem(true)
                }
                else {
                    Toast.makeText(requireContext(), "Not enough chip dialog", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.titleTxt.text = titleString
        binding.weekChipCountTxt.text =
            VIPStoreHelper.INSTANCE.getStoreItemPrice(tabIndex, itemIndex)
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
        var tabNameId = ""
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
        parentFragmentManager.setFragmentResult(tabNameId, result)
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

//        fun dismiss(activity: Activity) {
//            ShowDialog.INSTANCE.dismiss(activity,TAG)
//        }
    }
}
