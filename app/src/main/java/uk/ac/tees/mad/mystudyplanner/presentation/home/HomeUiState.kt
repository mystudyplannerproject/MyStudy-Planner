package uk.ac.tees.mad.mystudyplanner.presentation.home

import uk.ac.tees.mad.mystudyplanner.presentation.components.StudySessionUiState

data class HomeUiState(
    val sessions: List<StudySessionUiState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)