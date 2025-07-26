package com.keylogic.mindi.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.NotEnoughChipLayoutBinding
import com.keylogic.mindi.helper.AdHelper
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.helper.ProfileHelper
import com.keylogic.mindi.ui.viewModel.VipStoreViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

class NotEnoughChipDialogFragment : BaseDialogFragment() {

    private var _binding: NotEnoughChipLayoutBinding? = null
    private val binding get() = _binding!!
    private var isNotEnoughChipForStore = true
    private lateinit var upDownAnim: Animation
    private val vipStoreViewModel: VipStoreViewModel by activityViewModels()

    override fun getContentView(inflater: LayoutInflater): View {
        _binding = NotEnoughChipLayoutBinding.inflate(inflater)
        isNotEnoughChipForStore = requireArguments().getBoolean(KEY_IS_NOT_ENOUGH_FOR_STORE)
        upDownAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.up_down_enough_chip)
        setupDialogUI()
        return binding.root
    }

    private fun setupDialogUI() {
        CommonHelper.INSTANCE.setScaleOnTouch(binding.cancelCons, onclick = {
            findNavController().popBackStack()
        })

        binding.centerBackgroundCons.setSpotlightBackgroundResource()
        startPopUpAnimation()
        if (!isNotEnoughChipForStore) {
            binding.dialogTitleTxt.text = resources.getString(R.string.not_enough_chip_for_game)
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.cancelCons) {
            findNavController().popBackStack()
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.positiveBtnCons) {
            findNavController().popBackStack()
            findNavController().navigate(R.id.action_vipStoreFragment_to_buyStoreItemDialogFragment)
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
