package com.keylogic.mindi.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.keylogic.mindi.Adapters.ProfileSelectionAdapter
import com.keylogic.mindi.Helper.ProfileHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.FragmentProfileSelectionBinding

class ProfileSelectionFragment : Fragment() {
    private var _binding: FragmentProfileSelectionBinding? = null
    private val binding get() = _binding!!
    var selectedProfile = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileSelectionBinding.inflate(layoutInflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

        updateProfile(selectedProfile)
        val adapter = ProfileSelectionAdapter(requireContext(),8, onItemClick = { position ->
            selectedProfile = position
            updateProfile(position)
        })

        binding.profileRecycler.adapter = adapter
        binding.profileRecycler.layoutManager = GridLayoutManager(requireContext(),5)

        binding.submitCons.setOnClickListener {
            var name = binding.profileNameEditTxt.text ?: ""
            if (name.isBlank()) {
                name = ProfileHelper.INSTANCE.getRandomProfileName()
            }
            ProfileHelper.profileName = name.toString()
            ProfileHelper.defaultProfileId = selectedProfile
            ProfileHelper.profileUID = ProfileHelper.INSTANCE.generateUniqueKey()
            findNavController().navigate(R.id.action_profileSelectionFragment_to_homeFragment)
        }

        return binding.root
    }

    private fun updateProfile(position: Int) {
        ProfileHelper.defaultProfileId = position
        val resource = ProfileHelper.INSTANCE.getDefaultProfileResource(requireContext(), position)
        binding.selectedProfileImg.setImageResource(resource)
    }

}