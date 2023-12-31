package bes.max.trackseeker.ui.mediateka.editplaylist

import bes.max.trackseeker.domain.models.Playlist

sealed interface EditPlaylistScreenState {
    data class Editing(
        val playlist: Playlist
    ): EditPlaylistScreenState

    object Updated: EditPlaylistScreenState
}