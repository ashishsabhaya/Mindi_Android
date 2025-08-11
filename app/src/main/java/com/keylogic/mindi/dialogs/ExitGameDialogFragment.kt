package com.keylogic.mindi.dialogs

import android.view.LayoutInflater
import android.view.View
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.DialogFragmentExitGameBinding
import com.keylogic.mindi.helper.AdHelper
import com.keylogic.mindi.helper.CommonHelper
import kotlin.system.exitProcess

class ExitGameDialogFragment : BaseDialogFragment() {
    private var _binding: DialogFragmentExitGameBinding? = null
    private val binding get() = _binding!!
    private var isGameExit = false

    override fun getContentView(inflater: LayoutInflater): View {
        _binding = DialogFragmentExitGameBinding.inflate(inflater)
        isGameExit = requireArguments().getBoolean(KEY_IS_GAME_EXIT)
        setupDialogUI()
        return binding.root
    }

    private fun setupDialogUI() {
        CommonHelper.INSTANCE.setScaleOnTouch(binding.positiveBtnCons) {
            findNavController().popBackStack()
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.negativeBtnCons) {
            if (isGameExit) {
                AdHelper.INSTANCE.showInterstitialAdWithLoading(requireActivity(), onAdDismiss = {
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.home, true)
                        .build()
                    findNavController().navigate(R.id.homeFragment, null, navOptions)
                })
            }
            else {
                requireActivity().finishAffinity()
                exitProcess(0)
            }
        }
    }

    companion object {
        const val KEY_IS_GAME_EXIT = "is_game_exit"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}