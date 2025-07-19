package com.keylogic.mindi.Ui.Multiplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.keylogic.mindi.Enum.DeckType
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.FragmentMultiplayerConfigBinding

class MultiplayerConfigFragment : Fragment() {

    private var _binding: FragmentMultiplayerConfigBinding? = null
    val binding get() = _binding!!
    private var isCreateTable = true
    private var isHideMode = false
    private var deckType = DeckType.DECK1
    private var totalPlayers = 4

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMultiplayerConfigBinding.inflate(inflater, container, false)

        binding.cancelCons.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.chipCountTxt.setText(CommonHelper.INSTANCE.getTotalChip())

        binding.customSeekBar.updateIndicatorPosition(binding.betPriceIndicator,binding.betPriceCountTxt)


        binding.createTableCons.visibility = View.VISIBLE
        binding.createTableSelectionCons.setOnClickListener {
            isCreateTable = true
            binding.createTableSelectionCons.updateSelection(isCreateTable)
            binding.joinTableSelectionCons.updateSelection(!isCreateTable)
            binding.createTableCons.visibility = View.VISIBLE
            binding.joinTableCons.visibility = View.GONE
        }

        binding.joinTableSelectionCons.setOnClickListener {
            isCreateTable = false
            binding.createTableSelectionCons.updateSelection(isCreateTable)
            binding.joinTableSelectionCons.updateSelection(!isCreateTable)
            binding.createTableCons.visibility = View.GONE
            binding.joinTableCons.visibility = View.VISIBLE
        }

        updateDeckType()
        binding.deck1Cons.setOnClickListener {
            deckType = DeckType.DECK1
            updateDeckType()
        }

        binding.deck2Cons.setOnClickListener {
            deckType = DeckType.DECK2
            updateDeckType()
        }

        binding.deck3Cons.setOnClickListener {
            deckType = DeckType.DECK3
            updateDeckType()
        }

        binding.deck4Cons.setOnClickListener {
            deckType = DeckType.DECK4
            updateDeckType()
        }

        updateGameMode()
        binding.hideModeCons.setOnClickListener {
            isHideMode = true
            updateGameMode()
        }

        binding.cutModeCons.setOnClickListener {
            isHideMode = false
            updateGameMode()
        }

        updatePlayer(true)
        binding.player4Cons.setOnClickListener {
            totalPlayers = 4
            updatePlayer()
        }

        binding.player6Cons.setOnClickListener {
            totalPlayers = 6
            updatePlayer()
        }

        binding.player8Cons.setOnClickListener {
            totalPlayers = 8
            updatePlayer()
        }

        return binding.root
    }

    private fun updatePlayer(asPerDeck: Boolean = false) {
        fun isViewEnabled(view: View, isEnabled: Boolean) {
            view.isEnabled = isEnabled
            if (isEnabled)
                view.alpha = 1f
            else
                view.alpha = 0.6f
        }
        binding.player4Cons.updateCheck(false)
        binding.player6Cons.updateCheck(false)
        binding.player8Cons.updateCheck(false)

        if (asPerDeck) {
            isViewEnabled(binding.player4Cons,true)
            isViewEnabled(binding.player6Cons,true)
            isViewEnabled(binding.player8Cons,true)
            when (deckType) {
                DeckType.DECK1 -> {
                    totalPlayers = 4
                    isViewEnabled(binding.player6Cons,false)
                    isViewEnabled(binding.player8Cons,false)
                }
                DeckType.DECK2 -> {
                    totalPlayers = 4
                    isViewEnabled(binding.player8Cons,false)
                }
                else -> {
                    totalPlayers = 6
                    isViewEnabled(binding.player4Cons,false)
                }
            }
        }

        when(totalPlayers) {
            4 -> binding.player4Cons.updateCheck(true)
            6 -> binding.player6Cons.updateCheck(true)
            8 -> binding.player8Cons.updateCheck(true)
        }

    }

    private fun updateGameMode() {
        binding.hideModeCons.updateCheck(isHideMode)
        binding.cutModeCons.updateCheck(!isHideMode)
    }

    private fun updateDeckType() {
        updateSeekBarProgress()
        binding.deck1Cons.updateSelection(false)
        binding.deck2Cons.updateSelection(false)
        binding.deck3Cons.updateSelection(false)
        binding.deck4Cons.updateSelection(false)

        updatePlayer(true)

        when(deckType) {
            DeckType.DECK1 -> binding.deck1Cons.updateSelection(true)
            DeckType.DECK2 -> binding.deck2Cons.updateSelection(true)
            DeckType.DECK3 -> binding.deck3Cons.updateSelection(true)
            DeckType.DECK4 -> binding.deck4Cons.updateSelection(true)
        }
    }

    private fun updateSeekBarProgress() {
        binding.customSeekBar.setCustomProgress(deckType.deckCount * 0.05f,deckType.deckCount * 0.05f)
    }

}