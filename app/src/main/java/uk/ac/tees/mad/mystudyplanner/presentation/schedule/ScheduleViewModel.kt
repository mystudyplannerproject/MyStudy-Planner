package uk.ac.tees.mad.mystudyplanner.presentation.schedule

import android.content.Context
import androidx.lifecycle.ViewModel
import uk.ac.tees.mad.mystudyplanner.data.model.StudySchedule
import uk.ac.tees.mad.mystudyplanner.data.repository.ScheduleRepository
import uk.ac.tees.mad.mystudyplanner.notification.CalendarHelper
import uk.ac.tees.mad.mystudyplanner.notification.ReminderScheduler

class ScheduleViewModel : ViewModel() {

    private val repository = ScheduleRepository()
    private var isSubmitting = false

    fun addSchedule(
        context: Context,
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
                onError("Failed to save schedule")
            }
        }
    }

    fun updateSchedule(
        context: Context,
        scheduleId: String?,
        uiState: ScheduleUiState,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (isSubmitting) return

        if (scheduleId.isNullOrBlank()) {
            onError("Invalid schedule")
            return
        }

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
            id = scheduleId,
            subject = uiState.subject,
            startTime = uiState.startTime,
            endTime = uiState.endTime,
            day = uiState.day
        )

        repository.updateSchedule(schedule) { success ->
            isSubmitting = false
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
                onError("Failed to update schedule")
            }
        }
    }

    fun deleteSchedule(
        context: Context,
        scheduleId: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (isSubmitting) return

        if (scheduleId.isNullOrBlank()) {
            onError("Invalid schedule")
            return
        }

        isSubmitting = true

        repository.deleteSchedule(scheduleId) { success ->
            isSubmitting = false
            if (success) {

                ReminderScheduler.cancelReminder(context, scheduleId)

                onSuccess()
            } else {
                onError("Failed to delete schedule. Check internet connection.")
            }
        }
    }
}
