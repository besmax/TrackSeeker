package bes.max.trackseeker.data.mappers

import bes.max.trackseeker.data.db.entities.TrackEntity
import bes.max.trackseeker.domain.models.Track

class TrackDbMapper {

    fun map(track: Track): TrackEntity =
        TrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl ?: "",
            trackTime = track.trackTime,
            bigCover = track.bigCover,
            year = track.year,
            addingDate = System.currentTimeMillis()
        )

    fun map(track: TrackEntity): Track =
        Track(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = 0L,
            artworkUrl100 = "",
            collectionName = track.collectionName,
            releaseDate = track.year,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            trackTime = track.trackTime,
            bigCover = track.bigCover,
            year = track.year
        )
}