package uk.ac.tees.mad.mystudyplanner.presentation.schedule

import android.Manifest
import android.app.TimePickerDialog
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.tees.mad.mystudyplanner.ui.theme.MyStudyPlannerTheme
import java.util.Calendar

@Composable
fun ScheduleScreen(
    isEditMode: Boolean,
    scheduleId: String?,
    onBack: () -> Unit,
    viewModel: ScheduleViewModel = viewModel()
) {

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var uiState by remember {
        mutableStateOf(
            ScheduleUiState(
                subject = if (isEditMode) "Mathematics" else "",
                startTime = if (isEditMode) "09:00 AM" else "",
                endTime = if (isEditMode) "10:00 AM" else "",
                day = if (isEditMode) "Monday" else ""
            )
        )
    }

    fun showTimePicker(onTimeSelected: (String) -> Unit) {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                val formattedTime = formatTo12Hour(selectedHour, selectedMinute)
                onTimeSelected(formattedTime)
            },
            hour,
            minute,
            false
        ).show()
    }

    ScheduleContent(
        uiState = uiState,
        isEditMode = isEditMode,
        onSubjectChange = { uiState = uiState.copy(subject = it) },
        onDayChange = { uiState = uiState.copy(day = it) },

        onStartTimeClick = {
            showTimePicker { time ->
                uiState = uiState.copy(startTime = time)
            }
        },

        onEndTimeClick = {
            showTimePicker { time ->
                uiState = uiState.copy(endTime = time)
            }
        },

        onSaveClick = {
            if (isEditMode) {
                viewModel.updateSchedule(
                    context = context,
                    scheduleId = scheduleId,
                    uiState = uiState,
                    onSuccess = { onBack() },
                    onError = { message ->
                        uiState = uiState.copy(error = message)
                    }
                )
            } else {
                viewModel.addSchedule(
                    context = context,
                    uiState = uiState,
                    onSuccess = { onBack() },
                    onError = { message ->
                        uiState = uiState.copy(error = message)
                    }
                )
            }
        },

        onDeleteClick = {
            viewModel.deleteSchedule(
                context = context,
                scheduleId = scheduleId,
                onSuccess = { onBack() },
                onError = { message ->
                    uiState = uiState.copy(error = message)
                }
            )
        }
    )
}

private fun formatTo12Hour(hour: Int, minute: Int): String {
    val amPm = if (hour >= 12) "PM" else "AM"

    val formattedHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }

    return String.format("%02d:%02d %s", formattedHour, minute, amPm)
}

@Preview(showBackground = true)
@Composable
fun AddSchedulePreview() {
    MyStudyPlannerTheme {
        ScheduleContent(
            uiState = ScheduleUiState(),
            isEditMode = false,
            onSubjectChange = {},
            onDayChange = {},
            onStartTimeClick = {},
            onEndTimeClick = {},
            onSaveClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditSchedulePreview() {
    MyStudyPlannerTheme {
        ScheduleContent(
            uiState = ScheduleUiState(
                subject = "Physics",
                startTime = "11:00 AM",
                endTime = "12:00 PM",
                day = "Tuesday"
            ),
            isEditMode = true,
            onSubjectChange = {},
            onDayChange = {},
            onStartTimeClick = {},
            onEndTimeClick = {},
            onSaveClick = {},
            onDeleteClick = {}
        )
    }
}
