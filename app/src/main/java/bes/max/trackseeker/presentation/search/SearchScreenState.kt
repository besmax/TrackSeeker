package bes.max.trackseeker.presentation.search

import bes.max.trackseeker.domain.models.Track

sealed interface SearchScreenState {

    object Default : SearchScreenState

    object Loading : SearchScreenState

    object History : SearchScreenState {
        var tracks: List<Track> = emptyList()
    }

    object Tracks : SearchScreenState {
        var tracks: List<Track> = emptyList()
    }

    object TracksNotFound : SearchScreenState

    object SearchError : SearchScreenState
}