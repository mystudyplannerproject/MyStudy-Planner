package uk.ac.tees.mad.mystudyplanner.presentation.home

import androidx.lifecycle.ViewModel
import uk.ac.tees.mad.mystudyplanner.data.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val repository = HomeRepository()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    fun loadTodayPlan() {
        repository.observeTodaySchedules { sessions ->
            _uiState.value = HomeUiState(sessions = sessions)
        }
    }

    override fun onCleared() {
        repository.clearListener()
        super.onCleared()
    }
}
