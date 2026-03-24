package uk.ac.tees.mad.mystudyplanner.presentation.schedule

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.mystudyplanner.ui.theme.MyStudyPlannerTheme

@Composable
fun ScheduleScreen(
    isEditMode: Boolean,
    scheduleId: String?,
    onBack: () -> Unit,
    viewModel: ScheduleViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

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

    ScheduleContent(
        uiState = uiState,
        isEditMode = isEditMode,
        onSubjectChange = { uiState.copy(subject = it) },
        onDayChange = { uiState.copy(day = it) },
        onStartTimeClick = { /* Time picker later */ },
        onEndTimeClick = { /* Time picker later */ },
        onSaveClick = {
            viewModel.addSchedule(
                uiState = uiState,
                onSuccess = { onBack() },
                onError = { message ->
                    uiState.copy(error = message)
                }
            )
        },
        onDeleteClick = { /* Commit 11 */ }
    )
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