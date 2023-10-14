package bes.max.trackseeker.data.search

import bes.max.trackseeker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryDao {

    fun saveTrack(track: Track)

    fun getHistoryTracks() : Flow<List<Track>>

    fun clearTracksHistory()

}