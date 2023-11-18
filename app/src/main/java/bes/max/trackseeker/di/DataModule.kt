package bes.max.trackseeker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import bes.max.trackseeker.data.db.AppDatabase
import bes.max.trackseeker.data.db.dao.PlaylistTrackDao
import bes.max.trackseeker.data.db.dao.PlaylistsDao
import bes.max.trackseeker.data.db.dao.TrackDao
import bes.max.trackseeker.data.mappers.TrackDbMapper
import bes.max.trackseeker.data.mappers.TrackDtoMapper
import bes.max.trackseeker.data.mediateka.ImageDaoImpl
import bes.max.trackseeker.data.network.ITunesSearchApiService
import bes.max.trackseeker.data.network.NetworkClient
import bes.max.trackseeker.data.network.RetrofitNetworkClient
import bes.max.trackseeker.data.search.SearchHistoryDao
import bes.max.trackseeker.data.search.SearchHistoryDaoImpl
import bes.max.trackseeker.data.settings.ExternalNavigatorImpl
import bes.max.trackseeker.data.settings.SettingsDao
import bes.max.trackseeker.data.settings.SettingsDaoImpl
import bes.max.trackseeker.domain.mediateka.playlist.ImageDao
import bes.max.trackseeker.domain.settings.ExternalNavigator
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val SETTINGS_PREFERENCES = "settings_preferences"
private const val SHARED_PREF_KEY = "search_history_preferences_key"
private const val ITUNES_BASE_URL = "https://itunes.apple.com"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SETTINGS_PREFERENCES
)

val dataModule = module {

    factoryOf(::Gson)

    single<DataStore<Preferences>> {
        androidContext().dataStore
    }

    singleOf(::RetrofitNetworkClient) bind NetworkClient::class

    single<SharedPreferences> {
        androidContext().getSharedPreferences(SHARED_PREF_KEY, 0)
    }

    singleOf(::SearchHistoryDaoImpl) bind SearchHistoryDao::class

    factoryOf(::TrackDtoMapper)

    factoryOf(::ExternalNavigatorImpl) bind ExternalNavigator::class

    singleOf(::SettingsDaoImpl) bind SettingsDao::class

    singleOf(::ImageDaoImpl) bind ImageDao::class

    single<ITunesSearchApiService> {
        Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchApiService::class.java)
    }

    single {
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<TrackDao> {
        val database = get<AppDatabase>()
        database.trackDao()
    }

    single<PlaylistsDao> {
        val database = get<AppDatabase>()
        database.playlistsDao()
    }

    single<PlaylistTrackDao> {
        val database = get<AppDatabase>()
        database.playlistTrackDao()
    }

    factoryOf(::TrackDbMapper)

}