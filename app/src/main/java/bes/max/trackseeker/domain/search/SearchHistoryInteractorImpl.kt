package bes.max.trackseeker.domain.search

import bes.max.trackseeker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class SearchHistoryInteractorImpl(
    private val trackRepository: TracksRepository
) : SearchHistoryInteractor {

    override fun saveTrackToHistory(track: Track) {
        trackRepository.saveTrackInHistory(track)
    }

    override fun getTracksFromHistory(): Flow<List<Track>> =
        trackRepository.getTracksFromHistory()


    override fun clearHistory() {
        trackRepository.clearHistory()
    }
}