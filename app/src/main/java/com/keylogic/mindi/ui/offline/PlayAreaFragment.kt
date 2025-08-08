package com.keylogic.mindi.ui.offline

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.keylogic.mindi.R
import com.keylogic.mindi.databinding.FragmentPlayAreaBinding
import com.keylogic.mindi.databinding.LeftHandsIndicatorLayoutBinding
import com.keylogic.mindi.databinding.RightHandsIndicatorLayoutBinding
import com.keylogic.mindi.dialogs.GameResultFragment
import com.keylogic.mindi.enums.DeviceType
import com.keylogic.mindi.enums.SuitType
import com.keylogic.mindi.gamePlay.animation.ChipAnimation
import com.keylogic.mindi.gamePlay.animation.DistributeCards
import com.keylogic.mindi.gamePlay.animation.EnterPlayerCard
import com.keylogic.mindi.gamePlay.animation.MindiCollect
import com.keylogic.mindi.gamePlay.animation.OpenTrumpCard
import com.keylogic.mindi.gamePlay.animation.TeamDeclaration
import com.keylogic.mindi.gamePlay.helper.*
import com.keylogic.mindi.gamePlay.models.Card
import com.keylogic.mindi.gamePlay.models.CardHider
import com.keylogic.mindi.gamePlay.models.CardView
import com.keylogic.mindi.gamePlay.models.Score
import com.keylogic.mindi.gamePlay.models.TableConfig
import com.keylogic.mindi.gamePlay.models.TrumpCard
import com.keylogic.mindi.helper.*
import com.keylogic.mindi.interfaces.GameCycleCallback
import com.keylogic.mindi.models.ResultProfile
import com.keylogic.mindi.ui.viewModel.CenterCardsViewModel
import com.keylogic.mindi.ui.viewModel.GameCycleViewModel
import com.keylogic.mindi.ui.viewModel.PlayAreaConfigViewModel
import com.keylogic.mindi.ui.viewModel.PlayerCardsViewModel
import com.keylogic.mindi.ui.viewModel.TableConfigViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.set
import kotlin.math.max


class PlayAreaFragment : Fragment() {

    private var _binding: FragmentPlayAreaBinding? = null
    private val binding get() = _binding!!

    private val args: PlayAreaFragmentArgs by navArgs()
    private lateinit var tableConfig: TableConfig
    private lateinit var trumpCardView: CardView

    private lateinit var playAreaViewModel: PlayAreaConfigViewModel
    private lateinit var tableConfigViewModel: TableConfigViewModel
    private lateinit var centerCardsViewModel: CenterCardsViewModel
    private lateinit var playerCardsViewModel: PlayerCardsViewModel
    private lateinit var gameCycleViewModel: GameCycleViewModel

