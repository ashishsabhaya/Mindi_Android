package com.keylogic.mindi.dialogs

import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.keylogic.mindi.R
import com.keylogic.mindi.adapters.ResultProfileAdapter
import com.keylogic.mindi.databinding.DialogFragmentGameResultBinding
import com.keylogic.mindi.gamePlay.models.Score
import com.keylogic.mindi.helper.CommonHelper
import com.keylogic.mindi.models.ResultProfile

class GameResultFragment : BaseFullDialogFragment() {
    private var _binding: DialogFragmentGameResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var wAdapter: ResultProfileAdapter
    private lateinit var lAdapter: ResultProfileAdapter
    private val winnerList = ArrayList<ResultProfile>()
    private val loserList = ArrayList<ResultProfile>()
    private var winnerScore = Score()
    private var loserScore = Score()

    override fun getContentView(inflater: LayoutInflater): View {
        _binding = DialogFragmentGameResultBinding.inflate(inflater)
        CommonHelper.print("W > ${requireArguments().getString(KEY_WINNER_LIST_JSON)}")
        CommonHelper.print("L > ${requireArguments().getString(KEY_LOSER_LIST_JSON)}")
        val wListJson: String = requireArguments().getString(KEY_WINNER_LIST_JSON) ?: "[]"
        val lListJson: String = requireArguments().getString(KEY_LOSER_LIST_JSON) ?: "[]"
        val wScoreJson: String = requireArguments().getString(KEY_WINNER_SCORE_JSON) ?: ""
        val lScoreJson: String = requireArguments().getString(KEY_LOSER_SCORE_JSON) ?: ""

        val gson = Gson()
        val wList: List<ResultProfile> = gson.fromJson(
            wListJson,
            object : TypeToken<List<ResultProfile>>() {}.type
        )

        val lList: List<ResultProfile> = gson.fromJson(
            lListJson,
            object : TypeToken<List<ResultProfile>>() {}.type
        )
        winnerList.clear()
        winnerList.addAll(wList)

        loserList.clear()
        loserList.addAll(lList)

        if (wScoreJson.isNotEmpty())
            winnerScore = gson.fromJson(wScoreJson, Score::class.java)
        if (lScoreJson.isNotEmpty())
            loserScore = gson.fromJson(lScoreJson, Score::class.java)

        setupUI()
        return binding.root
    }

    private fun setupUI() {
        binding.topTitleInclude.chipCons.visibility = View.GONE
        binding.topTitleInclude.cancelCons.visibility = View.GONE
        binding.topTitleInclude.titleTxt.text = getString(R.string.game_result)

        wAdapter = ResultProfileAdapter(requireContext(), winnerList)
        binding.winnerRecycler.adapter = wAdapter
        binding.winnerRecycler.layoutManager = GridLayoutManager(requireActivity(),winnerList.size.coerceAtLeast(1))
        binding.winnerRecycler.suppressLayout(true)

        lAdapter = ResultProfileAdapter(requireContext(), loserList)
        binding.loserRecycler.adapter = lAdapter
        binding.loserRecycler.layoutManager = GridLayoutManager(requireActivity(),loserList.size.coerceAtLeast(1))
        binding.loserRecycler.suppressLayout(true)

        binding.winnerScoreLayout.countHandTxt.text = winnerScore.hands.toString()
        binding.winnerScoreLayout.countSpadesTxt.text = winnerScore.spades.toString()
        binding.winnerScoreLayout.countHeartsTxt.text = winnerScore.hearts.toString()
        binding.winnerScoreLayout.countClubsTxt.text = winnerScore.clubs.toString()
        binding.winnerScoreLayout.countDiamondsTxt.text = winnerScore.diamonds.toString()

        binding.loserScoreLayout.countHandTxt.text = loserScore.hands.toString()
        binding.loserScoreLayout.countSpadesTxt.text = loserScore.spades.toString()
        binding.loserScoreLayout.countHeartsTxt.text = loserScore.hearts.toString()
        binding.loserScoreLayout.countClubsTxt.text = loserScore.clubs.toString()
        binding.loserScoreLayout.countDiamondsTxt.text = loserScore.diamonds.toString()

        CommonHelper.INSTANCE.setScaleOnTouch(binding.backToLobbyBtnCons) {
            findNavController().popBackStack()
            findNavController().popBackStack()
        }

        CommonHelper.INSTANCE.setScaleOnTouch(binding.newGameBtnCons) {
            // Send a signal to the parent fragment
            parentFragmentManager.setFragmentResult(
                "game_result_action",
                bundleOf("action" to "new_game")
            )

            // Close the dialog
            findNavController().popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_WINNER_LIST_JSON = "winner_list_json"
        const val KEY_LOSER_LIST_JSON = "loser_list_json"
        const val KEY_WINNER_SCORE_JSON = "winner_score_json"
        const val KEY_LOSER_SCORE_JSON = "loser_score_json"
        const val KEY_RESULT_ACTION = "game_result_action"

    }

}
