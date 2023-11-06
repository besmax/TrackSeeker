package bes.max.trackseeker.di

import bes.max.trackseeker.domain.mediateka.favorite.FavoriteTracksInteractor
import bes.max.trackseeker.domain.mediateka.favorite.FavoriteTracksInteractorImpl
import bes.max.trackseeker.domain.mediateka.playlist.PlaylistInteractor
import bes.max.trackseeker.domain.mediateka.playlist.PlaylistInteractorImpl
import bes.max.trackseeker.domain.mediateka.playlistdetails.SharePlaylistUseCase
import bes.max.trackseeker.domain.player.PlayerInteractor
import bes.max.trackseeker.domain.player.PlayerInteractorImpl
import bes.max.trackseeker.domain.search.SearchHistoryInteractor
import bes.max.trackseeker.domain.search.SearchHistoryInteractorImpl
import bes.max.trackseeker.domain.search.SearchInNetworkUseCase
import bes.max.trackseeker.domain.settings.SettingsInteractor
import bes.max.trackseeker.domain.settings.SettingsInteractorImpl
import bes.max.trackseeker.domain.settings.SharingInteractor
import bes.max.trackseeker.domain.settings.SharingInteractorImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {

    factoryOf(::PlayerInteractorImpl) bind PlayerInteractor::class

    factoryOf(::SearchHistoryInteractorImpl) bind SearchHistoryInteractor::class

    factoryOf(::SearchInNetworkUseCase)

    factoryOf(::SettingsInteractorImpl) bind SettingsInteractor::class

    factoryOf(::SharingInteractorImpl) bind SharingInteractor::class

    factoryOf(::FavoriteTracksInteractorImpl) bind FavoriteTracksInteractor::class

    factoryOf(::PlaylistInteractorImpl) bind PlaylistInteractor::class

    factoryOf(::SharePlaylistUseCase)

}