package com.keylogic.mindi.Ui.Store

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.keylogic.mindi.Adapters.AvatarAdapter
import com.keylogic.mindi.Dialogs.BuyStoreItemDialogFragment
import com.keylogic.mindi.Enum.VIPStore
import com.keylogic.mindi.Helper.VIPStoreHelper
import com.keylogic.mindi.R
import com.keylogic.mindi.Ui.ViewModel.AvatarViewModel
import com.keylogic.mindi.databinding.FragmentAvatarBinding

class AvatarFragment : Fragment() {
    private var _binding: FragmentAvatarBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AvatarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAvatarBinding.inflate(inflater, container, false)

        viewModel.avatars.observe(viewLifecycleOwner) { avatarList ->
            val adapter = AvatarAdapter(requireContext(), avatarList, onItemClick = { position ->
                BuyStoreItemDialogFragment.show(requireActivity(), VIPStore.AVATAR.tabIndex, position)
            })
            binding.avatarRecycler.adapter = adapter
            binding.avatarRecycler.layoutManager = GridLayoutManager(requireContext(), 5)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
