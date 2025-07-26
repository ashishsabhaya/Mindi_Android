package com.keylogic.mindi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keylogic.mindi.helper.ProfileHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.ui.viewModel.ProfileViewModel
import com.keylogic.mindi.databinding.FragmentProfileBinding
import com.keylogic.mindi.database.MyPreferences
import com.keylogic.mindi.helper.CommonHelper

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Set header text
        binding.topTitleInclude.titleTxt.text = requireContext().resources.getString(R.string.profile_details)
        binding.topTitleInclude.chipCountTxt.text = CommonHelper.INSTANCE.getTotalChip()

        // Observe profile name
        viewModel.profileName.observe(viewLifecycleOwner) {
            binding.profileNameEditTxt.setText(it)
        }

        // Observe profile image
        viewModel.profileImageIndex.observe(viewLifecycleOwner) {
            val resource = ProfileHelper.INSTANCE.getProfileResource(requireContext())
            binding.selectedProfileImg.setImageResource(resource)
        }

        // Set up edit text behavior
        val defaultTxt = ProfileHelper.profileName.ifEmpty {
            requireContext().getString(R.string.enter_your_name)
        }

        CommonHelper.INSTANCE.setUpCursorVisibility(
            defaultTxt,
            binding.profileNameEditLayout,
            binding.profileNameEditTxt,
            binding.profileNameTxt,
            onUpdate = { newName ->
                viewModel.updateProfileName(newName)
            }
        )

        // Observe game stats
        viewModel.gameWin.observe(viewLifecycleOwner) {
            binding.gameWinIconCons.setViewTextAndResource(it.toString(), 0)
        }

        viewModel.gameLost.observe(viewLifecycleOwner) {
            binding.gameLostIconCons.setViewTextAndResource(it.toString(), 0)
        }

        viewModel.gamePlayed.observe(viewLifecycleOwner) {
            binding.gamePlayedIconCons.setViewTextAndResource(it.toString(), 0)
        }

        // Chip button
        CommonHelper.INSTANCE.setScaleOnTouch(binding.topTitleInclude.chipCons) {
            findNavController().navigate(R.id.action_profileFragment_to_chipStoreFragment)
        }

        // Cancel/back button
        CommonHelper.INSTANCE.setScaleOnTouch(binding.topTitleInclude.cancelCons) {
            MyPreferences.INSTANCE.saveGameProfileDetails(requireContext())
            findNavController().popBackStack()
        }

        // Edit profile image button (example navigation)
        CommonHelper.INSTANCE.setScaleOnTouch(binding.editProfileCard) {
            findNavController().navigate(R.id.action_profileFragment_to_vipStoreFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
