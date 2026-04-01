package uk.ac.tees.mad.mystudyplanner.presentation.settings

data class SettingsUiState(
    val remindersEnabled: Boolean = true,
    val reminderOffset: Int = 10
)