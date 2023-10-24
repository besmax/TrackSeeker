package bes.max.trackseeker.domain.mediateka.playlist

import android.net.Uri
import bes.max.trackseeker.domain.models.Playlist
import bes.max.trackseeker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun getAllPlaylists() : Flow<List<Playlist>>

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun saveCover(uri: Uri): Uri

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) : Flow<Boolean>
}