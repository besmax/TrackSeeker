package bes.max.trackseeker.ui.mediateka.playlists

import bes.max.trackseeker.domain.models.Playlist

sealed interface PlaylistScreenState {
    object Loading : PlaylistScreenState

    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistScreenState

    object Empty : PlaylistScreenState
}