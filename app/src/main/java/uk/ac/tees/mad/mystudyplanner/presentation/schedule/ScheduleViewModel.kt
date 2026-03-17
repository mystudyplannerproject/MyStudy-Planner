package uk.ac.tees.mad.mystudyplanner.presentation.schedule

import androidx.lifecycle.ViewModel
import uk.ac.tees.mad.mystudyplanner.data.model.StudySchedule
import uk.ac.tees.mad.mystudyplanner.data.repository.ScheduleRepository

class ScheduleViewModel : ViewModel() {

    private val repository = ScheduleRepository()

    fun addSchedule(uiState: ScheduleUiState, onDone: () -> Unit) {
        val schedule = StudySchedule(
            subject = uiState.subject,
            startTime = uiState.startTime,
            endTime = uiState.endTime,
            day = uiState.day
        )
        repository.addSchedule(schedule) {
            if (it) onDone()
        }
    }
}
