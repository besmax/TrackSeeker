package bes.max.trackseeker.domain.mediateka

import bes.max.trackseeker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : FavoriteTracksInteractor {
    override suspend fun addTrackToFavorite(track: Track) {
        favoriteTracksRepository.addTrackToFavorite(track)
    }

    override suspend fun deleteTrackFromFavorite(track: Track) {
        favoriteTracksRepository.deleteTrackFromFavorite(track)
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> =
        favoriteTracksRepository.getAllFavoriteTracks()

    override fun getAllIdsOfFavoriteTracks(): Flow<List<Long>> =
        favoriteTracksRepository.getAllIdsOfFavoriteTracks()
}