package bes.max.trackseeker.domain.mediateka.favorite

import bes.max.trackseeker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {


    suspend fun addTrack(track: Track)

    suspend fun deleteTrack(track: Track)

    fun getAll(): Flow<List<Track>>

    suspend fun getTrackById(id: Long): Track

    fun getAllIdsOfFavoriteTracks(): Flow<List<Long>>

}