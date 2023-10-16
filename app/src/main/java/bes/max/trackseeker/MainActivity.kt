package bes.max.trackseeker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import bes.max.trackseeker.ui.FavoriteTracksScreen
import bes.max.trackseeker.presentation.utils.GsonTrackConverter
import bes.max.trackseeker.ui.MediatekaScreen
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
                        startDestination = "mediatekaScreen",
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
                        composable(route = "favoriteTracksScreen") {
                            FavoriteTracksScreen(navController = navController)
                        }
                        composable(route = "mediatekaScreen") {
                            MediatekaScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}



