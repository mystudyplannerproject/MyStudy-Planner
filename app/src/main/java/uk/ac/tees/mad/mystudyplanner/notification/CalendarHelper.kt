package uk.ac.tees.mad.mystudyplanner.notification

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CalendarContract
import androidx.core.content.ContextCompat
import java.util.TimeZone

object CalendarHelper {

    fun addEventToCalendar(
        context: Context,
        scheduleId: String,
        title: String,
        startTimeMillis: Long,
        endTimeMillis: Long
    ) {

        if (!hasCalendarPermission(context)) return

        if (isEventAlreadyAdded(context, scheduleId)) return

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startTimeMillis)
            put(CalendarContract.Events.DTEND, endTimeMillis)
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, "Study Session")
            put(CalendarContract.Events.CALENDAR_ID, 1)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
            put(CalendarContract.Events.HAS_ALARM, 1)
            put(CalendarContract.Events.UID_2445, scheduleId) // custom marker
        }

        context.contentResolver.insert(
            CalendarContract.Events.CONTENT_URI,
            values
        )
    }

    fun hasCalendarPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isEventAlreadyAdded(context: Context, scheduleId: String): Boolean {

        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.UID_2445
        )

        val selection = "${CalendarContract.Events.UID_2445} = ?"
        val selectionArgs = arrayOf(scheduleId)

        val cursor = context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        val exists = (cursor?.count ?: 0) > 0
        cursor?.close()
        return exists
    }
}