package com.keylogic.mindi.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.keylogic.mindi.Adapters.ProfileSelectionAdapter
import com.keylogic.mindi.Database.MyPreferences
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.Helper.ProfileHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.Ui.ViewModel.ProfileSelectionViewModel
import com.keylogic.mindi.databinding.FragmentProfileSelectionBinding
import kotlin.text.ifEmpty

class ProfileSelectionFragment : Fragment() {

    private var _binding: FragmentProfileSelectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileSelectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileSelectionBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

        val defaultTxt = ProfileHelper.profileName.ifEmpty {
            requireContext().getString(R.string.enter_your_name)
        }
        CommonHelper.INSTANCE.setUpCursorVisibility(defaultTxt, binding.profileNameEditLayout, binding.profileNameEditTxt,
            binding.profileNameTxt, onUpdate = { newName ->
                viewModel.updateProfileName(newName)
            })

        val adapter = ProfileSelectionAdapter(requireContext(), 8, onItemClick = { position ->
            viewModel.updateSelectedProfile(position)
        })

        binding.profileRecycler.layoutManager = GridLayoutManager(requireContext(), 5)
        binding.profileRecycler.adapter = adapter

        viewModel.selectedProfile.observe(viewLifecycleOwner) { position ->
            val resource = viewModel.getProfileResource(requireContext())
            binding.selectedProfileImg.setImageResource(resource)
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.submitCons, onclick = {
            val enteredName = binding.profileNameEditTxt.text?.toString()?.trim() ?: ""
            viewModel.updateProfileName(enteredName)
            viewModel.submitProfile()

            CommonHelper.isNewUser = false
            MyPreferences.INSTANCE.saveGameDetails(requireContext())

            findNavController().navigate(R.id.action_profileSelectionFragment_to_homeFragment)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
