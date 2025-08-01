package com.keylogic.mindi.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.DialogFragmentNotEnoughChipBinding
import com.keylogic.mindi.helper.AdHelper
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.helper.ProfileHelper
import com.keylogic.mindi.ui.viewModel.VipStoreViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotEnoughChipDialogFragment : BaseDialogFragment() {
    private var _binding: DialogFragmentNotEnoughChipBinding? = null
    private val binding get() = _binding!!
    private var isNotEnoughChipForStore = -1
    private lateinit var upDownAnim: Animation
    private val vipStoreViewModel: VipStoreViewModel by activityViewModels()

    override fun getContentView(inflater: LayoutInflater): View {
        _binding = DialogFragmentNotEnoughChipBinding.inflate(inflater)
        isNotEnoughChipForStore = requireArguments().getInt(KEY_IS_NOT_ENOUGH_FOR_STORE)
        upDownAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.up_down_enough_chip)
        setupDialogUI()
        return binding.root
    }

    private fun setupDialogUI() {
        if (isNotEnoughChipForStore == -1)
            return
        CommonHelper.INSTANCE.setScaleOnTouch(binding.cancelCons, onclick = {
            findNavController().popBackStack()
        })

        binding.centerBackgroundCons.setSpotlightBackgroundResource()
        var message = ""
        startPopUpAnimation()
        when(isNotEnoughChipForStore) {
            0 -> message = resources.getString(R.string.not_enough_chips_avatar)
            1 -> message = resources.getString(R.string.not_enough_chips_cards)
            2 -> message = resources.getString(R.string.not_enough_chips_tables)
            3 -> message = resources.getString(R.string.not_enough_chips_background)
            11 -> message = resources.getString(R.string.not_enough_chip_create_table)
            22 -> message = resources.getString(R.string.not_enough_chip_join_table)
            33 -> message = resources.getString(R.string.not_enough_chip_find_table)
        }
        binding.dialogTitleTxt.text = message

        CommonHelper.INSTANCE.setScaleOnTouch(binding.cancelCons) {
            findNavController().popBackStack()
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.positiveBtnCons) {
            findNavController().popBackStack()
            if (findNavController().currentDestination?.id == R.id.buyStoreItemDialogFragment) {
                findNavController().navigate(R.id.chipStoreFragment)
            }
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.negativeBtnCons) {
            AdHelper.INSTANCE.showRewardedAdWithLoading(requireActivity()) { isAdDismiss ->
                if (isAdDismiss) {
                    ProfileHelper.totalChips += ProfileHelper.freeChipCount
                    vipStoreViewModel.refreshChipCount()
                    findNavController().popBackStack()
                }
            }
        }
    }

    fun startPopUpAnimation() {
        CoroutineScope(Dispatchers.Main).launch {
            while (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                binding.freeChipCountCons.startAnimation(upDownAnim)
                delay(upDownAnim.duration + 5000)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_IS_NOT_ENOUGH_FOR_STORE = "isNotEnoughChipForStore"
    }

}