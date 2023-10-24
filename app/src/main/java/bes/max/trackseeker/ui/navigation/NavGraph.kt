package bes.max.trackseeker.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import bes.max.trackseeker.presentation.utils.GsonTrackConverter
import bes.max.trackseeker.ui.mediateka.favorite.FavoriteTracksScreen
import bes.max.trackseeker.ui.MediatekaScreen
import bes.max.trackseeker.ui.player.PlayerScreen
import bes.max.trackseeker.ui.search.SearchScreen
import bes.max.trackseeker.ui.SettingsScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.MediatekaScreen.route,
    ) {
        composable(route = Screen.SearchScreen.route) {
            SearchScreen(navController = navController)
        }
        composable(
            route = Screen.PlayerScreen.route,
            arguments = listOf(
                navArgument(name = "track") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
            val encodedTrackArg = it.arguments?.getString("track") ?: ""
            val trackArg = Uri.decode(encodedTrackArg)
            val track = GsonTrackConverter.fromJsonToTrack(trackArg)
            PlayerScreen(
                track,
                navigateBack = { navController.popBackStack() })
        }
        composable(route = Screen.FavoritetracksScreen.route) {
            FavoriteTracksScreen(navController = navController)
        }
        composable(route = Screen.MediatekaScreen.route) {
            MediatekaScreen(navController = navController)
        }
        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen()
        }
    }
}