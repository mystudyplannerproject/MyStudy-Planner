package uk.ac.tees.mad.mystudyplanner.presentation.home

data class HomeUiState(
    val sessions: List<StudySessionUiState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)