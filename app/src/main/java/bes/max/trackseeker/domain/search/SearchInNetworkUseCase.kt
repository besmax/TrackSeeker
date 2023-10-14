package bes.max.trackseeker.domain.search

import bes.max.trackseeker.domain.models.Resource
import bes.max.trackseeker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class SearchInNetworkUseCase(private val tracksRepository: TracksRepository) {

    suspend fun execute(searchRequest: String): Flow<Resource<List<Track>>> {
        return tracksRepository.searchTracks(searchRequest)
    }

}