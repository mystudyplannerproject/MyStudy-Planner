package uk.ac.tees.mad.mystudyplanner.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("mystudyplanner_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_REMINDERS_ENABLED = "reminders_enabled"
        private const val KEY_REMINDER_OFFSET = "reminder_offset"
    }

    fun setRemindersEnabled(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_REMINDERS_ENABLED, enabled) }
    }

    fun isRemindersEnabled(): Boolean {
        return prefs.getBoolean(KEY_REMINDERS_ENABLED, true)
    }

    fun setReminderOffset(minutes: Int) {
        prefs.edit { putInt(KEY_REMINDER_OFFSET, minutes) }
    }

    fun getReminderOffset(): Int {
        return prefs.getInt(KEY_REMINDER_OFFSET, 10)
    }

    fun clearAll() {
        prefs.edit { clear() }
    }
}
