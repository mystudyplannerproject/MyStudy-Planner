package uk.ac.tees.mad.mystudyplanner.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.mystudyplanner.ui.theme.MyStudyPlannerTheme

@Composable
fun HomeScreen() {

    val dummySessions = listOf(
        StudySessionUiState("Mathematics", "09:00 AM", "10:00 AM", isNext = true),
        StudySessionUiState("Physics", "11:00 AM", "12:00 PM"),
        StudySessionUiState("Chemistry", "02:00 PM", "03:00 PM")
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Navigate later */ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Schedule",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Text(
                text = "Today's Study Plan",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(dummySessions) { session ->
                    StudySessionCard(session)
                }
            }
        }
    }
}

@Composable
fun EmptyStudyPlan() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No study sessions planned for today",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyStudyPlannerTheme {
        HomeScreen()
    }
}