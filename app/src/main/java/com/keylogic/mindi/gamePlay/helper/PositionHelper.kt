package com.keylogic.mindi.gamePlay.helper

import com.keylogic.mindi.enums.DeviceType
import com.keylogic.mindi.gamePlay.helper.DisplayHelper.Companion.cardHeight
import com.keylogic.mindi.gamePlay.helper.DisplayHelper.Companion.cardWidth
import com.keylogic.mindi.gamePlay.helper.DisplayHelper.Companion.profileWidth
import com.keylogic.mindi.gamePlay.helper.DisplayHelper.Companion.profileHeight
import com.keylogic.mindi.gamePlay.helper.DisplayHelper.Companion.screenHeight
import com.keylogic.mindi.gamePlay.helper.DisplayHelper.Companion.screenWidth
import com.keylogic.mindi.helper.CommonHelper

class PositionHelper {
    companion object {
        val INSTANCE = PositionHelper()
    }

    fun getUserCardXDistance(): Float {
        return cardWidth * 0.6f
    }

    fun getUserCardYPosition(): Float {
        return screenHeight - cardHeight + 0f
    }

    fun getTrumpCardPosition(totalPlayers: Int): FloatArray {
        val arr = FloatArray(2)

        val lastPlayerPos = getPlayerPosition(totalPlayers, totalPlayers)
        val firstPlayerPos = getPlayerPosition(1, totalPlayers)
        val xPos = lastPlayerPos[0] + (profileWidth - cardWidth) / 2f
            .coerceAtMost(screenWidth - cardWidth * 1.25f)
        val yPos = firstPlayerPos[1] + (profileHeight - cardHeight) / 2f
            .coerceAtMost(screenHeight - cardHeight * 1.25f)
        arr[0] = xPos
        arr[1] = yPos
        return arr
    }

    fun getPlayerPosition(index: Int, totalPlayers: Int): FloatArray {
        val halfSWidth = screenWidth / 2f
        val halfSHeight = screenHeight / 2f

        val pos = FloatArray(2)
        val commonDistance = 10f
        val commonDistance1 = profileWidth / 4f
        val thirdAndSevenW = profileWidth * 0.85f
        val thirdAndSevenH = profileHeight * 1.45f
        when(totalPlayers) {
            4 -> {
                when(index) {
                    1 -> {
                        val xPos = halfSWidth - profileWidth * 4.75f
                        pos[0] = xPos.coerceAtLeast(commonDistance)
                        pos[1] = screenHeight - profileHeight * 1.15f//halfSHeight + profileHeight * 1.25f
                    }
                    2 -> {
                        val xPos = halfSWidth - profileWidth * 4.75f
                        pos[0] = xPos.coerceAtLeast(commonDistance1)
                        pos[1] = halfSHeight - profileHeight / 2
                    }
                    3 -> {
                        pos[0] = halfSWidth - profileWidth / 2
                        val yPos = halfSHeight - profileHeight * if (CommonHelper.Companion.deviceType == DeviceType.NORMAL) 2.35f else 2.75f
                        pos[1] = yPos.coerceAtLeast(commonDistance)

                    }
                    4 -> {
                        val xPos = halfSWidth + profileWidth * 4
                        pos[0] = xPos.coerceAtMost(screenWidth - profileWidth - commonDistance1)
                        pos[1] = halfSHeight - profileHeight / 2
                    }
                }
            }
            6 -> {
                when(index) {
                    1 -> {
                        val xPos = halfSWidth - profileWidth * 4.75f
                        pos[0] = xPos.coerceAtLeast(commonDistance)
                        pos[1] = screenHeight - profileHeight * 1.15f//halfSHeight + profileHeight * 1.25f
                    }
                    2 -> {
                        val xPos = halfSWidth - profileWidth * 4.75f
                        pos[0] = xPos.coerceAtLeast(commonDistance1)
                        pos[1] = halfSHeight - profileHeight / 2
                    }
                    3 -> {
                        val twoPos = getPlayerPosition(2,totalPlayers)[0]
                        val fourPos = getPlayerPosition(4,totalPlayers)
                        pos[0] = twoPos - profileWidth / 2 + (fourPos[0] - twoPos) / 2
                        pos[1] = fourPos[1]
                    }
                    4 -> {
                        pos[0] = halfSWidth - profileWidth / 2
                        val yPos = halfSHeight - profileHeight * if (CommonHelper.Companion.deviceType == DeviceType.NORMAL) 2.35f else 2.75f
                        pos[1] = yPos.coerceAtLeast(commonDistance)

                    }
                    5 -> {
                        val fourPos = getPlayerPosition(4,totalPlayers)
                        val sixPos = getPlayerPosition(6,totalPlayers)[0]
                        pos[0] = fourPos[0] + profileWidth / 2 + (sixPos - fourPos[0]) / 2
                        pos[1] = fourPos[1]
                    }
                    6 -> {
                        val xPos = halfSWidth + profileWidth * 4
                        pos[0] = xPos.coerceAtMost(screenWidth - profileWidth - commonDistance1)
                        pos[1] = halfSHeight - profileHeight / 2
                    }
                }
            }
            8 -> {
                when(index) {
                    1 -> {
                        val xPos = halfSWidth - profileWidth * 4.75f
                        pos[0] = xPos.coerceAtLeast(commonDistance)
                        pos[1] = screenHeight - profileHeight * 1.15f//halfSHeight + profileHeight * 1.25f
                    }
                    2 -> {
                        val xPos = halfSWidth - profileWidth * 4.75f
                        pos[0] = xPos.coerceAtLeast(commonDistance1)
                        pos[1] = halfSHeight - profileHeight / 2
                    }
                    3 -> {
                        val secondPos = getPlayerPosition(2,totalPlayers)
                        pos[0] = secondPos[0] + thirdAndSevenW
                        pos[1] = secondPos[1] - thirdAndSevenH
                    }
                    4 -> {
                        val thirdPos = getPlayerPosition(3,totalPlayers)[0]
                        val fifthPos = getPlayerPosition(5,totalPlayers)
                        pos[0] = thirdPos + (fifthPos[0] - thirdPos) / 2
                        pos[1] = fifthPos[1]
                    }
                    5 -> {
                        pos[0] = halfSWidth - profileWidth / 2
                        val yPos = halfSHeight - profileHeight * if (CommonHelper.Companion.deviceType == DeviceType.NORMAL) 2.35f else 2.75f
                        pos[1] = yPos.coerceAtLeast(commonDistance)

                    }
                    6 -> {
                        val fifthPos = getPlayerPosition(5,totalPlayers)
                        val sevenPos = getPlayerPosition(7,totalPlayers)[0]
                        pos[0] = fifthPos[0] + (sevenPos - fifthPos[0]) / 2
                        pos[1] = fifthPos[1]
                    }
                    7 -> {
                        val eightPos = getPlayerPosition(8,totalPlayers)
                        pos[0] = eightPos[0] - thirdAndSevenW
                        pos[1] = eightPos[1] - thirdAndSevenH
                    }
                    8 -> {
                        val xPos = halfSWidth + profileWidth * 4
                        pos[0] = xPos.coerceAtMost(screenWidth - profileWidth - commonDistance1)
                        pos[1] = halfSHeight - profileHeight / 2
                    }
                }
            }
        }
        return pos
    }

