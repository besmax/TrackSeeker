package bes.max.trackseeker.data.mediateka

import bes.max.trackseeker.data.db.dao.TrackDao
import bes.max.trackseeker.data.mappers.TrackDbMapper
import bes.max.trackseeker.domain.mediateka.favorite.FavoriteTracksRepository
import bes.max.trackseeker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val trackDao: TrackDao,
    private val convertor: TrackDbMapper
) : FavoriteTracksRepository {
    override suspend fun addTrack(track: Track) {
        trackDao.insertTrack(convertor.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        trackDao.deleteTrack(convertor.map(track))
    }

    override fun getAll(): Flow<List<Track>> = flow {
        val tracks = trackDao.getAllFavoriteTracks().map { convertor.map(it) }
        emit(tracks)
    }

    override suspend fun getTrackById(id: Long): Track {
        val trackEntity = trackDao.getTrackById(id)
        return convertor.map(trackEntity)
    }

    override fun getAllIdsOfFavoriteTracks(): Flow<List<Long>> = flow {
        emit(trackDao.getAllIdsOfFavoriteTracks())
    }
}