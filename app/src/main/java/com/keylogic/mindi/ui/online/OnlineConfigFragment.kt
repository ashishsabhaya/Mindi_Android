package com.keylogic.mindi.ui.online

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.keylogic.mindi.enums.DeckType
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.ui.viewModel.TableConfigViewModel
import com.keylogic.mindi.databinding.FragmentOnlineConfigBinding

class OnlineConfigFragment : Fragment() {
    private var _binding: FragmentOnlineConfigBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: TableConfigViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnlineConfigBinding.inflate(layoutInflater, container,false)
        viewModel = ViewModelProvider(this)[TableConfigViewModel::class.java]

        CommonHelper.INSTANCE.setScaleOnTouch(binding.topTitleInclude.cancelCons, onclick = {
            findNavController().popBackStack()
        })

        CommonHelper.INSTANCE.setScaleOnTouch(binding.configInclude.createBtnCons, onclick = {
        })

        binding.topTitleInclude.titleTxt.text = requireContext().resources.getString(R.string.find_table)
        binding.topTitleInclude.chipCountTxt.text = CommonHelper.INSTANCE.getTotalChip()
        binding.configInclude.createBtnTxt.text = requireContext().resources.getString(R.string.find)

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
            binding.configInclude.createTableCons.visibility = if (isCreate) View.VISIBLE else View.GONE
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.topTitleInclude.chipCons, onclick = {
            findNavController().navigate(R.id.action_onlineConfigFragment_to_chipStoreFragment)
        })
    }

    private fun setupListeners() {
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
