package uk.ac.tees.mad.mystudyplanner.presentation.home

data class StudySessionUiState(
    val subject: String,
    val startTime: String,
    val endTime: String,
    val isNext: Boolean = false
)