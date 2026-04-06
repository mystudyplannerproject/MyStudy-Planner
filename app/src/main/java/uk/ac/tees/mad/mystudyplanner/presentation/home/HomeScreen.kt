package uk.ac.tees.mad.mystudyplanner.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.tees.mad.mystudyplanner.ui.theme.MyStudyPlannerTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onAddScheduleClick: () -> Unit,
    onEditScheduleClick: (String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    HomeContent(
        uiState = uiState,
        onAddScheduleClick = onAddScheduleClick,
        onSessionClick = {
            onEditScheduleClick(it.id)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    MyStudyPlannerTheme {
        HomeContent(
            uiState = HomeUiState(
                sessions = listOf(
                    StudySessionUiState(
                        id = "1",
                        subject = "Mathematics",
                        startTime = "09:00 AM",
                        endTime = "10:00 AM",
                        isNext = true
                    ),
                    StudySessionUiState(
                        id = "2",
                        subject = "Physics",
                        startTime = "11:00 AM",
                        endTime = "12:00 PM"
                    )
                )
            ),
            onAddScheduleClick = {},
            onSessionClick = {}
        )
    }
}