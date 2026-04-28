package com.andika.mindtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.andika.mindtrack.ui.CalendarScreen
import com.andika.mindtrack.ui.NoteEditorScreen
import com.andika.mindtrack.ui.NoteViewModel
import com.andika.mindtrack.ui.NoteViewModelFactory
import com.andika.mindtrack.ui.theme.MindTrackTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    
    private val viewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NoteApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MindTrackTheme {
                MindTrackApp(viewModel)
            }
        }
    }
}

@Composable
fun MindTrackApp(viewModel: NoteViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "calendar"
    ) {
        composable("calendar") {
            CalendarScreen(
                viewModel = viewModel,
                onDateSelected = { date ->
                    navController.navigate("editor/${date}")
                }
            )
        }
        composable(
            route = "editor/{date}",
            arguments = listOf(navArgument("date") { type = NavType.StringType })
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: LocalDate.now().toString()
            NoteEditorScreen(
                date = date,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
