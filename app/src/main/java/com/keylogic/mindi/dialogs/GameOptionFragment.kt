package com.keylogic.mindi.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keylogic.mindi.R
import com.keylogic.mindi.adapters.GameOptionAdapter
import com.keylogic.mindi.databinding.DialogFragmentGameOptionBinding
import com.keylogic.mindi.enums.GameOptions
import com.keylogic.mindi.helper.AdHelper
import com.keylogic.mindi.helper.CommonHelper

class GameOptionFragment : BaseFullDialogFragment() {
    private var _binding: DialogFragmentGameOptionBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: GameOptionAdapter

    override fun getContentView(inflater: LayoutInflater): View {
        _binding = DialogFragmentGameOptionBinding.inflate(inflater)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        CommonHelper.Companion.INSTANCE.setScaleOnTouch(binding.cancelCons) {
            findNavController().popBackStack()
        }

        binding.gameOptionBg.setOnClickListener { findNavController().popBackStack() }

        AdHelper.INSTANCE.loadBannerAd(binding.bannerAdView)

        adapter = GameOptionAdapter(
            GameOptions.entries.toList(), onItemClick = { option ->
                when (option) {
                    GameOptions.SOUND -> CommonHelper.isSoundEnabled = !CommonHelper.isSoundEnabled
                    GameOptions.BACKGROUND_MUSIC -> CommonHelper.isMusicEnabled = !CommonHelper.isMusicEnabled
                    GameOptions.VIBRATE -> CommonHelper.isVibrationEnabled = !CommonHelper.isVibrationEnabled
                    GameOptions.TABLE_INFO -> {
                        if (findNavController().currentDestination?.id == R.id.gameOptionFragment) {
                            findNavController().popBackStack()
                            findNavController().navigate(R.id.tableInformationFragment)
                        }
                    }
                    GameOptions.HELP -> {
                        if (findNavController().currentDestination?.id == R.id.gameOptionFragment) {
                            findNavController().popBackStack()
                            findNavController().navigate(R.id.helpFragment)
                        }
                    }
                    GameOptions.EXIT -> {
                        if (findNavController().currentDestination?.id == R.id.gameOptionFragment) {
                            findNavController().popBackStack()
                            val bundle = Bundle().apply {
                                putBoolean(ExitGameDialogFragment.KEY_IS_GAME_EXIT, true)
                            }
                            findNavController().navigate(R.id.exitGameDialogFragment, bundle)
                        }
                    }
                }
            })

        binding.gameOptionRecycler.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)
        binding.gameOptionRecycler.adapter = adapter
        binding.gameOptionRecycler.suppressLayout(true)

    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setWindowAnimations(R.style.DialogLeftInAnimationEntryExit)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
