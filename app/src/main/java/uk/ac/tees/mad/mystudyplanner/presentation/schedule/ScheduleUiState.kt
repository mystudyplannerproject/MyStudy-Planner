package uk.ac.tees.mad.mystudyplanner.presentation.schedule

data class ScheduleUiState(
    val subject: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val day: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)