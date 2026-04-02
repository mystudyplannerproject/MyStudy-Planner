package uk.ac.tees.mad.mystudyplanner.presentation.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.mystudyplanner.data.local.PreferencesManager

class SettingsViewModel : ViewModel() {

    fun loadSettings(context: Context): SettingsUiState {
        val prefs = PreferencesManager(context)
        return SettingsUiState(
            remindersEnabled = prefs.isRemindersEnabled(),
            reminderOffset = prefs.getReminderOffset()
        )
    }

    fun updateReminders(context: Context, enabled: Boolean) {
        PreferencesManager(context).setRemindersEnabled(enabled)
    }

    fun updateOffset(context: Context, minutes: Int) {
        PreferencesManager(context).setReminderOffset(minutes)
    }

    fun logout(context: Context, onLoggedOut: () -> Unit) {
        FirebaseAuth.getInstance().signOut()
        PreferencesManager(context).clearAll()
        onLoggedOut()
    }

}