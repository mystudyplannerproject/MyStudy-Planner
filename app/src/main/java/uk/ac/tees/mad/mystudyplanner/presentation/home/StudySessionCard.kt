package uk.ac.tees.mad.mystudyplanner.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.mystudyplanner.ui.theme.MyStudyPlannerTheme

@Composable
fun StudySessionCard(
    session: StudySessionUiState
) {
    val cardColor = if (session.isNext)
        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    else
        MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = session.subject,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${session.startTime} - ${session.endTime}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            if (session.isNext) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Next Session",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudySessionCardPreview() {
    MyStudyPlannerTheme {
        Column(modifier = Modifier.padding(16.dp)) {

            StudySessionCard(
                session = StudySessionUiState(
                    subject = "Mathematics",
                    startTime = "09:00 AM",
                    endTime = "10:00 AM",
                    isNext = true
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            StudySessionCard(
                session = StudySessionUiState(
                    subject = "Physics",
                    startTime = "11:00 AM",
                    endTime = "12:00 PM",
                    isNext = false
                )
            )
        }
    }
}