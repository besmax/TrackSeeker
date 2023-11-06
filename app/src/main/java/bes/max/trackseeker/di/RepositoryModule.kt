package bes.max.trackseeker.di

import bes.max.trackseeker.data.mediateka.FavoriteTracksRepositoryImpl
import bes.max.trackseeker.data.mediateka.PlaylistRepositoryImpl
import bes.max.trackseeker.data.player.PlayerImpl
import bes.max.trackseeker.data.search.TracksRepositoryImpl
import bes.max.trackseeker.data.settings.SettingsRepositoryImpl
import bes.max.trackseeker.domain.mediateka.favorite.FavoriteTracksRepository
import bes.max.trackseeker.domain.mediateka.playlist.PlaylistRepository
import bes.max.trackseeker.domain.player.Player
import bes.max.trackseeker.domain.search.TracksRepository
import bes.max.trackseeker.domain.settings.SettingsRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {

    singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class

    singleOf(::TracksRepositoryImpl) bind TracksRepository::class

    singleOf(::PlayerImpl) bind Player::class

    singleOf(::FavoriteTracksRepositoryImpl) bind FavoriteTracksRepository::class

    singleOf(::PlaylistRepositoryImpl) bind PlaylistRepository::class

}