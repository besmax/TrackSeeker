package bes.max.trackseeker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import bes.max.trackseeker.data.db.dao.PlaylistTrackDao
import bes.max.trackseeker.data.db.dao.PlaylistsDao
import bes.max.trackseeker.data.db.dao.TrackDao
import bes.max.trackseeker.data.db.entities.PlaylistEntity
import bes.max.trackseeker.data.db.entities.PlaylistTrackEntity
import bes.max.trackseeker.data.db.entities.TrackEntity

@Database(
    version = 1,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playlistsDao(): PlaylistsDao

    abstract fun trackDao(): TrackDao

    abstract fun playlistTrackDao(): PlaylistTrackDao
}