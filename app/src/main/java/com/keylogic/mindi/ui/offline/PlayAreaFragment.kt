package com.keylogic.mindi.ui.offline

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.keylogic.mindi.databinding.FragmentPlayAreaBinding
import com.keylogic.mindi.databinding.LeftHandsIndicatorLayoutBinding
import com.keylogic.mindi.databinding.RightHandsIndicatorLayoutBinding
import com.keylogic.mindi.enums.DeviceType
import com.keylogic.mindi.gamePlay.animation.TeamDeclaration
import com.keylogic.mindi.gamePlay.helper.*
import com.keylogic.mindi.gamePlay.models.PlayerDetails
import com.keylogic.mindi.gamePlay.models.Score
import com.keylogic.mindi.gamePlay.models.TableConfig
import com.keylogic.mindi.helper.*
import com.keylogic.mindi.ui.viewModel.ScoreConfigViewModel
import com.keylogic.mindi.ui.viewModel.TableConfigViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max

class PlayAreaFragment : Fragment() {

    private var _binding: FragmentPlayAreaBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TableConfigViewModel
    private val args: PlayAreaFragmentArgs by navArgs()

    companion object {
        private lateinit var scoreViewModel: ScoreConfigViewModel
        private lateinit var tableConfig: TableConfig
        private lateinit var playerDesignHelper: PlayerDesignHelper
        private lateinit var teamDeclaration: TeamDeclaration
        val playerDetailsList = ArrayList<PlayerDetails>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayAreaBinding.inflate(inflater, container, false)
        initViewModels()
        setupTableInfo()
        setupNavigation()
        setupPlayersAndCards()
        observeScores()

        return binding.root
    }

    private fun initViewModels() {
        viewModel = ViewModelProvider(requireActivity())[TableConfigViewModel::class.java]
        scoreViewModel = ViewModelProvider(requireActivity())[ScoreConfigViewModel::class.java]
        scoreViewModel.resetScores()
        tableConfig = args.tableConfig
        playerDesignHelper = PlayerDesignHelper(binding.includeLayouts)
        teamDeclaration = TeamDeclaration(binding.includeLayouts)
    }

    private fun setupTableInfo() {
        val tableInfoName = "deck_${tableConfig.deckType.deckCount}" +
                if (tableConfig.isHideMode) "_hide" else "_katte"
        val resource = CommonHelper.INSTANCE.getResourceByName(requireContext(), tableInfoName)
        binding.tableInfoImg.setImageResource(resource)
    }

    private fun setupNavigation() {
        CommonHelper.INSTANCE.setScaleOnTouch(binding.cancelCons) {
            findNavController().popBackStack()
        }
    }

    private fun setupPlayersAndCards() {
        generatePlayerAndCards()
        playerDesignHelper.designWaitingPlayer(playerDetailsList)

        lifecycleScope.launch {
            for (i in 1 until playerDetailsList.size) {
//                delay(500L)
                playerDesignHelper.updateWaitingProfile(i, playerDetailsList[i])
            }
            teamDeclaration()
        }

        updateScoreUI(binding.leftHandLayout, Score(), isLeft = true)
        updateScoreUI(binding.rightHandLayout, Score(), isLeft = false)
    }

    private fun generatePlayerAndCards() {
        playerDetailsList.clear()
        for (i in 0 until tableConfig.totalPlayers) {
            val player = if (i == 0)
                ProfileHelper.INSTANCE.getUserProfileDetails()
            else
                BotHelper.INSTANCE.generateBotPlayerProfile(i)

            player.apply {
                isRedTeamMember = i % 2 != 0
                playerGameDetails.originalPlayerIndex = i
                playerGameDetails.centerCardViewIndex = i
                playerGameDetails.cardList = CardGenerator.INSTANCE.generateCard(
                    tableConfig.deckType, tableConfig.totalPlayers
                )[i]
                playerGameDetails.isTrumpCardExist = true
                playerGameDetails.isCardHider = false
            }

            playerDetailsList.add(player)
        }
    }

