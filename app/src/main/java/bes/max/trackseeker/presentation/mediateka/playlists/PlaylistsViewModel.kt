package bes.max.trackseeker.presentation.mediateka.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bes.max.trackseeker.domain.mediateka.playlist.PlaylistInteractor
import bes.max.trackseeker.ui.mediateka.playlists.PlaylistScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.flowOn


class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<PlaylistScreenState>(PlaylistScreenState.Empty)
    val screenState: LiveData<PlaylistScreenState> = _screenState

    init {
        getPlaylists()
    }

    fun getPlaylists() {
        _screenState.value = PlaylistScreenState.Loading
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().flowOn(Dispatchers.Main).collect { playlists ->
                if (playlists.isNullOrEmpty()) {
                    _screenState.postValue(PlaylistScreenState.Empty)
                } else {
                    _screenState.postValue(PlaylistScreenState.Content(playlists))
                }
            }

        }
    }

}