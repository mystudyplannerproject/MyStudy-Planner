package uk.ac.tees.mad.mystudyplanner

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import uk.ac.tees.mad.mystudyplanner.navigation.MyStudyPlannerNavGraph
import uk.ac.tees.mad.mystudyplanner.ui.theme.MyStudyPlannerTheme
import android.Manifest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyStudyPlannerTheme {
                RequestPermissionsIfNeeded()
                MyStudyPlannerNavGraph()
            }
        }
    }
}

@Composable
fun RequestPermissionsIfNeeded() {

    val context = androidx.compose.ui.platform.LocalContext.current

    var notificationGranted by remember { mutableStateOf(false) }

    val calendarPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { }

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->
            notificationGranted = granted
        }

    LaunchedEffect(Unit) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                notificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } else {
                notificationGranted = true
            }

        } else {
            notificationGranted = true
        }
    }

    LaunchedEffect(notificationGranted) {

        if (notificationGranted) {

            val calendarPermissions = arrayOf(
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR
            )

            val needCalendarPermission = calendarPermissions.any {
                ContextCompat.checkSelfPermission(
                    context,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            }

            if (needCalendarPermission) {
                calendarPermissionLauncher.launch(calendarPermissions)
            }
        }
    }
}