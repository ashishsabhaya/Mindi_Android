package com.keylogic.mindi.dialogs

import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import com.keylogic.mindi.databinding.DialogFragmentExitGameBinding
import com.keylogic.mindi.helper.CommonHelper

class ExitGameDialogFragment : BaseDialogFragment() {
    private var _binding: DialogFragmentExitGameBinding? = null
    private val binding get() = _binding!!

    override fun getContentView(inflater: LayoutInflater): View {
        _binding = DialogFragmentExitGameBinding.inflate(inflater)
        setupDialogUI()
        return binding.root
    }

    private fun setupDialogUI() {
        CommonHelper.INSTANCE.setScaleOnTouch(binding.positiveBtnCons) {
            findNavController().popBackStack()
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.negativeBtnCons) {
            findNavController().popBackStack()
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}