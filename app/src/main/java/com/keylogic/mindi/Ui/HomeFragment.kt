package com.keylogic.mindi.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.keylogic.mindi.Adapters.GameModeAdapter
import com.keylogic.mindi.Dialogs.LoadingDialogFragment
import com.keylogic.mindi.Dialogs.ShowDialog
import com.keylogic.mindi.Enum.GameMode
import com.keylogic.mindi.Helper.AdHelper
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var shineAnim: Animation
    private lateinit var upDownAnim: Animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

        // Set touch listeners
        CommonHelper.INSTANCE.setScaleOnTouch(binding.vipCons) {
            findNavController().navigate(R.id.action_homeFragment_to_vipStoreFragment)
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.settingsCons) {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.chipCons) {
            findNavController().navigate(R.id.action_homeFragment_to_chipStoreFragment)
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.profileCons) {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.freeChipCons) {
            AdHelper.INSTANCE.showRewardedAdWithLoading(requireActivity()) { onAdDismiss ->
                if (onAdDismiss)
                    viewModel.addFreeChips(requireContext())
            }
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.statisticsCons) {
        }

        val adapter = GameModeAdapter(GameMode.entries.toList()) { mode ->
            when (mode) {
                GameMode.ONLINE -> findNavController().navigate(R.id.action_homeFragment_to_onlineConfigFragment)
                GameMode.PLAY_WITH_FRIEND -> findNavController().navigate(R.id.action_homeFragment_to_multiplayerConfigFragment)
                GameMode.OFFLINE -> findNavController().navigate(R.id.action_homeFragment_to_offlineConfigFragment)
            }
        }

        binding.gameModeRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), GameMode.entries.size)
            this.adapter = adapter
            suppressLayout(true)
        }

        shineAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.left_to_right)
        upDownAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.up_down)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        viewModel.loadProfileData(requireContext())
        startShineLoop()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadProfileData(requireContext())
    }

    override fun onPause() {
        super.onPause()
        binding.shine.clearAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.profileName.collect {
                        binding.profileNameTxt.text = it
                    }
                }

                launch {
                    viewModel.chipCount.collect {
                        binding.chipCountTxt.text = it
                    }
                }

                launch {
                    viewModel.profileImageRes.collect {
                        if (it != 0) {
                            binding.profileImg.setImageResource(it)
                        }
                    }
                }
            }
        }
    }

    private fun startShineLoop() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                while (true) {
                    binding.freeChipCountCons.startAnimation(upDownAnim)
                    binding.shine.startAnimation(shineAnim)
                    delay(shineAnim.duration + 5000)
                }
            }
        }
    }
}
