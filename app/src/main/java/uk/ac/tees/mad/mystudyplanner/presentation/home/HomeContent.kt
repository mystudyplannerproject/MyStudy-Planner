package uk.ac.tees.mad.mystudyplanner.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.mystudyplanner.presentation.components.StudySessionCard
import uk.ac.tees.mad.mystudyplanner.presentation.components.StudySessionUiState

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onAddScheduleClick: () -> Unit,
    onSessionClick: (StudySessionUiState) -> Unit,
    onDeleteSession: (String) -> Unit
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddScheduleClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Schedule"
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {

            Text(
                text = "Your Study Plan",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Stay consistent. Stay focused.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            if (uiState.sessions.isEmpty()) {
                EmptyStudyPlan()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    contentPadding = PaddingValues(bottom = 120.dp)
                ) {
                    items(
                        items = uiState.sessions,
                        key = { it.id }
                    ) { session ->

                        SwipeToDeleteItem(
                            session = session,
                            onDelete = onDeleteSession
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        enabled = !session.isCompleted
                                    ) {
                                        onSessionClick(session)
                                    }
                            ) {
                                StudySessionCard(session)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun EmptyStudyPlan() {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "No sessions planned today",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Tap + to add your first study session",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteItem(
    session: StudySessionUiState,
    onDelete: (String) -> Unit,
    content: @Composable () -> Unit
) {

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete(session.id)
            }
            true
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 6.dp)
                    .background(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) {
        content()
    }
}