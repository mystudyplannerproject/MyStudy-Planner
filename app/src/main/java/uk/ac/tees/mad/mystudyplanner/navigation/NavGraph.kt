package uk.ac.tees.mad.mystudyplanner.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.mystudyplanner.presentation.auth.AuthScreen
import uk.ac.tees.mad.mystudyplanner.presentation.components.MyStudyPlannerTopBar
import uk.ac.tees.mad.mystudyplanner.presentation.home.HomeScreen
import uk.ac.tees.mad.mystudyplanner.presentation.schedule.ScheduleScreen
import uk.ac.tees.mad.mystudyplanner.presentation.settings.SettingsScreen
import uk.ac.tees.mad.mystudyplanner.presentation.splash.SplashScreen
import uk.ac.tees.mad.mystudyplanner.presentation.splash.SplashViewModel

@Composable
fun MyStudyPlannerNavGraph() {

    val navController = rememberNavController()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val showTopBar = currentRoute != Splash::class.qualifiedName &&
            currentRoute != Auth::class.qualifiedName

    Scaffold(
        topBar = {
            if (showTopBar) {
                MyStudyPlannerTopBar(
                    onSettingsClick = {
                        navController.navigate(Settings)
                    }
                )
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Splash,
            modifier = Modifier.padding(padding)
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
                val scheduleId =
                    backStackEntry.arguments?.getString("scheduleId")
                val isEditMode = scheduleId != null

                ScheduleScreen(
                    isEditMode = isEditMode,
                    scheduleId = scheduleId,
                    onBack = { navController.popBackStack() }
                )
            }

            composable<Settings> {
                SettingsScreen(
                    onLogout = {
                        navController.navigate(Auth) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}