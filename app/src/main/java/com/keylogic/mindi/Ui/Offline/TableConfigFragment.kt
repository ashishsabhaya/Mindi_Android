package com.keylogic.mindi.Ui.Offline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.keylogic.mindi.Enum.DeckType
import com.keylogic.mindi.Helper.CommonHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.Ui.ViewModel.TableConfigViewModel
import com.keylogic.mindi.databinding.FragmentTableConfigBinding

class TableConfigFragment : Fragment() {
    private var _binding: FragmentTableConfigBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: TableConfigViewModel
    private val args: TableConfigFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTableConfigBinding.inflate(layoutInflater, container,false)
        viewModel = ViewModelProvider(this)[TableConfigViewModel::class.java]

        viewModel.setDeckType(args.deckType)

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

        binding.createCons.setOnClickListener {
            val deckType = viewModel.deckType.value
            val isHideMode = viewModel.isHideMode.value
            val totalPlayers = viewModel.totalPlayers.value
            viewModel.setBetPrice(binding.customSeekBar.getBetPrice())
            val betPrice = viewModel.betPrice.value

            if (deckType != null && isHideMode != null && totalPlayers != null && betPrice != null) {
                val action = TableConfigFragmentDirections
                    .actionTableConfigFragmentToPlayAreaFragment(
                        deckType = deckType,
                        isHideMode = isHideMode,
                        totalPlayers = totalPlayers,
                        betPrice = betPrice
                    )
                findNavController().navigate(action)
            } else {
                println("Null value ==> $deckType | $isHideMode | $totalPlayers | $betPrice")
            }
        }
    }

    private fun setupListeners() {
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
