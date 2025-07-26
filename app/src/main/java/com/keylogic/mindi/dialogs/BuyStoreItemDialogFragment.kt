package com.keylogic.mindi.dialogs

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
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
    private var tabIndex = -1
    private var itemIndex = -1

    override fun getContentView(inflater: LayoutInflater): View {
        _binding = BuyStoreItemLayoutBinding.inflate(inflater)
        tabIndex = requireArguments().getInt(KEY_TAB_INDEX)
        itemIndex = requireArguments().getInt(KEY_ITEM_INDEX)
        setupDialogUI()
        return binding.root
    }

    private fun setupDialogUI() {
        if (tabIndex == -1 || itemIndex == -1) {
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
            findNavController().popBackStack()
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
                    if (findNavController().currentDestination?.id == R.id.buyStoreItemDialogFragment) {
                        val bundle = Bundle().apply {
                            putBoolean(NotEnoughChipDialogFragment.KEY_IS_NOT_ENOUGH_FOR_STORE, true)
                        }
                        findNavController().navigate(R.id.notEnoughChipDialogFragment,bundle)
                    }
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
        requireActivity().supportFragmentManager.setFragmentResult(tabNameId, result)
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_IS_ITEM_PURCHASED = "is_item_purchased"
        const val KEY_ITEM_INDEX = "itemIndex"
        const val KEY_TAB_INDEX = "tabIndex"

    }
}
