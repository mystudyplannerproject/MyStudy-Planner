package uk.ac.tees.mad.mystudyplanner.presentation.schedule

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uk.ac.tees.mad.mystudyplanner.data.model.StudySchedule
import uk.ac.tees.mad.mystudyplanner.data.repository.ScheduleRepository
import uk.ac.tees.mad.mystudyplanner.notification.CalendarHelper
import uk.ac.tees.mad.mystudyplanner.notification.ReminderScheduler
import java.text.SimpleDateFormat
import java.util.*

class ScheduleViewModel : ViewModel() {

    private val repository = ScheduleRepository()

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState.asStateFlow()

    private var isSubmitting = false

    fun updateSubject(value: String) {
        _uiState.value = _uiState.value.copy(subject = value, error = null)
    }

    fun updateStartTime(value: String) {
        _uiState.value = _uiState.value.copy(startTime = value, error = null)
    }

    fun updateEndTime(value: String) {
        _uiState.value = _uiState.value.copy(endTime = value, error = null)
    }

    fun loadSchedule(scheduleId: String) {

        repository.getScheduleById(scheduleId) { schedule ->

            if (schedule != null) {
                _uiState.value = ScheduleUiState(
                    subject = schedule.subject,
                    startTime = schedule.startTime,
                    endTime = schedule.endTime
                )
            } else {
                _uiState.value =
                    _uiState.value.copy(error = "Failed to load schedule")
            }
        }
    }

    fun addSchedule(
        context: Context,
        onSuccess: () -> Unit
    ) {

        val state = _uiState.value

        if (isSubmitting) return

        if (
            state.subject.isBlank() ||
            state.startTime.isBlank() ||
            state.endTime.isBlank()
        ) {
            _uiState.value = state.copy(error = "Please fill all fields")
            return
        }

        isSubmitting = true

        val today = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        ).format(Date())

        val schedule = StudySchedule(
            subject = state.subject,
            startTime = state.startTime,
            endTime = state.endTime,
            selectedDate = today
        )

        repository.addSchedule(schedule) { success ->
            isSubmitting = false

            if (success) {

                val triggerTimeMillis =
                    computeTriggerTime(today, state.startTime)

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
            state.startTime.isBlank() ||
            state.endTime.isBlank()
        ) {
            _uiState.value = state.copy(error = "Please fill all fields")
            return
        }

        val today = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        ).format(Date())

        val schedule = StudySchedule(
            id = scheduleId,
            subject = state.subject,
            startTime = state.startTime,
            endTime = state.endTime,
            selectedDate = today
        )

        repository.updateSchedule(schedule) { success ->

            if (success) {

                ReminderScheduler.cancelReminder(context, scheduleId)

                val triggerTimeMillis =
                    computeTriggerTime(today, state.startTime)

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
                    _uiState.value.copy(error = "Failed to delete schedule")
            }
        }
    }

    private fun computeTriggerTime(
        selectedDate: String,
        startTime: String
    ): Long {

        val dateFormat =
            SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault())

        val combined = "$selectedDate $startTime"

        val parsedDate = dateFormat.parse(combined)
            ?: return System.currentTimeMillis()

        return parsedDate.time
    }
}