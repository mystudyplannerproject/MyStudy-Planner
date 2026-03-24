package uk.ac.tees.mad.mystudyplanner.presentation.schedule

import androidx.lifecycle.ViewModel
import uk.ac.tees.mad.mystudyplanner.data.model.StudySchedule
import uk.ac.tees.mad.mystudyplanner.data.repository.ScheduleRepository

class ScheduleViewModel : ViewModel() {

    private val repository = ScheduleRepository()
    private var isSubmitting = false

    fun addSchedule(
        uiState: ScheduleUiState,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        if (isSubmitting) return

        if (
            uiState.subject.isBlank() ||
            uiState.day.isBlank() ||
            uiState.startTime.isBlank() ||
            uiState.endTime.isBlank()
        ) {
            onError("Please fill all fields")
            return
        }

        isSubmitting = true

        val schedule = StudySchedule(
            subject = uiState.subject,
            startTime = uiState.startTime,
            endTime = uiState.endTime,
            day = uiState.day
        )

        repository.addSchedule(schedule) { success ->
            isSubmitting = false
            if (success) {
                onSuccess()
            } else {
                onError("Failed to save schedule")
            }
        }
    }
}
