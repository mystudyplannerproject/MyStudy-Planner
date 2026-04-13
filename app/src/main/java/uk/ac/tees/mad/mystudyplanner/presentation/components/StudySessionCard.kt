package uk.ac.tees.mad.mystudyplanner.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.mystudyplanner.ui.theme.MyStudyPlannerTheme

@Composable
fun StudySessionCard(
    session: StudySessionUiState
) {
    val isNext = session.isNext
    val isCompleted = session.isCompleted

    val accentColor = when {
        isNext -> MaterialTheme.colorScheme.primary
        isCompleted -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.secondary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isNext) 8.dp else 3.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = accentColor.copy(alpha = 0.2f),
                spotColor = accentColor.copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {

        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .height(60.dp)
                    .width(4.dp)
                    .alpha(0.9f)
                    .background(
                        color = accentColor,
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = session.subject,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${session.startTime} – ${session.endTime}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (isNext) {
                    StatusChip(
                        text = "Next Session",
                        background = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.surface
                    )
                }

                if (isCompleted) {
                    StatusChip(
                        text = "Completed",
                        background = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
fun StatusChip(
    text: String,
    background: Color,
    contentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = background
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
            fontWeight = FontWeight.SemiBold
        )
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

            Spacer(modifier = Modifier.height(12.dp))

            StudySessionCard(
                session = StudySessionUiState(
                    subject = "Physics",
                    startTime = "11:00 AM",
                    endTime = "12:00 PM",
                    isCompleted = true
                )
            )
        }
    }
}