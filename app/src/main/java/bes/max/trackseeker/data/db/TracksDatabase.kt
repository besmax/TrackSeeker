package bes.max.trackseeker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import bes.max.trackseeker.data.db.dao.FavoriteTracksDao
import bes.max.trackseeker.data.db.dao.PlaylistsDao
import bes.max.trackseeker.data.db.entities.PlaylistEntity
import bes.max.trackseeker.data.db.entities.TrackEntity

@Database(version = 2, entities = [TrackEntity::class, PlaylistEntity::class], exportSchema = false)
abstract class TracksDatabase : RoomDatabase() {

    abstract fun favoriteTracksDao(): FavoriteTracksDao

    abstract fun playlistDao(): PlaylistsDao
}