package uk.ac.tees.mad.mystudyplanner.presentation.schedule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScheduleContent(
    isEditMode: Boolean,
    scheduleId: String?,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isEditMode)
                "Edit Schedule Screen (UI will be added next)"
            else
                "Add Schedule Screen (UI will be added next)",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}