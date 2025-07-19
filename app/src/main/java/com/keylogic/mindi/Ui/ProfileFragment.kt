package com.keylogic.mindi.Ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.keylogic.mindi.Helper.ProfileHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        binding.gameWinIconCons.setViewTextAndResource(ProfileHelper.gameWin.toString(),0)
        binding.gameLostIconCons.setViewTextAndResource(ProfileHelper.gameLost.toString(),0)
        binding.gamePlayedIconCons.setViewTextAndResource(ProfileHelper.gamePlayed.toString(),0)

        binding.profileNameEditTxt.setText(ProfileHelper.profileName)

        binding.cancelCons.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.editProfileCard.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_vipStoreFragment)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.selectedProfileImg.setImageResource(ProfileHelper.INSTANCE.getProfileResource(requireContext()))
    }

}