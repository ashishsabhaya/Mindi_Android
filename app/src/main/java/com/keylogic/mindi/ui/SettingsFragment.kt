package com.keylogic.mindi.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.Share.AppShare
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

        binding.topTitleInclude.titleTxt.text = requireContext().resources.getString(R.string.settings)
        binding.topTitleInclude.chipCons.visibility = View.GONE

        binding.soundSwitch.setOnCheckedChangeListener { v, isChecked ->
            CommonHelper.isSoundEnabled = isChecked
        }

        binding.musicSwitch.setOnCheckedChangeListener { v, isChecked ->
            CommonHelper.isMusicEnabled = isChecked
        }

        binding.vibrationSwitch.setOnCheckedChangeListener { v, isChecked ->
            CommonHelper.isVibrationEnabled = isChecked
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.topTitleInclude.cancelCons, onclick = {
            findNavController().popBackStack()
        })

        CommonHelper.INSTANCE.setScaleOnTouch(binding.privacyPolicyCons, onclick = {
        })

        CommonHelper.INSTANCE.setScaleOnTouch(binding.feedbackCons, onclick = {
        })

        CommonHelper.INSTANCE.setScaleOnTouch(binding.rateUsCons, onclick = {
        })

        CommonHelper.INSTANCE.setScaleOnTouch(binding.howToPlayCons, onclick = {
        })

        CommonHelper.INSTANCE.setScaleOnTouch(binding.instagramShare, onclick = {
            AppShare.shareInstagram(requireContext())
        })

        CommonHelper.INSTANCE.setScaleOnTouch(binding.facebookShare, onclick = {
            AppShare.shareFacebook(requireContext())
        })

        CommonHelper.INSTANCE.setScaleOnTouch(binding.whatsappShare, onclick = {
            AppShare.shareWhatsApp(requireContext())
        })

        CommonHelper.INSTANCE.setScaleOnTouch(binding.anyShare, onclick = {
            AppShare.shareGeneric(requireContext())
        })

        return binding.root
    }

}