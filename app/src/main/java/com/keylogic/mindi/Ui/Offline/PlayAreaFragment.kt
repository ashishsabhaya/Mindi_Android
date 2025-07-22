package com.keylogic.mindi.Ui.Offline

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.keylogic.mindi.Custom.PlayerProfileView
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.Helper.DisplayHelper
import com.keylogic.mindi.Helper.PositionHelper
import com.keylogic.mindi.Helper.ProfileHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.Ui.ViewModel.TableConfigViewModel
import com.keylogic.mindi.databinding.FragmentPlayAreaBinding

class PlayAreaFragment() : Fragment() {
    private var _binding: FragmentPlayAreaBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TableConfigViewModel
    private val args: PlayAreaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayAreaBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[TableConfigViewModel::class.java]
        val deckType = args.deckType
        val isHideMode = args.isHideMode
        val totalPlayers = args.totalPlayers
        val betPrice = args.betPrice

        val chipCountTxt = CommonHelper.INSTANCE.getChip(betPrice * totalPlayers)
        binding.bitPriceCons.setViewTextAndResource(chipCountTxt, R.drawable.ic_chip)

        binding.cancelCons.setOnClickListener {
            findNavController().popBackStack()
        }

        for (i in 0 until totalPlayers) {
            val playerView = PlayerProfileView(requireActivity())
            playerView.setContainerSize(DisplayHelper.profileWidth, DisplayHelper.profileHeight)
            playerView.setCardViewProperties()
            playerView.setBackgroundLayoutHeight()
            playerView.setTextViewProperties()
            val resource = ProfileHelper.INSTANCE.getDefaultProfileResource(requireActivity(), i)
            playerView.updateDetails("User ${i+1}", resource)

            val arr = PositionHelper.INSTANCE.getPlayerPosition(i+1, totalPlayers)
            playerView.x = arr[0]
            playerView.y = arr[1]

            binding.playAreaRelative.addView(playerView)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
