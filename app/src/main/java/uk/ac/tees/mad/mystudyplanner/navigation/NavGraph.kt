package uk.ac.tees.mad.mystudyplanner.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.mystudyplanner.presentation.home.HomeScreen
import uk.ac.tees.mad.mystudyplanner.presentation.splash.SplashScreen

@Composable
fun MyStudyPlannerNavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Splash
    ) {

        composable<Splash> {
            SplashScreen(
                onNavigateNext = {
                    navController.navigate(Home) {
                        popUpTo<Splash> { inclusive = true }
                    }
                }
            )
        }

        composable<Home> {
            HomeScreen()
        }
    }
}