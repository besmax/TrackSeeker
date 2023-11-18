package bes.max.trackseeker.presentation.mediateka.playlistdetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bes.max.trackseeker.domain.mediateka.playlist.PlaylistInteractor
import bes.max.trackseeker.domain.mediateka.playlistdetails.SharePlaylistUseCase
import bes.max.trackseeker.domain.models.Playlist
import bes.max.trackseeker.ui.mediateka.playlistdetails.PlaylistDetailsScreenState
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val playlistInteractor: PlaylistInteractor,
    private val sharePlaylistUseCase: SharePlaylistUseCase
) : ViewModel() {

    private val _screenState: MutableLiveData<PlaylistDetailsScreenState> =
        MutableLiveData(PlaylistDetailsScreenState.Default)
    val screenState: LiveData<PlaylistDetailsScreenState> = _screenState
    private val playlistId = savedStateHandle.get<Long>("playlistId")!!

    init {
        if (playlistId != null) {
            getPlaylist(playlistId)
        }
    }

    fun getPlaylist(id: Long = playlistId) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(id).collect() { playlist ->
                var durationSum = 0L
                playlist.tracks?.forEach { durationSum += it.trackTimeMillis }
                val playlistDetails = PlaylistDetails(
                    title = playlist.name,
                    description = playlist.description ?: "",
                    tracks = playlist.tracks ?: emptyList(),
                    durationSum = durationSum,
                    cover = playlist.coverPath,
                    playlist = playlist
                )
                _screenState.postValue(PlaylistDetailsScreenState.Content(playlistDetails))
            }
        }
    }

    fun deleteTrackFromPlaylist(trackId: Long) {
        _screenState.value = PlaylistDetailsScreenState.Editing
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(trackId = trackId, playlistId = playlistId)
            getPlaylist(playlistId)
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlist)
        }
    }

    fun sharePlaylist(playlist: Playlist) {
        try {
            sharePlaylistUseCase.execute(playlist)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    fun showMenu() {
        if (_screenState.value is PlaylistDetailsScreenState.Content) {
            val playlist =
                (_screenState.value as PlaylistDetailsScreenState.Content).playlistDetails.playlist
            _screenState.value = PlaylistDetailsScreenState.Menu(playlist = playlist)
        }
    }

    fun onCloseMenu() {
        if (_screenState.value is PlaylistDetailsScreenState.Menu) {
            getPlaylist(playlistId)
        }
    }

    companion object {
        private const val TAG = "PlaylistDetailsViewModel"
    }

}