    // Helpers
    private lateinit var playerDesignHelper: PlayerDesignHelper
    private lateinit var teamDeclaration: TeamDeclaration
    private lateinit var centerCardHelper: CenterCardHelper
    private lateinit var distributeCards: DistributeCards
    private lateinit var chipAnimation: ChipAnimation
    private lateinit var playerCardViewHelper: PlayerCardViewHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPlayAreaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        initHelpers()
        setupUI()
        observeViewModels()
    }

    private fun initViewModels() {
        tableConfig = args.tableConfig

        val activity = requireActivity()
        playAreaViewModel = ViewModelProvider(activity)[PlayAreaConfigViewModel::class.java]
        tableConfigViewModel = ViewModelProvider(activity)[TableConfigViewModel::class.java]
        centerCardsViewModel = ViewModelProvider(activity)[CenterCardsViewModel::class.java]
        playerCardsViewModel = ViewModelProvider(activity)[PlayerCardsViewModel::class.java]
        gameCycleViewModel = ViewModelProvider(activity)[GameCycleViewModel::class.java]

        playAreaViewModel.resetScores()
        GameHelper.tableConfig = tableConfig
    }

    private fun initHelpers() {
        val layout = binding.includeLayouts
        playerDesignHelper = PlayerDesignHelper(layout)
        teamDeclaration = TeamDeclaration(layout)
        centerCardHelper = CenterCardHelper(tableConfig.totalPlayers, layout)
        distributeCards = DistributeCards(layout)
        chipAnimation = ChipAnimation()
        playerCardViewHelper = PlayerCardViewHelper(layout)
    }

    private fun setupUI() {
        binding.tableImg.setImageResource(ProfileHelper.INSTANCE.getTableResource(requireContext()))
        binding.tableInfoImg.setImageResource(
            CommonHelper.INSTANCE.getResourceByName(
                requireContext(),
                "deck_${tableConfig.deckType.deckCount}" + if (tableConfig.isHideMode) "_hide" else "_katte"
            )
        )
        CommonHelper.INSTANCE.setScaleOnTouch(binding.cancelCons) {
            if (findNavController().currentDestination?.id == R.id.playAreaFragment)
                findNavController().navigate(R.id.gameOptionFragment)
        }

        isHandCountViewVisible(false)
        generatePlayerList()
        playerDesignHelper.designWaitingPlayer(GameHelper.playerDetailsList)
        centerCardHelper.designCenterCards()

        startEntryAnimation()
    }

    private fun observeViewModels() {
        playAreaViewModel.leftScore.observe(viewLifecycleOwner) {
            GameHelper.greenTeamScore = it
            updateScoreUI(binding.includeLayouts.leftHandLayout, it, isLeft = true)
        }
        playAreaViewModel.rightScore.observe(viewLifecycleOwner) {
            GameHelper.redTeamScore = it
            updateScoreUI(binding.includeLayouts.rightHandLayout, it, isLeft = false)
        }
        playAreaViewModel.betPrice.observe(viewLifecycleOwner) {
            binding.betPriceCons.setViewTextAndResource(CommonHelper.INSTANCE.formatChip(it), R.drawable.ic_chip)
        }
        playAreaViewModel.trumpCard.observe(viewLifecycleOwner) { card ->
            designTrumpCardView(card)
        }
        playAreaViewModel.mindiCount.observe(viewLifecycleOwner) { pair ->
            MindiCollect.INSTANCE.showMindiAnimation(requireContext(),
                tableConfig.totalPlayers,
                binding.includeLayouts.mindiCountImg,
                pair, onAnimationEnd = {
                    if (pair != null && pair.first != 0) {
                        val highestPlayer = GameHelper.getHighestCardPlayer()
                        if (isGameContinue()) {
                            GameHelper.roundComplete(highestPlayer)
                            gameCycleViewModel.startCountdown()
                        }
                        else
                            gameWinnerFound()
                    }
                })
        }
        playAreaViewModel.message.observe(viewLifecycleOwner) { msg ->
            binding.includeLayouts.messageTxt.text = msg
            binding.includeLayouts.clickRelative.visibility = if (msg.isNotEmpty()) View.VISIBLE else View.GONE
        }
        centerCardsViewModel.centerCards.observe(viewLifecycleOwner) { pair ->
            pair?.let { centerCardHelper.updateCenterCard(it.first, it.second) }
        }
        playerCardsViewModel.playerCards.observe(viewLifecycleOwner) {
            updateCardViews(it)
        }
    }

    private fun generatePlayerList() {
        GameHelper.playerDetailsList.clear()
        val totalPlayers = tableConfig.totalPlayers
        val deckType = tableConfig.deckType
        val cards = CardGenerator.INSTANCE.generateCard(deckType, totalPlayers)

        for (index in 0 until totalPlayers) {
            val player = if (index == 0) ProfileHelper.INSTANCE.getUserProfileDetails() else BotHelper.INSTANCE.generateBotPlayerProfile(index)
            player.playerGameDetails.apply {
                assignCards(cards[index])
                isTrumpCardExist = true
            }
            GameHelper.playerDetailsList.add(player)
        }
    }

    private fun startEntryAnimation() {
        viewLifecycleOwner.lifecycleScope.launch {
            if (!isAdded || view == null) return@launch
            playAreaViewModel.updateMessage(getString(R.string.m_wait_for_all_players))
            for (i in 1 until GameHelper.playerDetailsList.size) {
                delay(500L)
                if (!isAdded || view == null) return@launch
                playerDesignHelper.updateWaitingProfile(i, GameHelper.playerDetailsList[i])
            }

            playAreaViewModel.updateMessage(getString(R.string.m_collecting_bet_price))
            val chipX = DisplayHelper.screenWidth - requireContext().resources.getDimensionPixelSize(
                if (CommonHelper.deviceType == DeviceType.NORMAL) com.intuit.sdp.R.dimen._112sdp else com.intuit.sdp.R.dimen._102sdp
            ) / 2f

            val chipY = requireContext().resources.getDimensionPixelSize(
                if (CommonHelper.deviceType == DeviceType.NORMAL) com.intuit.sdp.R.dimen._22sdp else com.intuit.sdp.R.dimen._24sdp
            ) / 2f

            chipAnimation.collectAllPlayersChips(
                binding.includeLayouts.animationRelative,
                tableConfig.totalPlayers,
                chipX, chipY
            ) {
                val totalBet = tableConfig.totalPlayers * tableConfig.betPrice
                playAreaViewModel.updateBetPrice(totalBet)
                playAreaViewModel.updateMessage(getString(R.string.m_team_declaration))
                declareTeams()
            }
        }
    }

    private fun declareTeams() {
        val teamCards = CardGenerator.INSTANCE.getRandom24Cards()
        teamDeclaration.declareTeam(tableConfig.totalPlayers, teamCards) { teamA, teamB ->
            if (!isAdded || view == null) return@declareTeam
            viewLifecycleOwner.lifecycleScope.launch {
                delay(1_000L)
                if (!isAdded || view == null) return@launch
                binding.includeLayouts.animationRelative.removeAllViews()
                binding.includeLayouts.animationRelative.visibility = View.GONE

                val newList = teamA.zip(teamB).flatMap { (a, b) ->
                    listOf(GameHelper.playerDetailsList[a], GameHelper.playerDetailsList[b])
                }
                val firstTurnPlayer = newList[0]
                CommonHelper.print("firstPlayerName ==> ${firstTurnPlayer.name}")

                val currentIndex = newList.indexOfFirst {
                    it.uId == ProfileHelper.profileUID && it.name == ProfileHelper.profileName
                }

                val orderedPlayers = newList.subList(currentIndex, newList.size) + newList.subList(0, currentIndex)
                GameHelper.playerDetailsList.clear()
                GameHelper.playerDetailsList.addAll(orderedPlayers)

                playerDesignHelper.designPlayer(GameHelper.playerDetailsList)
                GameHelper.playerDetailsList.forEachIndexed { i, player ->
                    player.centerCardIndex = i
                    player.isMyTeammate = i % 2
                }
                //re-arrange as per 1st turn
                GameHelper.reOrderPlayerList(firstTurnPlayer.uId)

                GameHelper.currTurnPlayerUID = firstTurnPlayer.uId
                GameHelper.firstPlayerUID = firstTurnPlayer.uId
                CommonHelper.print("list firstPlayerName ==> ${GameHelper.playerDetailsList[0].name}")

                playAreaViewModel.updateMessage(getString(R.string.m_distributing_cards))
                distributeCards()
            }
        }
    }

    private fun distributeCards() {
        distributeCards.distributeCards { hiddenCard ->
            if (hiddenCard.suit != SuitType.NONE) {
                GameHelper.cardHider = CardHider(hiddenCard, GameHelper.currTurnPlayerUID)
            }

            val firstPlayerName = GameHelper.playerDetailsList[0].name
            playAreaViewModel.updateMessage(getString(R.string.m_first_turn, firstPlayerName))
            isHandCountViewVisible(true)

            playerCardViewHelper.designPlayerCardView()
            playerCardsViewModel.initializeList()

            viewLifecycleOwner.lifecycleScope.launch {
                delay(500L)
                if (!isAdded || view == null) return@launch
                GameHelper.playerDetailsList.forEach { it.playerGameDetails.sortCards() }
                playerCardsViewModel.updateCards(GameHelper.getCurrentPlayer().playerGameDetails.cardList)
                delay(1_000L)
                if (!isAdded || view == null) return@launch
                playAreaViewModel.updateMessage("")
                startGame()
            }
        }
    }

    private fun startGame() {
        gameCycleViewModel.initializeTotalPlayers(tableConfig.totalPlayers)
        val userProfileView = playerDesignHelper.playerProfileList[0]
        playerCardViewHelper.setCardViewClickEvents(userProfileView) { card ->
            setUpTrumpCardOfCurrRoundSuit(card)
            stopAllTimer()

            EnterPlayerCard.INSTANCE.enterPlayerCard(card, centerCardHelper, playerCardViewHelper, onAnimationComplete = {
                playerCardsViewModel.removeCard(card)
                val centerIndex = GameHelper.getCurrentPlayer().centerCardIndex
                centerCardsViewModel.updateCenterCards(Pair(centerIndex, card))

                GameHelper.setNextPlayerTurn()
                gameCycleViewModel.startCountdown()
            })
        }

        gameCycleViewModel.initializeCallback(callback = object : GameCycleCallback {
            override fun onPlayerStart(playerIndex: Int) {
                if (tableConfig.isHideMode)
                    setUpHideModelTrumpCardOfCurrRoundSuit()
                playerDesignHelper.playerProfileList[playerIndex].startTimer()
            }

            override fun onPlayerTrigger(playerIndex: Int) {
                val playerDetails = GameHelper.getCurrentTurnPlayer()
                val cpuCard = CpuCard.INSTANCE.getValidCardByCPU()
                setUpTrumpCardOfCurrRoundSuit(cpuCard)
                stopAllTimer()
                EnterPlayerCard.INSTANCE.enterBotOrPlayerCard(playerIndex, cpuCard, binding.includeLayouts.animationRelative,
                    playerCardViewHelper, centerCardHelper, tableConfig.totalPlayers,
                    onAnimationComplete = {
                        if (!playerDetails.isBot)
                            playerCardsViewModel.removeCard(cpuCard)
                        else
                            playerDetails.playerGameDetails.removeCard(cpuCard)
                        centerCardsViewModel.updateCenterCards(Pair(playerIndex, cpuCard))

                        GameHelper.setNextPlayerTurn()
                        gameCycleViewModel.startCountdown()
                    })

            }

            override fun onGameCycleComplete() {
//                CommonHelper.print("onGameCycleComplete")
                gameCycleViewModel.stopCountdown()

                val highestPlayer = GameHelper.getHighestCardPlayer()
                EnterPlayerCard.INSTANCE.roundComplete(highestPlayer, tableConfig.totalPlayers,
                    binding.includeLayouts.animationRelative, centerCardHelper,
                    onAnimationStart = {
                        //reset center cards
                        for (i in 0 until tableConfig.totalPlayers) {
                            centerCardsViewModel.updateCenterCards(Pair(i,Card()))
                        }
                    },
                    onAnimationEnd = {
                        val totalMindi = GameHelper.updateScore(highestPlayer, playAreaViewModel)
                        if (totalMindi == 0) {
                            if (isGameContinue()) {
                                GameHelper.roundComplete(highestPlayer)
                                gameCycleViewModel.startCountdown()
                            }
                            else
                                gameWinnerFound()
                        }
                        else {
                            playAreaViewModel.updateMindiCount(
                                Pair(
                                    totalMindi,
                                    highestPlayer.centerCardIndex
                                )
                            )
                        }
                    })
            }
        })
        gameCycleViewModel.startCountdown()
    }

    private fun stopAllTimer() {
        gameCycleViewModel.stopCountdown()
        playerDesignHelper.playerProfileList.forEach { it.stopTimer() }
        playerCardViewHelper.setCurrRoundCards()
    }

    private fun setUpHideModelTrumpCardOfCurrRoundSuit() {
        GameHelper.checkAndSetTrumpOrCurrRoundSuit(null, onTrumpCardDeclared = {
            OpenTrumpCard.INSTANCE.showTrumpCard(binding.includeLayouts, trumpCardView) {
                playAreaViewModel.updateTrumpCard(GameHelper.cardHider.card)
                OpenTrumpCard.INSTANCE.startScaleAnimation(trumpCardView)
            }
        })
    }

    private fun setUpTrumpCardOfCurrRoundSuit(card: Card) {
        GameHelper.checkAndSetTrumpOrCurrRoundSuit(card, onTrumpCardDeclared = {
            if (tableConfig.isHideMode) {
                OpenTrumpCard.INSTANCE.showTrumpCard(binding.includeLayouts, trumpCardView) {
                    playAreaViewModel.updateTrumpCard(card)
                    OpenTrumpCard.INSTANCE.startScaleAnimation(trumpCardView)
                }
            }
            else {
                playAreaViewModel.updateTrumpCard(card)
            }
        })
        GameHelper.updatePlayerEnteredCards(card)
    }

    private fun designTrumpCardView(card: Card) {
        if (!::trumpCardView.isInitialized) {
            val pos = PositionHelper.INSTANCE.getTrumpCardPosition(tableConfig.totalPlayers)
            trumpCardView = CardView(requireContext(), card, pos[0], pos[1])
            trumpCardView.isDefaultCard = true
            binding.includeLayouts.playAreaRelative.addView(trumpCardView)
        } else {
            trumpCardView.updateResource(card)
        }
    }

    private fun updateCardViews(cards: List<Card>) {
        val cardViewList = playerCardViewHelper.playerCardViewList
        val playArea = binding.includeLayouts.playAreaRelative
        val yPos = PositionHelper.INSTANCE.getUserCardYPosition()
        val xDistance = PositionHelper.INSTANCE.getUserCardXDistance()
        val totalWidth = (cards.size - 1) * xDistance + DisplayHelper.cardWidth
        val startX = (DisplayHelper.screenWidth - totalWidth) / 2

        val cardViewMap = cardViewList.associateBy { it.card }
        val cardsToRemove = cardViewMap.keys - cards.toSet()
        for (removedCard in cardsToRemove) {
            cardViewMap[removedCard]?.let {
                playArea.removeView(it)
                playerCardViewHelper.playerCardViewList.remove(it)
            }
        }

        cards.forEachIndexed { index, card ->
            val cardView = cardViewMap[card]
            cardView?.let {
                val newX = startX + index * xDistance
                cardView.z = index.toFloat()
                ObjectAnimator.ofFloat(it, "x", it.x, newX).apply {
                    duration = 200L
                    start()
                }
                ObjectAnimator.ofFloat(it, "y", it.y, yPos).apply {
                    duration = 200L
                    start()
                }
            }
        }
    }

    private fun isHandCountViewVisible(visible: Boolean) {
        val visibility = if (visible) View.VISIBLE else View.GONE
        binding.includeLayouts.leftHandLayout.root.visibility = visibility
        binding.includeLayouts.rightHandLayout.root.visibility = visibility

        if (visible) {
            updateScoreUI(binding.includeLayouts.leftHandLayout, Score(), true)
            updateScoreUI(binding.includeLayouts.rightHandLayout, Score(), false)
            trumpCardView.setCardBack()
        }
    }

    private fun updateScoreUI(layout: Any, score: Score, isLeft: Boolean) {
        val context = requireContext()
        val hWidth = if (CommonHelper.deviceType == DeviceType.NORMAL)
            context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._28sdp)
        else
            context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._24sdp)

        val mWidth = context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._20sdp)
        val handsHeight = if (CommonHelper.deviceType == DeviceType.NORMAL)
            context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._44sdp)
        else
            context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._40sdp)

        val playerFirstPos = PositionHelper.INSTANCE.getPlayerPosition(1, tableConfig.totalPlayers)
        val playerLastPos = PositionHelper.INSTANCE.getPlayerPosition(tableConfig.totalPlayers, tableConfig.totalPlayers)
        val player1CenterX = playerFirstPos[0] + DisplayHelper.profileWidth / 2
        val cardResource = ProfileHelper.INSTANCE.getCardResource(context)

        val rootView: View
        val suitViews: List<Triple<View, View, Int>>

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

            visibleSuits.forEach { (container, textView, count) ->
                container.visibility = View.VISIBLE
                (textView as? AppCompatTextView)?.text = count.toString()
            }

            suitViews.filter { it.third == 0 }.forEach { (container, _, _) ->
                container.visibility = View.GONE
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

    fun isGameContinue(): Boolean {
        val gameStatus = GameHelper.checkWinner(playAreaViewModel.leftScore.value!!, playAreaViewModel.rightScore.value!!)
        CommonHelper.print("Game Status -----------> $gameStatus")
        return gameStatus == -1
    }

    fun gameWinnerFound() {
        val gameStatus = GameHelper.checkWinner(playAreaViewModel.leftScore.value!!, playAreaViewModel.rightScore.value!!)
        GameHelper.resultScreen(requireContext(), gameStatus, binding.includeLayouts) {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(5_000L)
                if (!isAdded || view == null) return@launch
                showResult(gameStatus)
                delay(300L)
                if (!isAdded || view == null) return@launch
                binding.includeLayouts.winnerLayout.alpha = 0f
            }
        }
    }

    fun showResult(gameStatus: Int) {
        val bundle = GameHelper.getResultBundle(gameStatus)
        findNavController().navigate(R.id.gameResultFragment,bundle)
    }

    private fun restartGame() {
        val gameStatus = GameHelper.checkWinner(playAreaViewModel.leftScore.value!!, playAreaViewModel.rightScore.value!!)
        val lastGame1stPlayer = GameHelper.getPlayerDetails(GameHelper.firstPlayerUID)
        if (lastGame1stPlayer.isMyTeammate != gameStatus) {
            val index = GameHelper.playerDetailsList.indexOfFirst { it.uId == lastGame1stPlayer.uId }
            if (index+1 == GameHelper.playerDetailsList.size)
                GameHelper.firstPlayerUID = GameHelper.playerDetailsList[0].uId
            else
                GameHelper.firstPlayerUID = GameHelper.playerDetailsList[index+1].uId
        }

        val highestPlayer = GameHelper.getHighestCardPlayer()
        val totalPlayers = tableConfig.totalPlayers
        val deckType = tableConfig.deckType
        val cards = CardGenerator.INSTANCE.generateCard(deckType, totalPlayers)
        GameHelper.reOrderPlayerList(highestPlayer.uId)

        for ((index, player) in GameHelper.playerDetailsList.withIndex()) {
            player.playerGameDetails.apply {
                assignCards(cards[index])
                isTrumpCardExist = true
            }
        }

        reStartEntryAnimation()
    }

    private fun reStartEntryAnimation() {
        viewLifecycleOwner.lifecycleScope.launch {
            if (!isAdded || view == null) return@launch
            playAreaViewModel.updateMessage(getString(R.string.m_collecting_bet_price))
            val chipX = DisplayHelper.screenWidth - requireContext().resources.getDimensionPixelSize(
                if (CommonHelper.deviceType == DeviceType.NORMAL) com.intuit.sdp.R.dimen._112sdp else com.intuit.sdp.R.dimen._102sdp
            ) / 2f

            val chipY = requireContext().resources.getDimensionPixelSize(
                if (CommonHelper.deviceType == DeviceType.NORMAL) com.intuit.sdp.R.dimen._22sdp else com.intuit.sdp.R.dimen._24sdp
            ) / 2f

            chipAnimation.collectAllPlayersChips(
                binding.includeLayouts.animationRelative,
                tableConfig.totalPlayers,
                chipX, chipY
            ) {
                val totalBet = tableConfig.totalPlayers * tableConfig.betPrice
                playAreaViewModel.updateBetPrice(totalBet)

                GameHelper.currTurnPlayerUID = GameHelper.firstPlayerUID
                CommonHelper.print("list firstPlayerName ==> ${GameHelper.playerDetailsList[0].name}")

                playAreaViewModel.updateMessage(getString(R.string.m_distributing_cards))
                distributeCards()
            }
        }
    }

    override fun onDestroyView() {
        gameCycleViewModel.stopCountdown()
        viewLifecycleOwner.lifecycleScope.coroutineContext.cancelChildren()
        _binding = null
        super.onDestroyView()
    }
}
