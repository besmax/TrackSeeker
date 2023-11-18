package bes.max.trackseeker.ui.mediateka.playlistdetails

import bes.max.trackseeker.domain.models.Playlist
import bes.max.trackseeker.presentation.mediateka.playlistdetails.PlaylistDetails

sealed interface PlaylistDetailsScreenState {

    object Default : PlaylistDetailsScreenState

    object Editing : PlaylistDetailsScreenState

    data class Menu(val playlist: Playlist) : PlaylistDetailsScreenState

    data class Content(val playlistDetails: PlaylistDetails) : PlaylistDetailsScreenState

    object Empty : PlaylistDetailsScreenState

}