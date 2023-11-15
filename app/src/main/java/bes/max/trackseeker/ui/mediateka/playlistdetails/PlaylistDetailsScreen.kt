package bes.max.trackseeker.ui.mediateka.playlistdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import bes.max.trackseeker.R
import bes.max.trackseeker.presentation.mediateka.playlistdetails.PlaylistDetails
import bes.max.trackseeker.presentation.mediateka.playlistdetails.PlaylistDetailsScreenState
import bes.max.trackseeker.presentation.mediateka.playlistdetails.PlaylistDetailsViewModel
import bes.max.trackseeker.ui.theme.YpBlack
import bes.max.trackseeker.ui.theme.YpGray
import bes.max.trackseeker.ui.theme.ysDisplayFamily
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistDetailsScreen(
    navController: NavController,
    playlistDetailsViewModel: PlaylistDetailsViewModel = koinViewModel()
) {

    val screenState by playlistDetailsViewModel.screenState.observeAsState(
        PlaylistDetailsScreenState.Default
    )

    if (screenState is PlaylistDetailsScreenState.Content) {
        PlaylistDetailsScreenContent(screenState as PlaylistDetailsScreenState.Content)
    }


}

@Composable
fun PlaylistDetailsScreenContent(
    screenState: PlaylistDetailsScreenState.Content
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AsyncImage(
            model = screenState.playlistDetails.cover?.toUri(),
            contentDescription = "playlist cover",
            error = painterResource(id = R.drawable.playlist_placeholder_grid),
            placeholder = painterResource(id = R.drawable.playlist_placeholder_grid),
            modifier = Modifier.aspectRatio(1f),
            contentScale = ContentScale.FillBounds
        )

        Text(
            text = screenState.playlistDetails.title,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            color = YpGray,
            modifier = Modifier
                .padding(start = 16.dp, top = 24.dp)
        )

        Text(
            text = screenState.playlistDetails.description,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            color = YpGray,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            Text(
                text = pluralStringResource(
                    id = R.plurals.minutes_number,
                    count = screenState.playlistDetails.duration,
                    screenState.playlistDetails.duration
                ),
                fontFamily = ysDisplayFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = YpGray
            )
            Icon(
                painterResource(id = R.drawable.ic_circle_between_text),
                contentDescription = "Text separator",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(top = 10.dp, start = 2.dp, end = 2.dp)
            )

            Text(
                text = pluralStringResource(
                    id = R.plurals.tracks_number,
                    count = screenState.playlistDetails.tracksNumber,
                    screenState.playlistDetails.tracksNumber
                ),
                fontFamily = ysDisplayFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = YpGray
            )

        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painterResource(id = R.drawable.ic_share),
                    contentDescription = "Text separator",
                    tint = YpBlack
                )
            }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painterResource(id = R.drawable.ic_more_vert),
                    contentDescription = "Text separator",
                    tint = YpBlack
                )
            }
        }
    }

}
