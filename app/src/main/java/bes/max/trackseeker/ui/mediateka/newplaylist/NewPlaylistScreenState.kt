package bes.max.trackseeker.ui.mediateka.newplaylist

sealed interface NewPlaylistScreenState {

    object Default : NewPlaylistScreenState

    object Creating : NewPlaylistScreenState

    object Created : NewPlaylistScreenState

}