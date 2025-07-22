package com.keylogic.mindi.Ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.keylogic.mindi.Helper.ProfileHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.Ui.ViewModel.ProfileViewModel
import com.keylogic.mindi.databinding.FragmentProfileBinding
import android.content.Context

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val context = requireContext()

        // Initial name display
        binding.profileNameTxt.text = ProfileHelper.profileName.ifEmpty {
            context.getString(R.string.enter_your_name)
        }

        // 1. Show EditText when TextView is clicked
        binding.profileNameTxt.setOnClickListener {
            binding.profileNameTxt.visibility = View.GONE
            binding.profileNameEditLayout.visibility = View.VISIBLE
            binding.profileNameEditTxt.isCursorVisible = true
            binding.profileNameEditTxt.requestFocus()
            showKeyboard(binding.profileNameEditTxt)
        }

        // 2. Handle focus loss (keyboard dismissed)
        binding.profileNameEditTxt.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                finishEditingName()
            }
        }

        // 3. Handle "Done" action from keyboard
        binding.profileNameEditTxt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.profileNameEditTxt.clearFocus() // triggers focus listener
                true
            } else {
                false
            }
        }

        // Observers
        viewModel.profileName.observe(viewLifecycleOwner) {
            binding.profileNameEditTxt.setText(it)
        }

        viewModel.gameWin.observe(viewLifecycleOwner) {
            binding.gameWinIconCons.setViewTextAndResource(it.toString(), 0)
        }

        viewModel.gameLost.observe(viewLifecycleOwner) {
            binding.gameLostIconCons.setViewTextAndResource(it.toString(), 0)
        }

        viewModel.gamePlayed.observe(viewLifecycleOwner) {
            binding.gamePlayedIconCons.setViewTextAndResource(it.toString(), 0)
        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Finalize editing, hide keyboard and update name
    private fun finishEditingName() {
        val newName = binding.profileNameEditTxt.text.toString().trim()
        viewModel.updateProfileName(newName)

        binding.profileNameTxt.text = newName.ifEmpty {
            requireContext().getString(R.string.enter_your_name)
        }

        binding.profileNameEditLayout.visibility = View.GONE
        binding.profileNameTxt.visibility = View.VISIBLE
        binding.profileNameEditTxt.isCursorVisible = false
        hideKeyboard(binding.profileNameEditTxt)
    }

    private fun hideKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showKeyboard(view: View) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}
