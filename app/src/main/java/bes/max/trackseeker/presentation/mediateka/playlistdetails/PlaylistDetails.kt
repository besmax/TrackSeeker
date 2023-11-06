package bes.max.trackseeker.presentation.mediateka.playlistdetails

import bes.max.trackseeker.domain.models.Playlist
import bes.max.trackseeker.domain.models.Track

data class PlaylistDetails(
    val title: String,
    val description: String,
    val tracks: List<Track>,
    val durationSum: Long,
    val cover: String?,
    val playlist: Playlist
) {
    val duration: Int get() = (durationSum / 60000).toInt()

    val tracksNumber get() = tracks.size
}
