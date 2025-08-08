package com.keylogic.mindi.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.keylogic.mindi.R
import com.keylogic.mindi.adapters.DailyRewardPlanAdapter
import com.keylogic.mindi.databinding.FragmentDailyRewardBinding
import com.keylogic.mindi.enums.DeviceType
import com.keylogic.mindi.gamePlay.animation.ChipAnimation
import com.keylogic.mindi.gamePlay.helper.DisplayHelper
import com.keylogic.mindi.helper.AdHelper
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.helper.DailyRewardHelper
import com.keylogic.mindi.helper.ProfileHelper
import com.keylogic.mindi.models.DailyReward

class DailyRewardFragment : Fragment() {
    private var _binding: FragmentDailyRewardBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: DailyRewardPlanAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyRewardBinding.inflate(inflater)
        setupUI()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        }

        return binding.root
    }

    private fun setupUI() {
        binding.topTitleInclude.cancelCons.visibility = View.GONE
        binding.topTitleInclude.titleTxt.text = getString(R.string.daily_reward)
        binding.topTitleInclude.chipCountTxt.text = CommonHelper.INSTANCE.getTotalChip()
        binding.topTitleInclude.chipAddImg.visibility = View.GONE

        adapter = DailyRewardPlanAdapter(requireContext(), DailyRewardHelper.dailyRewardList)
        binding.rfRecycler.layoutManager = GridLayoutManager(requireContext(), 5)
        binding.rfRecycler.adapter = adapter
        binding.rfRecycler.suppressLayout(true)

        val currReward = DailyRewardHelper.dailyRewardList[DailyRewardHelper.currDay-1]
        binding.rfChipCountTxt.text = CommonHelper.INSTANCE.formatChip(currReward.chipCount)
        binding.rfWatchAdChipCountTxt.text = CommonHelper.INSTANCE.formatChip(currReward.chipCount * 2)

        CommonHelper.INSTANCE.setScaleOnTouch(binding.rfCollectBtnCons) {
            rewardCollected(1, currReward)
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.rfWatchAndCollectBtnCons) {
            binding.rfWatchAndCollectBtnCons.isEnabled = false
            AdHelper.INSTANCE.showRewardedAdWithLoading(requireActivity(), onAdDismiss = { isAdDismiss ->
                if (isAdDismiss)
                    rewardCollected(2, currReward)
                else
                    binding.rfWatchAndCollectBtnCons.isEnabled = true
            })
        }
    }

    private fun rewardCollected(multiple: Int, currReward: DailyReward) {
        val context = requireContext()
        val startX = DisplayHelper.screenWidth / 2f - if (CommonHelper.deviceType == DeviceType.NORMAL) {
            if (multiple == 1)
                context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._96sdp)
            else
                - context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._76sdp)
        }
        else {
            if (multiple == 1)
                context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._94sdp)
            else
                - context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._78sdp)
        }

        val startY = DisplayHelper.screenHeight - 0f - if (CommonHelper.deviceType == DeviceType.NORMAL)
            context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._30sdp)
        else
            context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._28sdp)

        val endX = 0f + context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._26sdp)

        val endY = 0f + context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._18sdp)

        ChipAnimation.INSTANCE.collectDailyRewardChips(binding.rewardAnimRela, startX, startY,
            endX, endY, onAnimationComplete = {
                ProfileHelper.totalChips += currReward.chipCount * multiple
                binding.topTitleInclude.chipCountTxt.text = CommonHelper.INSTANCE.getTotalChip()
                DailyRewardHelper.isDailyRewardCollected = true
                DailyRewardHelper.INSTANCE.setNextDayReward(requireContext())
                findNavController().popBackStack()
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}