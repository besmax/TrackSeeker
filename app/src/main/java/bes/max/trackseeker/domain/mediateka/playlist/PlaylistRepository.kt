package bes.max.trackseeker.domain.mediateka.playlist

import android.net.Uri
import bes.max.trackseeker.domain.models.Playlist
import bes.max.trackseeker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    fun getAllPlaylists() : Flow<List<Playlist>>

    suspend fun saveCover(uri: Uri) : Uri

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) : Flow<Boolean>


}