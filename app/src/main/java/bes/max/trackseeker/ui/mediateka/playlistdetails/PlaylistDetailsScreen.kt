package bes.max.trackseeker.ui.mediateka.playlistdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.net.toUri
import bes.max.trackseeker.R
import bes.max.trackseeker.presentation.mediateka.playlistdetails.PlaylistDetails
import bes.max.trackseeker.presentation.mediateka.playlistdetails.PlaylistDetailsScreenState
import bes.max.trackseeker.presentation.mediateka.playlistdetails.PlaylistDetailsViewModel
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistDetailsScreen(
    playlistDetailsViewModel: PlaylistDetailsViewModel = koinViewModel()
) {

    val screenState by playlistDetailsViewModel.screenState.observeAsState(
        PlaylistDetailsScreenState.Default
    )

}

@Composable
fun PlaylistDetailsScreenContent(
    playlistDetails: PlaylistDetails
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AsyncImage(
            model = playlistDetails.cover?.toUri(),
            contentDescription = "playlist cover",
            error = painterResource(id = R.drawable.playlist_placeholder_grid),
            placeholder = painterResource(id = R.drawable.playlist_placeholder_grid),
            modifier = Modifier.aspectRatio(1f),
            contentScale = ContentScale.FillBounds

        )
    }

}
