package uk.ac.tees.mad.mystudyplanner.presentation.components

data class StudySessionUiState(
    val id: String = "",
    val subject: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val selectedDate: String = "",
    val isNext: Boolean = false,
    val isCompleted: Boolean = false
)