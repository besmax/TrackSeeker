package bes.max.trackseeker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import bes.max.trackseeker.domain.models.Track
import bes.max.trackseeker.ui.GsonTrackConverter
import bes.max.trackseeker.ui.PlayerScreen
import bes.max.trackseeker.ui.SearchScreen
import bes.max.trackseeker.ui.theme.TrackSeekerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TrackSeekerTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "searchScreen",
                        modifier = Modifier.weight(1f)
                    ) {
                        composable(route = "searchScreen") {
                            SearchScreen(navController = navController)
                        }
                        composable(
                            route = "playerScreen/{track}",
                            arguments = listOf(
                                navArgument(name = "track") {
                                    type = NavType.StringType
                                    nullable = false
                                }
                            )
                        ) {
                            val trackArg = it.arguments?.getString("track") ?: ""
                            val track = GsonTrackConverter.fromJsonToTrack(trackArg)
                            PlayerScreen(
                                track,
                                navigateBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}



