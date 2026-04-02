package uk.ac.tees.mad.mystudyplanner.presentation.schedule

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.mystudyplanner.data.model.StudySchedule
import uk.ac.tees.mad.mystudyplanner.data.repository.ScheduleRepository
import uk.ac.tees.mad.mystudyplanner.notification.CalendarHelper
import uk.ac.tees.mad.mystudyplanner.notification.ReminderScheduler

class ScheduleViewModel : ViewModel() {

    private val repository = ScheduleRepository()

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    private var isSubmitting = false

    fun updateSubject(value: String) {
        _uiState.value = _uiState.value.copy(subject = value, error = null)
    }

    fun updateDay(value: String) {
        _uiState.value = _uiState.value.copy(day = value, error = null)
    }

    fun updateStartTime(value: String) {
        _uiState.value = _uiState.value.copy(startTime = value, error = null)
    }

    fun updateEndTime(value: String) {
        _uiState.value = _uiState.value.copy(endTime = value, error = null)
    }

    fun addSchedule(
        context: Context,
        onSuccess: () -> Unit
    ) {

        val state = _uiState.value

        if (isSubmitting) return

        if (
            state.subject.isBlank() ||
            state.day.isBlank() ||
            state.startTime.isBlank() ||
            state.endTime.isBlank()
        ) {
            _uiState.value = state.copy(error = "Please fill all fields")
            return
        }

        isSubmitting = true

        val schedule = StudySchedule(
            subject = state.subject,
            startTime = state.startTime,
            endTime = state.endTime,
            day = state.day
        )

        repository.addSchedule(schedule) { success ->
            isSubmitting = false

            if (success) {

                val triggerTimeMillis = System.currentTimeMillis() + 60000

                ReminderScheduler.scheduleReminder(
                    context,
                    schedule.id,
                    schedule.subject,
                    triggerTimeMillis
                )

                CalendarHelper.addEventToCalendar(
                    context = context,
                    scheduleId = schedule.id,
                    title = schedule.subject,
                    startTimeMillis = triggerTimeMillis,
                    endTimeMillis = triggerTimeMillis + 3600000
                )

                onSuccess()

            } else {
                _uiState.value =
                    _uiState.value.copy(error = "Failed to save schedule")
            }
        }
    }

    fun updateSchedule(
        context: Context,
        scheduleId: String?,
        onSuccess: () -> Unit
    ) {

        val state = _uiState.value

        if (scheduleId.isNullOrBlank()) {
            _uiState.value = state.copy(error = "Invalid schedule")
            return
        }

        if (
            state.subject.isBlank() ||
            state.day.isBlank() ||
            state.startTime.isBlank() ||
            state.endTime.isBlank()
        ) {
            _uiState.value = state.copy(error = "Please fill all fields")
            return
        }

        val schedule = StudySchedule(
            id = scheduleId,
            subject = state.subject,
            startTime = state.startTime,
            endTime = state.endTime,
            day = state.day
        )

        repository.updateSchedule(schedule) { success ->

            if (success) {

                ReminderScheduler.cancelReminder(context, scheduleId)

                val triggerTimeMillis = System.currentTimeMillis() + 60000

                ReminderScheduler.scheduleReminder(
                    context,
                    scheduleId,
                    schedule.subject,
                    triggerTimeMillis
                )

                CalendarHelper.addEventToCalendar(
                    context = context,
                    scheduleId = scheduleId,
                    title = schedule.subject,
                    startTimeMillis = triggerTimeMillis,
                    endTimeMillis = triggerTimeMillis + 3600000
                )

                onSuccess()

            } else {
                _uiState.value =
                    _uiState.value.copy(error = "Failed to update schedule")
            }
        }
    }

    fun deleteSchedule(
        context: Context,
        scheduleId: String?,
        onSuccess: () -> Unit
    ) {

        if (scheduleId.isNullOrBlank()) {
            _uiState.value =
                _uiState.value.copy(error = "Invalid schedule")
            return
        }

        repository.deleteSchedule(scheduleId) { success ->

            if (success) {

                ReminderScheduler.cancelReminder(context, scheduleId)
                onSuccess()

            } else {
                _uiState.value =
                    _uiState.value.copy(
                        error = "Failed to delete schedule"
                    )
            }
        }
    }
}