    private fun teamDeclaration() {
        val teamDeclarationCardList = CardGenerator.INSTANCE.getRandom24Cards()
        teamDeclaration.declareTeam(tableConfig.totalPlayers, teamDeclarationCardList)
    }

    private fun observeScores() {
        scoreViewModel.leftScore.observe(viewLifecycleOwner) {
            updateScoreUI(binding.leftHandLayout, it, isLeft = true)
        }
        scoreViewModel.rightScore.observe(viewLifecycleOwner) {
            updateScoreUI(binding.rightHandLayout, it, isLeft = false)
        }
    }

    private fun updateScoreUI(layout: Any, score: Score, isLeft: Boolean) {
        val hWidth = if (CommonHelper.deviceType == DeviceType.NORMAL)
            requireContext().resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._28sdp)
        else
            requireContext().resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._24sdp)

        val mWidth = requireContext().resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._20sdp)
        val handsHeight = if (CommonHelper.deviceType == DeviceType.NORMAL)
            requireContext().resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._44sdp)
        else
            requireContext().resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._40sdp)

        val playerFirstPos = PositionHelper.INSTANCE.getPlayerPosition(1, tableConfig.totalPlayers)
        val playerLastPos = PositionHelper.INSTANCE.getPlayerPosition(tableConfig.totalPlayers, tableConfig.totalPlayers)
        val player1CenterX = playerFirstPos[0] + DisplayHelper.profileWidth / 2

        val rootView: View
        val suitViews: List<Triple<View, View, Int>>
        val cardResource = ProfileHelper.INSTANCE.getCardResource(requireContext())

        when (layout) {
            is LeftHandsIndicatorLayoutBinding -> {
                rootView = layout.root
                layout.handTxt.text = score.hands.toString()
                layout.handCardImg.setImageResource(cardResource)
                suitViews = listOf(
                    Triple(layout.spadesLinear, layout.spadesTxt, score.spades),
                    Triple(layout.heartsLinear, layout.heartsTxt, score.hearts),
                    Triple(layout.clubsLinear, layout.clubsTxt, score.clubs),
                    Triple(layout.diamondsLinear, layout.diamondsTxt, score.diamonds)
                )
            }

            is RightHandsIndicatorLayoutBinding -> {
                rootView = layout.root
                layout.handTxt.text = score.hands.toString()
                layout.handCardImg.setImageResource(cardResource)
                suitViews = listOf(
                    Triple(layout.spadesLinear, layout.spadesTxt, score.spades),
                    Triple(layout.heartsLinear, layout.heartsTxt, score.hearts),
                    Triple(layout.clubsLinear, layout.clubsTxt, score.clubs),
                    Triple(layout.diamondsLinear, layout.diamondsTxt, score.diamonds)
                )
            }

            else -> return
        }

        rootView.y = playerFirstPos[1] - handsHeight

        rootView.post {
            val visibleSuits = suitViews.filter { it.third > 0 }

            visibleSuits.forEach { (linear, txt, value) ->
                linear.visibility = View.VISIBLE
                (txt as? AppCompatTextView)?.text = value.toString()
            }

            suitViews.filter { it.third == 0 }.forEach { (linear, _, _) ->
                linear.visibility = View.GONE
            }

            val totalWidth = hWidth + visibleSuits.size * mWidth

            rootView.x = if (isLeft) {
                val desiredX = player1CenterX - totalWidth / 2
                max(playerFirstPos[0], desiredX)
            } else {
                val playerLastCenterX = playerLastPos[0] + DisplayHelper.profileWidth / 2
                val desiredX = playerLastCenterX - totalWidth / 2
                val maxRightX = playerLastPos[0] + DisplayHelper.profileWidth
                desiredX.coerceAtMost(maxRightX - totalWidth)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
