package uk.ac.tees.mad.mystudyplanner.presentation.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uk.ac.tees.mad.mystudyplanner.data.repository.HomeRepository

class HomeViewModel : ViewModel() {

    private val repository = HomeRepository()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        observeToday()
    }

    private fun observeToday() {
        repository.observeTodaySchedules { sessions ->
            _uiState.value = HomeUiState(sessions = sessions)
        }
    }

    override fun onCleared() {
        repository.clearListener()
        super.onCleared()
    }

    fun deleteSession(scheduleId: String) {
        repository.deleteSchedule(scheduleId)
    }
}