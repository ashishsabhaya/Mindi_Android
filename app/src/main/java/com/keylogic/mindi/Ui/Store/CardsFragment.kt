package com.keylogic.mindi.Ui.Store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.keylogic.mindi.Adapters.CardBackAdapter
import com.keylogic.mindi.Dialogs.BuyStoreItemDialogFragment
import com.keylogic.mindi.Enum.VIPStore
import com.keylogic.mindi.Helper.VIPStoreHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.Ui.ViewModel.CardBackViewModel
import com.keylogic.mindi.databinding.FragmentCardsBinding

class CardsFragment : Fragment() {
    private var _binding: FragmentCardsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CardBackViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardsBinding.inflate(inflater, container, false)

        viewModel.cardBacks.observe(viewLifecycleOwner) { cardBackList ->
            val adapter = CardBackAdapter(requireContext(), cardBackList, onItemClick = { position ->
                BuyStoreItemDialogFragment.show(requireActivity(), VIPStore.CARDS.tabIndex, position)
            })
            binding.cardBackRecycler.adapter = adapter
            binding.cardBackRecycler.layoutManager = GridLayoutManager(requireContext(), 5)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
