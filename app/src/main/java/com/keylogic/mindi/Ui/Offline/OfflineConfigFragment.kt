package com.keylogic.mindi.Ui.Offline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.keylogic.mindi.Adapters.DeckSelectionAdapter
import com.keylogic.mindi.Enum.DeckType
import com.keylogic.mindi.Enum.GameMode
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.FragmentOfflineConfigBinding

class OfflineConfigFragment : Fragment() {
    private var _binding: FragmentOfflineConfigBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOfflineConfigBinding.inflate(inflater, container, false)

        val adapter = DeckSelectionAdapter(DeckType.entries.toList(), onItemClick = { mode ->
            val action = OfflineConfigFragmentDirections
                .actionOfflineConfigFragmentToTableConfigFragment(
                    deckType = mode
                )
            findNavController().navigate(action)
        })
        binding.deckTypeRecycler.adapter = adapter
        binding.deckTypeRecycler.suppressLayout(true)
        binding.deckTypeRecycler.layoutManager = GridLayoutManager(requireActivity(), DeckType.entries.toList().size)

        binding.cancelCons.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

}