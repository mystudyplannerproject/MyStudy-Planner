package uk.ac.tees.mad.mystudyplanner.presentation.schedule

import androidx.compose.runtime.Composable

@Composable
fun ScheduleScreen(
    scheduleId: String?,
    onBack: () -> Unit
) {
    val isEditMode = scheduleId != null

    ScheduleContent(
        isEditMode = isEditMode,
        scheduleId = scheduleId,
        onBack = onBack
    )
}
