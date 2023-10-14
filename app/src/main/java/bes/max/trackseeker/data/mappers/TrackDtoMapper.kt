package bes.max.trackseeker.data.mappers

import bes.max.trackseeker.data.dto.TrackDto
import bes.max.trackseeker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackDtoMapper {

    fun trackDtoToTrack(trackDto: TrackDto) : Track {
        return Track(
            trackId = trackDto.trackId,
            trackName = trackDto.trackName,
            artistName = trackDto.artistName,
            trackTimeMillis = trackDto.trackTimeMillis,
            artworkUrl100 = trackDto.artworkUrl100,
            collectionName = trackDto.collectionName,
            releaseDate = trackDto.releaseDate,
            primaryGenreName = trackDto.primaryGenreName,
            country = trackDto.country,
            previewUrl = trackDto.previewUrl,
            trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackDto.trackTimeMillis),
            bigCover = trackDto.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
            year = trackDto.releaseDate?.take(4) ?: "0000"
        )
    }
}