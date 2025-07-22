package com.keylogic.mindi.Ui.Multiplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.keylogic.mindi.Enum.DeckType
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.Ui.ViewModel.TableConfigViewModel
import com.keylogic.mindi.databinding.FragmentMultiplayerConfigBinding

class MultiplayerConfigFragment : Fragment() {

    private var _binding: FragmentMultiplayerConfigBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TableConfigViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultiplayerConfigBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[TableConfigViewModel::class.java]

        binding.cancelCons.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.chipCountTxt.setText(CommonHelper.INSTANCE.getTotalChip())

        setupObservers()
        setupListeners()

        return binding.root
    }

    private fun setupObservers() {
        viewModel.deckType.observe(viewLifecycleOwner) { deck ->
            updateDeckSelection(deck)
            updateSeekBarProgress(deck)
            updatePlayerOptions(deck)
        }

        viewModel.totalPlayers.observe(viewLifecycleOwner) { players ->
            updatePlayerCheck(players)
        }

        viewModel.isHideMode.observe(viewLifecycleOwner) { isHide ->
            binding.hideModeCons.updateCheck(isHide)
            binding.cutModeCons.updateCheck(!isHide)
        }

        viewModel.isCreateTable.observe(viewLifecycleOwner) { isCreate ->
            binding.createTableSelectionCons.updateSelection(isCreate)
            binding.joinTableSelectionCons.updateSelection(!isCreate)
            binding.createTableCons.visibility = if (isCreate) View.VISIBLE else View.GONE
            binding.joinTableCons.visibility = if (!isCreate) View.VISIBLE else View.GONE
        }
    }

    private fun setupListeners() {
        binding.createTableSelectionCons.setOnClickListener {
            viewModel.setCreateTable(true)
        }
        binding.joinTableSelectionCons.setOnClickListener {
            viewModel.setCreateTable(false)
        }

        binding.deck1Cons.setOnClickListener { viewModel.setDeckType(DeckType.DECK1) }
        binding.deck2Cons.setOnClickListener { viewModel.setDeckType(DeckType.DECK2) }
        binding.deck3Cons.setOnClickListener { viewModel.setDeckType(DeckType.DECK3) }
        binding.deck4Cons.setOnClickListener { viewModel.setDeckType(DeckType.DECK4) }

        binding.hideModeCons.setOnClickListener { viewModel.setGameMode(true) }
        binding.cutModeCons.setOnClickListener { viewModel.setGameMode(false) }

        binding.player4Cons.setOnClickListener { viewModel.setTotalPlayers(4) }
        binding.player6Cons.setOnClickListener { viewModel.setTotalPlayers(6) }
        binding.player8Cons.setOnClickListener { viewModel.setTotalPlayers(8) }

        binding.customSeekBar.updateIndicatorPosition(binding.betPriceIndicator, binding.betPriceCountTxt)
    }

    private fun updateSeekBarProgress(deck: DeckType) {
        binding.customSeekBar.setCustomProgress(deck.deckCount)
    }

    private fun updateDeckSelection(deck: DeckType) {
        binding.deck1Cons.updateSelection(deck == DeckType.DECK1)
        binding.deck2Cons.updateSelection(deck == DeckType.DECK2)
        binding.deck3Cons.updateSelection(deck == DeckType.DECK3)
        binding.deck4Cons.updateSelection(deck == DeckType.DECK4)
    }

    private fun updatePlayerOptions(deck: DeckType) {
        fun enableView(view: View, enabled: Boolean) {
            view.isEnabled = enabled
            view.alpha = if (enabled) 1f else 0.6f
        }

        enableView(binding.player4Cons, true)
        enableView(binding.player6Cons, true)
        enableView(binding.player8Cons, true)

        when (deck) {
            DeckType.DECK1 -> {
                enableView(binding.player6Cons, false)
                enableView(binding.player8Cons, false)
            }
            DeckType.DECK2 -> {
                enableView(binding.player8Cons, false)
            }
            else -> {
                enableView(binding.player4Cons, false)
            }
        }
    }

    private fun updatePlayerCheck(players: Int) {
        binding.player4Cons.updateCheck(players == 4)
        binding.player6Cons.updateCheck(players == 6)
        binding.player8Cons.updateCheck(players == 8)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
