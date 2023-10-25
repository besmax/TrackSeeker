package bes.max.trackseeker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import bes.max.trackseeker.presentation.settings.SettingsViewModel
import bes.max.trackseeker.ui.navigation.BottomNavBar
import bes.max.trackseeker.ui.navigation.NavigationGraph
import bes.max.trackseeker.ui.navigation.Screen
import bes.max.trackseeker.ui.theme.TrackSeekerTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val settingsViewModel: SettingsViewModel = koinViewModel()
            val darkTheme by settingsViewModel.isNightModeActive.collectAsState(initial = isSystemInDarkTheme())

            TrackSeekerTheme(
                darkTheme = darkTheme
            ) {
                val navController = rememberNavController()

                var buttonsVisible = rememberSaveable { mutableStateOf(true) }

                val navBackStackEntry by navController.currentBackStackEntryAsState()

                when (navBackStackEntry?.destination?.route) {
                    Screen.SettingsScreen.route -> buttonsVisible.value = true
                    Screen.MediatekaScreen.route -> buttonsVisible.value = true
                    Screen.SearchScreen.route -> buttonsVisible.value = true
                    Screen.PlayerScreen.route -> buttonsVisible.value = false
                }

                Scaffold(
                    bottomBar = {
                        BottomNavBar(
                            navController = navController,
                            state = buttonsVisible,
                            modifier = Modifier
                        )
                    }
                ) { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues)
                    ) {

                        NavigationGraph(navController = navController)
                    }
                }
            }
        }
    }
}



