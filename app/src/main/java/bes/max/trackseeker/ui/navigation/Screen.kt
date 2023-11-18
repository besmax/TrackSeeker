package bes.max.trackseeker.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import bes.max.trackseeker.R

sealed class Screen(val route: String, @StringRes val titleResId: Int, @DrawableRes val iconResId: Int? = null) {
    object SearchScreen : Screen("searchScreen", R.string.search, R.drawable.ic_search)
    object PlayerScreen : Screen("playerScreen/{track}", R.string.player)
    object MediatekaScreen : Screen("mediatekaScreen", R.string.mediateka, R.drawable.ic_mediateka)
    object SettingsScreen : Screen("settingsScreen", R.string.settings, R.drawable.ic_settings)
    object FavoritetracksScreen : Screen("favoriteTracksScreen", R.string.mediateka_tab_title_favorite_tracks)
    object PlaylistsScreen : Screen("playlistsScreen", R.string.mediateka_tab_title_playlist)

    object NewPlaylistScreen : Screen("newPlaylistScreen?{track}", R.string.new_playlist)
    object PlaylistDetailsScreen : Screen("playlistDetailsScreen/{playlistId}", R.string.playlist)

    object EditPlaylistScreen : Screen("editPlaylistScreen/{playlistId}", R.string.edit)
}
