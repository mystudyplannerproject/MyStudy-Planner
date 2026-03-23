package uk.ac.tees.mad.mystudyplanner.presentation.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ScheduleContent(
    uiState: ScheduleUiState,
    isEditMode: Boolean,
    onSubjectChange: (String) -> Unit,
    onDayChange: (String) -> Unit,
    onStartTimeClick: () -> Unit,
    onEndTimeClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Text(
            text = if (isEditMode) "Edit Study Schedule" else "Add Study Schedule",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = uiState.subject,
            onValueChange = onSubjectChange,
            label = { Text("Subject") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.day,
            onValueChange = onDayChange,
            label = { Text("Day (e.g. Monday)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.startTime,
            onValueChange = {},
            readOnly = true,
            label = { Text("Start Time") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onStartTimeClick() }
        )

        OutlinedTextField(
            value = uiState.endTime,
            onValueChange = {},
            readOnly = true,
            label = { Text("End Time") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEndTimeClick() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isEditMode) "Update Schedule" else "Save Schedule")
        }

        if (isEditMode) {
            OutlinedButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete Schedule")
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDeleteClick()
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Delete Schedule") },
            text = { Text("Are you sure you want to delete this schedule?") }
        )
    }
}