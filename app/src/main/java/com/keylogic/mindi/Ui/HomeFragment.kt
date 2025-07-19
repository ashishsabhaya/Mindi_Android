package com.keylogic.mindi.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.keylogic.mindi.Adapters.GameModeAdapter
import com.keylogic.mindi.Enum.GameMode
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.Helper.ProfileHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

        binding.vipCons.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_vipStoreFragment)
        }

        binding.settingsCons.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }

        binding.chipCons.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_chipStoreFragment)
        }

        binding.profileCons.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        val adapter = GameModeAdapter(GameMode.entries.toList(), onItemClick = { mode ->
            when(mode) {
                GameMode.ONLINE -> {
                    findNavController().navigate(R.id.action_homeFragment_to_onlineConfigFragment)
                }
                GameMode.PLAY_WITH_FRIEND -> {
                    findNavController().navigate(R.id.action_homeFragment_to_multiplayerConfigFragment)
                }
                GameMode.OFFLINE -> {
                    findNavController().navigate(R.id.action_homeFragment_to_offlineConfigFragment)
                }
            }
        })
        binding.gameModeRecycler.adapter = adapter
        binding.gameModeRecycler.suppressLayout(true)
        binding.gameModeRecycler.layoutManager = GridLayoutManager(requireActivity(), GameMode.entries.toList().size)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val resource = ProfileHelper.INSTANCE.getDefaultProfileResource(requireContext(),
            ProfileHelper.defaultProfileId)
        binding.profileImg.setImageResource(resource)
        binding.profileNameTxt.text = ProfileHelper.profileName
        binding.chipCountTxt.text = CommonHelper.INSTANCE.getTotalChip()
    }

}