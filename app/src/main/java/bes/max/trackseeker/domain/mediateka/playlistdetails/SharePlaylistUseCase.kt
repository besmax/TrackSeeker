package bes.max.trackseeker.domain.mediateka.playlistdetails

import android.content.Context
import bes.max.trackseeker.R
import bes.max.trackseeker.domain.models.Playlist
import bes.max.trackseeker.domain.settings.ExternalNavigator

class SharePlaylistUseCase(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
) {

    fun execute(playlist: Playlist) {
        val playlistString = StringBuilder("")
        playlistString.append(playlist.name)
        playlistString.append(System.lineSeparator())
        playlistString.append(playlist.description)
        playlistString.append(System.lineSeparator())
        playlistString.append(
            context.getString(
                R.string.number_of_tracks,
                playlist.tracksNumber.toString()
            )
        )
        playlistString.append(System.lineSeparator())
        playlist.tracks?.forEach { track ->
            var number = 1
            playlistString.append("$number. ${track.artistName} - ${track.trackName} (${track.trackTime})")
            playlistString.append(System.lineSeparator())
            number++
        }
        externalNavigator.sharePlaylist(playlistString.toString().trim())
    }
}