package bes.max.trackseeker.di

import bes.max.trackseeker.presentation.mediateka.MediatekaViewModel
import bes.max.trackseeker.presentation.mediateka.editplaylist.EditPlaylistViewModel
import bes.max.trackseeker.presentation.mediateka.favorite.FavoriteTracksViewModel
import bes.max.trackseeker.presentation.mediateka.newplaylist.NewPlaylistViewModel
import bes.max.trackseeker.presentation.mediateka.playlistdetails.PlaylistDetailsViewModel
import bes.max.trackseeker.presentation.mediateka.playlists.PlaylistsViewModel
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

    viewModelOf(::PlaylistsViewModel)

    viewModelOf(::MediatekaViewModel)

    viewModelOf(::NewPlaylistViewModel)

    viewModelOf(::PlaylistDetailsViewModel)

    viewModelOf(::EditPlaylistViewModel)

}