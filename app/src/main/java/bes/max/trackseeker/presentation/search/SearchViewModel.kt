package bes.max.trackseeker.presentation.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bes.max.trackseeker.domain.models.Resource
import bes.max.trackseeker.domain.models.Track
import bes.max.trackseeker.domain.search.SearchHistoryInteractor
import bes.max.trackseeker.domain.search.SearchInNetworkUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "SearchViewModel"

class SearchViewModel(
    private val searchInNetworkUseCase: SearchInNetworkUseCase,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val _screenState: MutableLiveData<SearchScreenState> =
        MutableLiveData(SearchScreenState.Default)
    val screenState: LiveData<SearchScreenState> = _screenState

    private var searchJob: Job? = null
    private var latestSearchText = ""


    fun searchDebounce(searchText: String) {
        if (searchJob?.isCompleted != true) {
            searchJob?.cancel()
        }

        if (latestSearchText != searchText) {
            latestSearchText = searchText
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                searchTrack(searchText)
            }
        }
    }

    fun cancelSearch() {
        searchJob?.cancel()
    }

    private fun searchTrack(searchRequest: String) {
        _screenState.value = SearchScreenState.Loading
        viewModelScope.launch {
            searchInNetworkUseCase.execute(searchRequest).collect { response ->
                when (response) {
                    is Resource.Error -> {
                        _screenState.postValue(SearchScreenState.SearchError)
                        Log.w(
                            TAG,
                            "Error in fun searchTrack in SearchViewModel: ${response.message}"
                        )
                    }

                    is Resource.Success -> {
                        if (response.data.isNullOrEmpty()) {
                            _screenState.postValue(SearchScreenState.TracksNotFound)
                            Log.w(
                                TAG,
                                "List from remote is empty in fun searchTrack in SearchViewModel"
                            )
                        } else {
                            SearchScreenState.Tracks.tracks = response.data
                            _screenState.postValue(SearchScreenState.Tracks)
                        }
                    }
                }

            }
        }
    }

    fun saveTrackToHistory(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            searchHistoryInteractor.saveTrackToHistory(track)
            withContext(Dispatchers.Main) {
                searchHistoryInteractor.getTracksFromHistory().collect() {
                    SearchScreenState.History.tracks = it
                }
                if (_screenState.value == SearchScreenState.History) {
                    _screenState.value = SearchScreenState.History
                }
            }
        }
    }

    fun showHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            searchHistoryInteractor.getTracksFromHistory().collect() {
                SearchScreenState.History.tracks = it
            }
            withContext(Dispatchers.Main) {
                _screenState.value = SearchScreenState.History
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            searchHistoryInteractor.clearHistory()
            withContext(Dispatchers.Main) {
                _screenState.value = SearchScreenState.Default
                SearchScreenState.History.tracks = emptyList()
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
