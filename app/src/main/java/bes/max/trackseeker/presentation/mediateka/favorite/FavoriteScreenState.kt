package bes.max.trackseeker.presentation.mediateka.favorite

import bes.max.trackseeker.domain.models.Track

sealed interface FavoriteScreenState {

    object Loading : FavoriteScreenState

    data class Content(
        val tracks: List<Track>
    ) : FavoriteScreenState

    object Empty : FavoriteScreenState

}