package uk.ac.tees.mad.mystudyplanner.presentation.home

import androidx.lifecycle.ViewModel
import uk.ac.tees.mad.mystudyplanner.data.repository.ScheduleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val repository = ScheduleRepository()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        observeSchedules()
    }

    private fun observeSchedules() {
        repository.observeSchedules { schedules ->

            val sessions = schedules.map {
                StudySessionUiState(
                    id = it.id,
                    subject = it.subject,
                    startTime = it.startTime,
                    endTime = it.endTime,
                    day = it.day
                )
            }

            _uiState.value = HomeUiState(sessions = sessions)
        }
    }

    override fun onCleared() {
        repository.clearListener()
        super.onCleared()
    }
}