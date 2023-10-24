package bes.max.trackseeker.di

import bes.max.trackseeker.presentation.mediateka.MediatekaViewModel
import bes.max.trackseeker.presentation.mediateka.NewPlaylistViewModel
import bes.max.trackseeker.presentation.mediateka.favorite.FavoriteTracksViewModel
import bes.max.trackseeker.presentation.mediateka.playlist.PlaylistViewModel
import bes.max.trackseeker.presentation.player.PlayerViewModel
import bes.max.trackseeker.presentation.search.SearchViewModel
import bes.max.trackseeker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {

    viewModelOf(::PlayerViewModel)

    viewModelOf(::SearchViewModel)

    viewModelOf(::SettingsViewModel)

    viewModelOf(::FavoriteTracksViewModel)

    viewModelOf(::PlaylistViewModel)

    viewModelOf(::MediatekaViewModel)

    viewModelOf(::NewPlaylistViewModel)

}