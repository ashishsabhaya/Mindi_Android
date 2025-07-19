package com.keylogic.mindi.Ui.Store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.keylogic.mindi.Adapters.CardBackAdapter
import com.keylogic.mindi.Helper.VIPStoreHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.FragmentCardsBinding

class CardsFragment : Fragment() {
    private var _binding: FragmentCardsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardsBinding.inflate(layoutInflater, container, false)

        val list = VIPStoreHelper.INSTANCE.generateCardBackList()

        val adapter = CardBackAdapter(requireContext(), list, onItemClick = { position ->

        })
        binding.cardBackRecycler.adapter = adapter
        binding.cardBackRecycler.layoutManager = GridLayoutManager(requireContext(), 5)

        return binding.root
    }
}