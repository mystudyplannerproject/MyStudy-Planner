package uk.ac.tees.mad.mystudyplanner.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.tees.mad.mystudyplanner.ui.theme.MyStudyPlannerTheme

@Composable
fun SettingsScreen(
    onLogout: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {

    val context = LocalContext.current
    var uiState by remember { mutableStateOf(viewModel.loadSettings(context)) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Enable Reminders")
                Switch(
                    checked = uiState.remindersEnabled,
                    onCheckedChange = {
                        uiState = uiState.copy(remindersEnabled = it)
                        viewModel.updateReminders(context, it)
                    }
                )
            }

            Column {
                Text("Reminder Offset (minutes before)")
                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = uiState.reminderOffset.toFloat(),
                    onValueChange = {
                        uiState = uiState.copy(reminderOffset = it.toInt())
                        viewModel.updateOffset(context, it.toInt())
                    },
                    valueRange = 0f..60f,
                    steps = 11
                )

                Text("${uiState.reminderOffset} minutes")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.logout(context) {
                        onLogout()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Logout")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    MyStudyPlannerTheme {
        SettingsScreen(onLogout = {})
    }
}