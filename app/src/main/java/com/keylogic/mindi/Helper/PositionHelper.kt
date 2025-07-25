package com.keylogic.mindi.Helper

import com.keylogic.mindi.Enum.DeviceType
import com.keylogic.mindi.Helper.DisplayHelper.Companion.cardWidth
import com.keylogic.mindi.Helper.DisplayHelper.Companion.profileWidth
import com.keylogic.mindi.Helper.DisplayHelper.Companion.profileHeight
import com.keylogic.mindi.Helper.DisplayHelper.Companion.screenHeight
import com.keylogic.mindi.Helper.DisplayHelper.Companion.screenWidth
import com.keylogic.mindi.Helper.DisplayHelper.Companion.totalProfileDivision

class PositionHelper {
    companion object {
        val INSTANCE = PositionHelper()
    }

    fun getTeamPosition(index: Int, totalPlayers: Int): FloatArray {
        val arr = FloatArray(2)
        val halfSWidth = screenWidth / 2f
        val playerInSingleTeam = totalPlayers / 2

        val pvWidth = profileWidth * 1.5f
        val pvHeight = profileHeight * 1.25f

        val startSHeight = (screenHeight - (playerInSingleTeam * pvHeight)) / 2f

        val isLeftSide = index % 2 == 0
        val row = index / 2  // same row for left-right pairs

        arr[0] = if (isLeftSide) halfSWidth - pvWidth else halfSWidth + pvWidth
        arr[1] = startSHeight + row * pvHeight

        return arr
    }

    fun getPlayerPosition(index: Int, totalPlayers: Int): FloatArray {
        val halfWidth = (screenWidth - profileWidth) / 2f
        val halfSWidth = screenWidth / 2f
        val halfSHeight = screenHeight / 2f
        val startYPos = (screenHeight - profileHeight * totalProfileDivision) / 2f

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
                        val yPos = halfSHeight - profileHeight * if (CommonHelper.deviceType == DeviceType.NORMAL) 2.35f else 2.75f
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
                        val yPos = halfSHeight - profileHeight * if (CommonHelper.deviceType == DeviceType.NORMAL) 2.35f else 2.75f
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
                        val yPos = halfSHeight - profileHeight * if (CommonHelper.deviceType == DeviceType.NORMAL) 2.35f else 2.75f
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

    fun getPlayerCardPosition(index: Int, totalPlayers: Int): FloatArray {
        val xStart = 150f
        val xEnd = screenWidth - xStart * 1.5f
        val yStart = 10f
        val yEnd = screenHeight - profileWidth.toFloat()
        val halfWidth = (screenWidth - profileWidth) / 2f
        val halfHeight = (screenHeight - profileHeight) / 2f

        val pos = FloatArray(2)
        val xDistance = cardWidth * 1.25f
        val yDistance = cardWidth
        when(totalPlayers) {
            4 -> {
                when(index) {
                    1 -> {
                        pos[0] = halfWidth
                        pos[1] = yEnd - yDistance
                    }
                    2 -> {
                        pos[0] = xStart + xDistance
                        pos[1] = halfHeight
                    }
                    3 -> {
                        pos[0] = halfWidth
                        pos[1] = yStart + yDistance
                    }
                    4 -> {
                        pos[0] = xEnd - xDistance
                        pos[1] = halfHeight
                    }
                }
            }
            6 -> {
                when(index) {
                    1 -> {
                        pos[0] = halfWidth
                        pos[1] = yEnd - yDistance
                    }
                    2 -> {
                        pos[0] = xStart + xDistance
                        pos[1] = halfHeight
                    }
                    3 -> {
                        val distance = (xStart * 2.75f)
                        pos[0] = distance
                        pos[1] = halfHeight - pos[0] + yDistance
                    }
                    4 -> {
                        pos[0] = halfWidth
                        pos[1] = yStart + yDistance
                    }
                    5 -> {
                        val distance = (xStart * 2.75f)
                        pos[0] = xEnd - distance
                        pos[1] = halfHeight - distance + yDistance
                    }
                    6 -> {
                        pos[0] = xEnd - xDistance
                        pos[1] = halfHeight
                    }
                }
            }
            8 -> {
                when(index) {
                    1 -> {
                        pos[0] = halfWidth
                        pos[1] = yEnd - xDistance
                    }
                    2 -> {
                        pos[0] = xStart + xDistance
                        pos[1] = halfHeight
                    }
                    3 -> {
                        val distance = (xStart * 2.25f)
                        pos[0] = distance + xDistance * 0.75f
                        pos[1] = halfHeight - distance + yDistance / 2f
                    }
                    4 -> {
                        val distance = (xStart * 2.5f)
                        pos[0] = halfWidth - distance
                        pos[1] = yStart + yDistance
                    }
                    5 -> {
                        pos[0] = halfWidth
                        pos[1] = yStart + yDistance
                    }
                    6 -> {
                        val distance = (xStart * 2.5f)
                        pos[0] = halfWidth + distance
                        pos[1] = yStart + yDistance
                    }
                    7 -> {
                        val xDis = (xStart * 1.5f)
                        val yDis = (xStart * 2.25f)
                        pos[0] = xEnd - xDis - xDistance * 0.8f
                        pos[1] = halfHeight - yDis + yDistance / 2f
                    }
                    8 -> {
                        pos[0] = xEnd - xDistance
                        pos[1] = halfHeight
                    }
                }
            }
        }
        return pos
    }

}