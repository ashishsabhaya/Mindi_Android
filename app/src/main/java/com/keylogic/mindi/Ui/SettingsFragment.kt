package com.keylogic.mindi.Ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)

        binding.soundSwitch.isChecked = CommonHelper.isSoundEnabled
        binding.musicSwitch.isChecked = CommonHelper.isMusicEnabled
        binding.vibrationSwitch.isChecked = CommonHelper.isVibrationEnabled

        binding.soundSwitch.setOnCheckedChangeListener { v, isChecked ->
            CommonHelper.isSoundEnabled = isChecked
        }

        binding.musicSwitch.setOnCheckedChangeListener { v, isChecked ->
            CommonHelper.isMusicEnabled = isChecked
        }

        binding.vibrationSwitch.setOnCheckedChangeListener { v, isChecked ->
            CommonHelper.isVibrationEnabled = isChecked
        }

        binding.cancelCons.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

}