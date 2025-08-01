package com.keylogic.mindi.helper

import android.content.Context
import com.keylogic.mindi.database.MyPreferences
import com.keylogic.mindi.models.DailyReward
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DailyRewardHelper {
    companion object {
        val INSTANCE = DailyRewardHelper()

        var dailyRewardList = ArrayList<DailyReward>()

        var currDay = 1
        var lastSavedRewardDay = ""
        var isDailyRewardCollected = false
    }

    fun isMoreThanOneDaySince(): Boolean {
        val currentTimeMillis = getCurrentUtcDay().time

        if (lastSavedRewardDay.isEmpty())
            return true

        val lastSavedDate = parseUtcDateStringToDate(lastSavedRewardDay)
        val lastSavedMillis = lastSavedDate.time

        val diffInMillis = currentTimeMillis - lastSavedMillis

        return diffInMillis > 24 * 60 * 60 * 1000
    }


    fun setNextDayReward(context: Context) {
        for (reward in dailyRewardList) {
            if (reward.dayCount == currDay) {
                reward.isCollected = true
                break
            }
        }
        currDay++
        if (currDay > dailyRewardList.size)
            currDay = 1
        lastSavedRewardDay = getDayAsString(getCurrentUtcDay())
        MyPreferences.INSTANCE.saveDailyReward(context)
    }

    fun getCurrentUtcDay(): Date {
        val utcTimeZone = TimeZone.getTimeZone("UTC")

        val calendar = Calendar.getInstance(utcTimeZone)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.time
    }

    fun getDayAsString(date: Date?): String {
        if (date == null)
            return ""
        val utcTimeZone = TimeZone.getTimeZone("UTC")

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        sdf.timeZone = utcTimeZone
        return sdf.format(date)
    }

    fun parseUtcDateStringToDate(dateString: String): Date {
        val utcTimeZone = TimeZone.getTimeZone("UTC")

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        sdf.timeZone = utcTimeZone
        return sdf.parse(dateString)!!
    }

    fun isNextUtcDay(currentDate: Date, lastSavedDate: Date): Boolean {
        val utcTimeZone = TimeZone.getTimeZone("UTC")

        val calendarCurrent = Calendar.getInstance(utcTimeZone).apply {
            time = currentDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val calendarLastSaved = Calendar.getInstance(utcTimeZone).apply {
            time = lastSavedDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        calendarLastSaved.add(Calendar.DAY_OF_YEAR, 1)

        return calendarCurrent.time == calendarLastSaved.time
    }

    fun generateDailyRewards() {
        dailyRewardList.clear()

        dailyRewardList.add(DailyReward(
            dayCount = 1,
            chipCount = 1_000L,
            isCurrentDay = false,
            isCollected = false
        ))

        dailyRewardList.add(DailyReward(
            dayCount = 2,
            chipCount = 1_500L,
            isCurrentDay = false,
            isCollected = false
        ))

        dailyRewardList.add(DailyReward(
            dayCount = 3,
            chipCount = 2_000L,
            isCurrentDay = false,
            isCollected = false
        ))

        dailyRewardList.add(DailyReward(
            dayCount = 4,
            chipCount = 2_500L,
            isCurrentDay = false,
            isCollected = false
        ))

        dailyRewardList.add(DailyReward(
            dayCount = 5,
            chipCount = 3_000L,
            isCurrentDay = false,
            isCollected = false
        ))
    }

    fun updateTodayReward() {
        fun resetDailyRewards() {
            currDay = 1
            isDailyRewardCollected = false
            dailyRewardList[0].isCurrentDay = true
        }

        fun updateCollections() {
            for (reward in dailyRewardList) {
                reward.isCurrentDay = currDay == reward.dayCount
                if (reward.dayCount < currDay)
                    reward.isCollected = true
                println("${reward.dayCount} = ${reward.isCollected} | ${reward.isCurrentDay}")
            }
        }

        //when app in 1st open
        if (lastSavedRewardDay.isEmpty()) {
            resetDailyRewards()
            return
        }
        //when app more then 1 day skip
        val isAnyDaySkip = isMoreThanOneDaySince()
        if (isAnyDaySkip) {
            resetDailyRewards()
            updateCollections()
            return
        }

        //regular update
        val lastSavedDay = parseUtcDateStringToDate(lastSavedRewardDay)
        val isNextDay = isNextUtcDay(getCurrentUtcDay(), lastSavedDay)
        isDailyRewardCollected = !isNextDay
        updateCollections()
    }

}