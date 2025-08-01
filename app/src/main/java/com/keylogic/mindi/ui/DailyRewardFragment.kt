package com.keylogic.mindi.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.keylogic.mindi.R
import com.keylogic.mindi.adapters.DailyRewardPlanAdapter
import com.keylogic.mindi.databinding.FragmentDailyRewardBinding
import com.keylogic.mindi.helper.AdHelper
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.helper.DailyRewardHelper
import com.keylogic.mindi.helper.ProfileHelper

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
            binding.rfCollectBtnCons.isEnabled = false
            ProfileHelper.totalChips += currReward.chipCount
            DailyRewardHelper.isDailyRewardCollected = true
            DailyRewardHelper.INSTANCE.setNextDayReward(requireContext())

            binding.topTitleInclude.chipCountTxt.text = CommonHelper.INSTANCE.getTotalChip()
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().popBackStack()
            }, 1_000L)
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.rfWatchAndCollectBtnCons) {
            binding.rfWatchAndCollectBtnCons.isEnabled = false
            AdHelper.INSTANCE.showRewardedAdWithLoading(requireActivity(), onAdDismiss = { isAdDismiss ->
                if (isAdDismiss) {
                    ProfileHelper.totalChips += currReward.chipCount * 2
                    binding.topTitleInclude.chipCountTxt.text = CommonHelper.INSTANCE.getTotalChip()
                    DailyRewardHelper.isDailyRewardCollected = true
                    DailyRewardHelper.INSTANCE.setNextDayReward(requireContext())

                    Handler(Looper.getMainLooper()).postDelayed({
                        findNavController().popBackStack()
                    }, 1_000L)
                }
                else
                    binding.rfWatchAndCollectBtnCons.isEnabled = true
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}