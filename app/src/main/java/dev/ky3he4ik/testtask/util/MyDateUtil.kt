package dev.ky3he4ik.testtask.util

import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.text.format.DateUtils
import java.sql.Date
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object MyDateUtil {
    // year, month, day
    fun today(): Triple<Int, Int, Int> {
        if (Build.VERSION.SDK_INT >= 24) {
            val calendar = Calendar.getInstance()
            return Triple(
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        } else {
            val date = Date(System.currentTimeMillis())
            return Triple(
                date.year,
                date.month,
                date.day,
            )
        }
    }

    fun timeToDuration(hours: Int, minutes: Int, seconds: Int): Duration =
        hours.toDuration(DurationUnit.HOURS) +
                minutes.toDuration(DurationUnit.MINUTES) +
                seconds.toDuration(DurationUnit.SECONDS)

    fun dateToDay(date: Date, context: Context): String = DateUtils.formatDateTime(
        context,
        date.time,
        DateUtils.FORMAT_SHOW_DATE
    )

    fun dayToDate(year: Int, month: Int, day: Int): Date {
        if (Build.VERSION.SDK_INT >= 24) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            return Date(calendar.timeInMillis)
        } else {
            return Date(year, month, day)
        }
    }


    fun formatTime(millis: Long): String {
        val hours = millis / (1000 * 60 * 60)
        val hoursStr = if (hours > 9) "$hours:" else if (hours > 0) "0$hours:" else ""
        val minutes = (millis / (1000 * 60)) % 60
        val minutesStr = if (minutes > 9) "$minutes:" else "0$minutes:"
        val seconds = (millis / 1000) % 60
        val secondsStr = if (seconds > 9) "$seconds:" else "0$seconds:"
        val milliseconds = (millis / 10) % 100
        val millisecondsStr = if (milliseconds > 9) "$milliseconds" else "0$milliseconds"
        return "$hoursStr$minutesStr$secondsStr$millisecondsStr"
    }
}
