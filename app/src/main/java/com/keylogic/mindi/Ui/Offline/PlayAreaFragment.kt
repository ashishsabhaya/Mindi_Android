package com.keylogic.mindi.Ui.Offline

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.keylogic.mindi.Custom.PlayerProfileView
import com.keylogic.mindi.Enum.DeviceType
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

        CommonHelper.INSTANCE.setScaleOnTouch(binding.cancelCons, onclick = {
            findNavController().popBackStack()
        })

        val hWidth = if (CommonHelper.deviceType == DeviceType.NORMAL)
            requireContext().resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._28sdp)
        else
            requireContext().resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._24sdp)
        val mWidth = requireContext().resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._20sdp)
        val handsWidth = hWidth + mWidth * 0
        val handsHeight = requireContext().resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._40sdp)
        for (i in 0 until totalPlayers) {
            val playerView = PlayerProfileView(requireActivity())
            playerView.setContainerSize(DisplayHelper.profileWidth, DisplayHelper.profileHeight)
            playerView.setCardViewProperties()
            playerView.setBackgroundLayoutHeight()
            playerView.setTextViewProperties()
            val resource = ProfileHelper.INSTANCE.getDefaultProfileResource(requireActivity(), i)
            playerView.updateDetails("User ${i+1}", resource)

            val currPos = PositionHelper.INSTANCE.getPlayerPosition(i+1, totalPlayers)
            playerView.x = currPos[0]
            playerView.y = currPos[1]

            binding.playAreaRelative.addView(playerView)

            if (i == 0) {
                val player2Pos = PositionHelper.INSTANCE.getPlayerPosition(i+2, totalPlayers)
                binding.leftHandLayout.root.x = player2Pos[0]
//                binding.leftHandLayout.root.y = player2Pos + (currPos[1] - player2Pos - handsHeight) / 2
                binding.leftHandLayout.root.y = currPos[1] - handsHeight * 1.02f
            }
            else if (i == totalPlayers-1) {
                val firstPos = PositionHelper.INSTANCE.getPlayerPosition(1, totalPlayers)
                binding.rightHandLayout.root.x = currPos[0] + DisplayHelper.profileWidth - handsWidth
                binding.rightHandLayout.root.y = binding.leftHandLayout.root.y - binding.leftHandLayout.root.getTriangleHeight()
            }
        }

        val arr = arrayOf(binding.leftHandLayout.spadesTxt, binding.leftHandLayout.heartsTxt,
            binding.leftHandLayout.clubsTxt, binding.leftHandLayout.diamondsTxt,
            binding.rightHandLayout.spadesTxt, binding.rightHandLayout.heartsTxt,
            binding.rightHandLayout.clubsTxt, binding.rightHandLayout.diamondsTxt)

        binding.leftHandLayout.handTxt.text = "0"
        binding.rightHandLayout.handTxt.text = "0"

        for (i in 0 until 8) {
            arr[i].text = "0"
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
