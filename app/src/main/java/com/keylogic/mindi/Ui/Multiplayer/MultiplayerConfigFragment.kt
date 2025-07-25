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
import com.keylogic.mindi.Helper.ProfileHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.Ui.ViewModel.TableConfigViewModel
import com.keylogic.mindi.databinding.FragmentMultiplayerConfigBinding
import kotlin.text.ifEmpty

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

        CommonHelper.INSTANCE.setScaleOnTouch(binding.topTitleInclude.cancelCons, onclick = {
            findNavController().popBackStack()
        })

        CommonHelper.INSTANCE.setScaleOnTouch(binding.configInclude.createBtnCons, onclick = {
        })

        CommonHelper.INSTANCE.setScaleOnTouch(binding.joinBtnCons, onclick = {
        })

        binding.topTitleInclude.titleCons.visibility = View.GONE
        binding.topTitleInclude.chipCountTxt.setText(CommonHelper.INSTANCE.getTotalChip())
        binding.configInclude.createBtnTxt.setText(requireContext().resources.getString(R.string.create))

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
            binding.configInclude.hideModeCons.updateCheck(isHide)
            binding.configInclude.cutModeCons.updateCheck(!isHide)
        }

        viewModel.isCreateTable.observe(viewLifecycleOwner) { isCreate ->
            binding.createTableSelectionCons.updateSelection(isCreate)
            binding.joinTableSelectionCons.updateSelection(!isCreate)
            binding.configInclude.createTableCons.visibility = if (isCreate) View.VISIBLE else View.GONE
            binding.joinTableCons.visibility = if (!isCreate) View.VISIBLE else View.GONE
        }
    }

    private fun setupListeners() {
        CommonHelper.INSTANCE.setScaleOnTouch(binding.createTableSelectionCons) {
            viewModel.setCreateTable(true)
        }
        CommonHelper.INSTANCE.setScaleOnTouch(binding.joinTableSelectionCons) {
            viewModel.setCreateTable(false)
        }

        val defaultTxt = requireContext().getString(R.string.enter_your_code)
        CommonHelper.INSTANCE.setUpCursorVisibility(
            defaultTxt,
            binding.codeEditLayout,
            binding.codeEditTxt,
            binding.codeTxt,
            onUpdate = { enteredCode ->
                viewModel.updateCode(enteredCode)
            }
        )

        CommonHelper.INSTANCE.setScaleOnTouch(binding.topTitleInclude.chipCons) {
            findNavController().navigate(R.id.action_multiplayerConfigFragment_to_chipStoreFragment)
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.configInclude.deck1Cons) {
            viewModel.setDeckType(DeckType.DECK1)
        }
        CommonHelper.INSTANCE.setScaleOnTouch(binding.configInclude.deck2Cons) {
            viewModel.setDeckType(DeckType.DECK2)
        }
        CommonHelper.INSTANCE.setScaleOnTouch(binding.configInclude.deck3Cons) {
            viewModel.setDeckType(DeckType.DECK3)
        }
        CommonHelper.INSTANCE.setScaleOnTouch(binding.configInclude.deck4Cons) {
            viewModel.setDeckType(DeckType.DECK4)
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.configInclude.hideModeCons) {
            viewModel.setGameMode(true)
        }
        CommonHelper.INSTANCE.setScaleOnTouch(binding.configInclude.cutModeCons) {
            viewModel.setGameMode(false)
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.configInclude.player4Cons) {
            viewModel.setTotalPlayers(4)
        }
        CommonHelper.INSTANCE.setScaleOnTouch(binding.configInclude.player6Cons) {
            viewModel.setTotalPlayers(6)
        }
        CommonHelper.INSTANCE.setScaleOnTouch(binding.configInclude.player8Cons) {
            viewModel.setTotalPlayers(8)
        }

        binding.configInclude.customSeekBar.updateIndicatorPosition(
            binding.configInclude.betPriceIndicator,
            binding.configInclude.betPriceCountTxt
        )
    }

    private fun updateSeekBarProgress(deck: DeckType) {
        binding.configInclude.customSeekBar.setCustomProgress(deck.deckCount)
    }

    private fun updateDeckSelection(deck: DeckType) {
        binding.configInclude.deck1Cons.updateSelection(deck == DeckType.DECK1)
        binding.configInclude.deck2Cons.updateSelection(deck == DeckType.DECK2)
        binding.configInclude.deck3Cons.updateSelection(deck == DeckType.DECK3)
        binding.configInclude.deck4Cons.updateSelection(deck == DeckType.DECK4)
    }

    private fun updatePlayerOptions(deck: DeckType) {
        fun enableView(view: View, enabled: Boolean) {
            view.isEnabled = enabled
            view.alpha = if (enabled) 1f else 0.6f
        }

        enableView(binding.configInclude.player4Cons, true)
        enableView(binding.configInclude.player6Cons, true)
        enableView(binding.configInclude.player8Cons, true)

        when (deck) {
            DeckType.DECK1 -> {
                enableView(binding.configInclude.player6Cons, false)
                enableView(binding.configInclude.player8Cons, false)
            }
            DeckType.DECK2 -> {
                enableView(binding.configInclude.player8Cons, false)
            }
            else -> {
                enableView(binding.configInclude.player4Cons, false)
            }
        }
    }

    private fun updatePlayerCheck(players: Int) {
        binding.configInclude.player4Cons.updateCheck(players == 4)
        binding.configInclude.player6Cons.updateCheck(players == 6)
        binding.configInclude.player8Cons.updateCheck(players == 8)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
