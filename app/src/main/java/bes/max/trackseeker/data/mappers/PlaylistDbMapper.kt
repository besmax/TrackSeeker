package bes.max.trackseeker.data.mappers

import bes.max.trackseeker.data.db.entities.PlaylistEntity
import bes.max.trackseeker.domain.models.Playlist

object PlaylistDbMapper {

    fun map(playlistEntity: PlaylistEntity): Playlist =
        Playlist(
            id = playlistEntity.id,
            name = playlistEntity.name,
            description = playlistEntity.description,
            coverPath = playlistEntity.coverPath,
            tracks = null,
            tracksNumber = playlistEntity.tracksNumber
        )

    fun map(playlist: Playlist): PlaylistEntity =
        PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverPath = playlist.coverPath,
            tracksNumber = playlist.tracksNumber
        )

}