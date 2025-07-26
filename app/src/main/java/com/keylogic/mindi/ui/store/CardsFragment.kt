package com.keylogic.mindi.ui.store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.keylogic.mindi.adapters.StoreAdapter
import com.keylogic.mindi.dialogs.BuyStoreItemDialogFragment
import com.keylogic.mindi.enums.VIPStore
import com.keylogic.mindi.helper.VIPStoreHelper
import com.keylogic.mindi.ui.viewModel.CardBackViewModel
import com.keylogic.mindi.databinding.FragmentCardsBinding

class CardsFragment : Fragment() {
    private var _binding: FragmentCardsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CardBackViewModel by viewModels()
    private lateinit var cardsAdapter: StoreAdapter
    private val currTab = VIPStore.CARDS

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardsBinding.inflate(inflater, container, false)

        setupFragmentResultListener()

        cardsAdapter = StoreAdapter(
            requireContext(),
            currTab.tabIndex,
            emptyList(),
            onItemClick = { position ->
                val cardsList = viewModel.cardBacks.value ?: return@StoreAdapter
                if (position !in cardsList.indices) return@StoreAdapter

                if (cardsList[position].purchaseEndDate == 0L) {
                    BuyStoreItemDialogFragment.show(
                        requireActivity(),
                        childFragmentManager,
                        currTab.tabIndex,
                        position
                    )
                } else {
                    VIPStoreHelper.INSTANCE.buyOrSelectStoreItem(
                        context = requireContext(),
                        isBuyItem = false,
                        tabIndex = currTab.tabIndex,
                        itemIndex = position,
                        forWeek = false
                    )
                    cardsAdapter.notifyItemChanged(position)
                }
            }
        )

        binding.cardBackRecycler.adapter = cardsAdapter
        binding.cardBackRecycler.layoutManager = GridLayoutManager(requireContext(), 5)

        viewModel.cardBacks.observe(viewLifecycleOwner) { cardsList ->
            cardsAdapter.updateList(cardsList)
        }

        return binding.root
    }

    private fun setupFragmentResultListener() {
        childFragmentManager.setFragmentResultListener(
            currTab.tabName,
            viewLifecycleOwner
        ) { _, bundle ->
            val isPurchased = bundle.getBoolean(BuyStoreItemDialogFragment.KEY_IS_ITEM_PURCHASED, false)
            val index = bundle.getInt(BuyStoreItemDialogFragment.KEY_ITEM_INDEX, -1)
            if (isPurchased && index >= 0) {
                viewModel.updateCardBacks()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
