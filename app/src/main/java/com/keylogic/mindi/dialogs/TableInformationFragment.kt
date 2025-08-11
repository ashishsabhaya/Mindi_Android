package com.keylogic.mindi.dialogs

import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.keylogic.mindi.R
import com.keylogic.mindi.adapters.TableInfoAdapter
import com.keylogic.mindi.databinding.DialogFragmentTableInformationBinding
import com.keylogic.mindi.enums.GameOptions
import com.keylogic.mindi.gamePlay.helper.GameHelper
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.models.TableInformation

class TableInformationFragment : BaseFullDialogFragment() {
    private var _binding: DialogFragmentTableInformationBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TableInfoAdapter

    override fun getContentView(inflater: LayoutInflater): View {
        _binding = DialogFragmentTableInformationBinding.inflate(inflater)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        binding.topTitleInclude.chipCons.visibility = View.GONE
        binding.topTitleInclude.titleTxt.text = GameOptions.TABLE_INFO.oName

        CommonHelper.Companion.INSTANCE.setScaleOnTouch(binding.topTitleInclude.cancelCons) {
            findNavController().popBackStack()
        }

        val list = ArrayList<TableInformation>()
        generateTableInformation(list)

        adapter = TableInfoAdapter(list)
        binding.tableInformationRecycler.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.tableInformationRecycler.adapter = adapter
    }

    private fun generateTableInformation(list: ArrayList<TableInformation>) {
        val betPrice = CommonHelper.INSTANCE.getChip(GameHelper.tableConfig.betPrice)
        val totalPlayer = GameHelper.tableConfig.totalPlayers
        val totalDeck = GameHelper.tableConfig.deckType.deckCount
        val hukumMode = if (GameHelper.tableConfig.isHideMode)
            getMsgString(R.string.table_hide_mode)
        else
            getMsgString(R.string.table_katte_mode)

        list.add(TableInformation(getMsgString(R.string.table_bet_price), betPrice))
        list.add(TableInformation(getMsgString(R.string.table_no_of_player),totalPlayer.toString()))
        list.add(TableInformation(getMsgString(R.string.table_no_of_deck),totalDeck.toString()))
        list.add(TableInformation(getMsgString(R.string.table_hukum_mode), hukumMode))
//        list.add(TableInformation(getMsgString(R.string.game_result),getMsgString(R.string.table_result_msg)))
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setWindowAnimations(R.style.DialogRightInAnimationEntryExit)
    }

    fun getMsgString(id: Int): String {
        return requireContext().getString(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
