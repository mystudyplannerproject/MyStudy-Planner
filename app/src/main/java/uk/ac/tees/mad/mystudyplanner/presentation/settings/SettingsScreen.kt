package uk.ac.tees.mad.mystudyplanner.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Customize your study experience",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            Surface(
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 2.dp,
                shadowElevation = 4.dp,
                color = MaterialTheme.colorScheme.surface
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {

                    // REMINDERS SWITCH
                    Column {
                        Text(
                            text = "Reminders",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Enable Reminders",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Switch(
                                checked = uiState.remindersEnabled,
                                onCheckedChange = {
                                    uiState = uiState.copy(remindersEnabled = it)
                                    viewModel.updateReminders(context, it)
                                }
                            )
                        }
                    }

                    Column {

                        Text(
                            text = "Reminder Time",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${uiState.reminderOffset} minutes before session",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Slider(
                            value = uiState.reminderOffset.toFloat(),
                            onValueChange = {
                                uiState = uiState.copy(reminderOffset = it.toInt())
                                viewModel.updateOffset(context, it.toInt())
                            },
                            valueRange = 0f..60f,
                            steps = 11
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                onClick = {
                    viewModel.logout(context) {
                        onLogout()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
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