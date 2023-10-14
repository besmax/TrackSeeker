package bes.max.trackseeker.data.mediateka

import bes.max.trackseeker.data.db.FavoriteTracksDao
import bes.max.trackseeker.data.mappers.TrackDbMapper
import bes.max.trackseeker.domain.mediateka.FavoriteTracksRepository
import bes.max.trackseeker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val dao: FavoriteTracksDao,
    private val convertor: TrackDbMapper
) : FavoriteTracksRepository {
    override suspend fun addTrackToFavorite(track: Track) {
        dao.insertTrack(convertor.map(track))
    }

    override suspend fun deleteTrackFromFavorite(track: Track) {
        dao.deleteTrack(convertor.map(track))
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = dao.getAllFavoriteTracks().map { convertor.map(it) }
        emit(tracks)
    }

    override fun getAllIdsOfFavoriteTracks(): Flow<List<Long>> = flow {
        emit(dao.getAllIdsOfFavoriteTracks())
    }
}