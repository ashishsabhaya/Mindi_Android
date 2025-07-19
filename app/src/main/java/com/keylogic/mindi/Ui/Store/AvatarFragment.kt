package com.keylogic.mindi.Ui.Store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.keylogic.mindi.Adapters.AvatarAdapter
import com.keylogic.mindi.Helper.VIPStoreHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.FragmentAvatarBinding

class AvatarFragment : Fragment() {
    private var _binding: FragmentAvatarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAvatarBinding.inflate(layoutInflater, container, false)

        val list = VIPStoreHelper.INSTANCE.generateAvatarList()

        val adapter = AvatarAdapter(requireContext(), list, onItemClick = { position ->

        })
        binding.avatarRecycler.adapter = adapter
        binding.avatarRecycler.layoutManager = GridLayoutManager(requireContext(),5)

        return binding.root
    }

}