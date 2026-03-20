package uk.ac.tees.mad.mystudyplanner.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.mystudyplanner.presentation.auth.AuthScreen
import uk.ac.tees.mad.mystudyplanner.presentation.home.HomeScreen
import uk.ac.tees.mad.mystudyplanner.presentation.schedule.ScheduleScreen
import uk.ac.tees.mad.mystudyplanner.presentation.splash.SplashScreen
import uk.ac.tees.mad.mystudyplanner.presentation.splash.SplashViewModel

@Composable
fun MyStudyPlannerNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Splash
    ) {

        composable<Splash> {
            val viewModel: SplashViewModel = viewModel()

            SplashScreen(
                viewModel = viewModel,
                onNavigateToHome = {
                    navController.navigate(Home) {
                        popUpTo<Splash> { inclusive = true }
                    }
                },
                onNavigateToAuth = {
                    navController.navigate(Auth) {
                        popUpTo<Splash> { inclusive = true }
                    }
                }
            )

        }

        composable<Auth> {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(Home) {
                        popUpTo<Auth> { inclusive = true }
                    }
                }
            )
        }

        composable<Home> {
            HomeScreen(
                onAddScheduleClick = {
                    navController.navigate(Schedule())
                },
                onEditScheduleClick = { scheduleId ->
                    navController.navigate(Schedule(scheduleId))
                }
            )
        }

        composable<Schedule> { backStackEntry ->
            val args = backStackEntry.arguments
            val scheduleId = args?.getString("scheduleId")

            ScheduleScreen(
                scheduleId = scheduleId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}