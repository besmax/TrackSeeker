package bes.max.trackseeker.presentation.mediateka.newplaylist

sealed interface NewPlaylistScreenState {

    object Default : NewPlaylistScreenState

    object Creating : NewPlaylistScreenState

    object Created : NewPlaylistScreenState

}