    fun getPlayerEntetedCardPosition(index: Int, totalPlayers: Int): FloatArray {
        val pos = getPlayerPosition(index, totalPlayers)
        val arr = FloatArray(2)
        arr[0] = pos[0] + (profileWidth - cardWidth) / 2f
        arr[1] = pos[1] + (profileHeight - cardHeight) / 2f
        return arr
    }

    fun getPlayerCardPosition(index: Int, totalPlayers: Int): FloatArray {
        val pos = FloatArray(2)
        val playerPos = getPlayerPosition(index, totalPlayers)
        when(totalPlayers) {
            4 -> {
                when(index) {
                    1 -> {
                        pos[0] = (screenWidth - cardWidth) / 2f
                        pos[1] = playerPos[1]
                    }
                    2 -> {
                        pos[0] = playerPos[0] + profileWidth + cardWidth / 4
                        pos[1] = playerPos[1] + (profileHeight - cardHeight) / 2
                    }
                    3 -> {
                        pos[0] = (screenWidth - cardWidth) / 2f
                        pos[1] = playerPos[1] + profileHeight + cardHeight / 6f
                    }
                    4 -> {
                        pos[0] = playerPos[0] - cardWidth * 1.25f
                        pos[1] = playerPos[1] + (profileHeight - cardHeight) / 2
                    }
                }
            }
            6 -> {
                when(index) {
                    1 -> {
                        pos[0] = (screenWidth - cardWidth) / 2f
                        pos[1] = playerPos[1]
                    }
                    2 -> {
                        pos[0] = playerPos[0] + profileWidth + cardWidth / 4
                        pos[1] = playerPos[1] + (profileHeight - cardHeight) / 2
                    }
                    3 -> {
                        pos[0] = playerPos[0] + (profileWidth - cardWidth) / 2
                        pos[1] = playerPos[1] + profileHeight + cardHeight / 6f
                    }
                    4 -> {
                        pos[0] = (screenWidth - cardWidth) / 2f
                        pos[1] = playerPos[1] + profileHeight + cardHeight / 6f
                    }
                    5 -> {
                        pos[0] = playerPos[0] + (profileWidth - cardWidth) / 2
                        pos[1] = playerPos[1] + profileHeight + cardHeight / 6f
                    }
                    6 -> {
                        pos[0] = playerPos[0] - cardWidth * 1.25f
                        pos[1] = playerPos[1] + (profileHeight - cardHeight) / 2
                    }
                }
            }
            8 -> {
                when(index) {
                    1 -> {
                        pos[0] = (screenWidth - cardWidth) / 2f
                        pos[1] = playerPos[1]
                    }
                    2 -> {
                        pos[0] = playerPos[0] + profileWidth + cardWidth / 4
                        pos[1] = playerPos[1] + (profileHeight - cardHeight) / 2
                    }
                    3 -> {
                        pos[0] = playerPos[0] + profileWidth + cardWidth / 4
                        pos[1] = playerPos[1] + cardHeight
                    }
                    4 -> {
                        pos[0] = playerPos[0] + (profileWidth - cardWidth) / 2
                        pos[1] = playerPos[1] + profileHeight + cardHeight / 6
                    }
                    5 -> {
                        pos[0] = (screenWidth - cardWidth) / 2f
                        pos[1] = playerPos[1] + profileHeight + cardHeight / 6
                    }
                    6 -> {
                        pos[0] = playerPos[0] + (profileWidth - cardWidth) / 2
                        pos[1] = playerPos[1] + profileHeight + cardHeight / 6
                    }
                    7 -> {
                        pos[0] = playerPos[0] - cardWidth * 1.25f
                        pos[1] = playerPos[1] + cardHeight
                    }
                    8 -> {
                        pos[0] = playerPos[0] - cardWidth * 1.25f
                        pos[1] = playerPos[1] + (profileHeight - cardHeight) / 2
                    }
                }
            }
        }
        return pos
    }

}