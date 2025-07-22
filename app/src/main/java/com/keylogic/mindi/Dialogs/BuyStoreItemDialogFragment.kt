package com.keylogic.mindi.Dialogs

import android.app.Activity
import android.view.*
import androidx.fragment.app.FragmentActivity
import com.keylogic.mindi.Helper.VIPStoreHelper
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
            dismiss(requireActivity())
            return
        }

        var titleString = ""
        when(tabIndex) {
            0 -> {
                titleString = requireContext().resources.getString(R.string.buy_avatar_title)
            }
            1 -> {
                titleString = requireContext().resources.getString(R.string.buy_card_back_title)
            }
            2 -> {
                titleString = requireContext().resources.getString(R.string.buy_table_title)
            }
            3 -> {
                titleString = requireContext().resources.getString(R.string.buy_background_title)
            }
        }

        binding.cancelCons.setOnClickListener {
            dismiss(requireActivity())
        }

        binding.titleTxt.setText(titleString)
        binding.weekChipCountTxt.setText(VIPStoreHelper.INSTANCE.getStoreItemPrice(tabIndex, itemIndex))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "BuyStoreItemDialogFragment"
        private var tabIndex = -1
        private var itemIndex = -1

        fun show(activity: Activity, tabPosition: Int, position: Int) {
            if (activity is FragmentActivity) {
                val fm = activity.supportFragmentManager
                if (ShowDialog.INSTANCE.openDialogOnce(activity, fm,TAG)) {
                    tabIndex = tabPosition
                    itemIndex = position
                    val dialog = BuyStoreItemDialogFragment()
                    dialog.show(activity.supportFragmentManager, TAG)
                }
            }
        }

        fun dismiss(activity: Activity) {
            ShowDialog.INSTANCE.dismiss(activity,TAG)
        }
    }
}
