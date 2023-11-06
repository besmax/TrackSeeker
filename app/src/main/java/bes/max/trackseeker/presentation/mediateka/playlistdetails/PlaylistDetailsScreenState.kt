package bes.max.trackseeker.presentation.mediateka.playlistdetails

import bes.max.trackseeker.domain.models.Playlist

sealed interface PlaylistDetailsScreenState {

    object Default : PlaylistDetailsScreenState

    object Editing : PlaylistDetailsScreenState

    data class Menu(val playlist: Playlist) : PlaylistDetailsScreenState

    data class Content(val playlistDetails: PlaylistDetails) : PlaylistDetailsScreenState

    object Empty : PlaylistDetailsScreenState

}