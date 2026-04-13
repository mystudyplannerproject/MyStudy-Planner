package uk.ac.tees.mad.mystudyplanner.notification

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import java.util.*

object CalendarHelper {

    fun addEventToCalendar(
        context: Context,
        scheduleId: String,
        title: String,
        startTimeMillis: Long,
        endTimeMillis: Long
    ) {

        if (!hasCalendarPermission(context)) return

        val calendarId = getPrimaryCalendarId(context) ?: return

        if (isEventAlreadyAdded(context, scheduleId)) return

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startTimeMillis)
            put(CalendarContract.Events.DTEND, endTimeMillis)
            put(CalendarContract.Events.TITLE, title)

            put(
                CalendarContract.Events.DESCRIPTION,
                "Study Session | MSP_ID:$scheduleId"
            )

            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }

        context.contentResolver.insert(
            CalendarContract.Events.CONTENT_URI,
            values
        )
    }

    private fun getPrimaryCalendarId(context: Context): Long? {

        val projection = arrayOf(
            CalendarContract.Calendars._ID
        )

        val cursor = context.contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                return it.getLong(0)
            }
        }

        return null
    }

    private fun isEventAlreadyAdded(context: Context, scheduleId: String): Boolean {

        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.DESCRIPTION
        )

        val cursor = context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val description = it.getString(1) ?: continue
                if (description.contains("MSP_ID:$scheduleId")) {
                    return true
                }
            }
        }

        return false
    }

    fun hasCalendarPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED
    }
}