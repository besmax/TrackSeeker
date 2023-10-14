package bes.max.trackseeker.domain.search

import bes.max.trackseeker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryInteractor {

    fun saveTrackToHistory(track: Track)

    fun getTracksFromHistory() : Flow<List<Track>>

    fun clearHistory()
}