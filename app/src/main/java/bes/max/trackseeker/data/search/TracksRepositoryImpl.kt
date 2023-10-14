package bes.max.trackseeker.data.search

import bes.max.trackseeker.data.dto.TrackSearchRequest
import bes.max.trackseeker.data.dto.TrackSearchResponse
import bes.max.trackseeker.data.mappers.TrackDtoMapper
import bes.max.trackseeker.data.network.NetworkClient
import bes.max.trackseeker.domain.models.Resource
import bes.max.trackseeker.domain.models.Track
import bes.max.trackseeker.domain.search.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchHistoryDao: SearchHistoryDao,
    private val trackDtoMapper: TrackDtoMapper
) : TracksRepository {

    override suspend fun searchTracks(searchRequest: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.searchTracks(TrackSearchRequest(searchRequest))
        when (response.resultCode) {
            200 -> {
                val data = (response as TrackSearchResponse).results.map {
                    trackDtoMapper.trackDtoToTrack(it)
                }
                emit(Resource.Success(data = data))
            }

            -1 -> emit(Resource.Error(message = "No Internet"))
            else -> emit(Resource.Error(message = "Server error"))
        }
    }

    override fun saveTrackInHistory(track: Track) {
        searchHistoryDao.saveTrack(track)
    }

    override fun getTracksFromHistory(): Flow<List<Track>> {
        return searchHistoryDao.getHistoryTracks()
    }

    override fun clearHistory() {
        searchHistoryDao.clearTracksHistory()
    }

}