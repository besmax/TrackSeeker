package bes.max.trackseeker.domain.mediateka.favorite

import bes.max.trackseeker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {

    suspend fun addTrackToFavorite(track: Track)

    suspend fun deleteTrackFromFavorite(track: Track)

    fun getAllFavoriteTracks(): Flow<List<Track>>

    fun getAllIdsOfFavoriteTracks(): Flow<List<Long>>

    fun trackIsFavorite(trackId: Long): Flow<Boolean>
}