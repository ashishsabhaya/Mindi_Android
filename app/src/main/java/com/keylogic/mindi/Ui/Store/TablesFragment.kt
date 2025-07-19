package com.keylogic.mindi.Ui.Store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.FragmentTablesBinding

class TablesFragment : Fragment() {
    private var _binding: FragmentTablesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTablesBinding.inflate(layoutInflater, container,false)

        return binding.root
    }
}