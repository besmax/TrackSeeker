package bes.max.trackseeker.ui.mediateka.playlists

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bes.max.trackseeker.R
import bes.max.trackseeker.domain.models.Playlist
import bes.max.trackseeker.presentation.mediateka.playlists.PlaylistsViewModel
import bes.max.trackseeker.ui.Loading
import bes.max.trackseeker.ui.TrackListItem
import bes.max.trackseeker.ui.mediateka.favorite.FavoriteScreenState
import bes.max.trackseeker.ui.navigation.Screen
import bes.max.trackseeker.ui.theme.ysDisplayFamily
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistsScreen(
    navController: NavController,
    playlistsViewModel: PlaylistsViewModel = koinViewModel()
) {

    val uiState by playlistsViewModel.screenState.observeAsState(initial = PlaylistScreenState.Empty)

    LaunchedEffect(key1 = Unit) {
        playlistsViewModel.getPlaylists()
    }

    PlaylistsScreenContent(
        uiState = uiState,
        addPlaylist = { navController.navigate(Screen.NewPlaylistScreen.route) }
    )
}

@Composable
fun PlaylistsScreenContent(
    uiState: PlaylistScreenState,
    addPlaylist: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = { addPlaylist() },
            modifier = Modifier
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text(
                text = stringResource(id = R.string.playlist_placeholder_button),
                fontFamily = ysDisplayFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }

        when (uiState) {
            is PlaylistScreenState.Empty -> NoPlaylists()

            is PlaylistScreenState.Content -> ShowPlaylists(
                playlists = uiState.playlists,
                onItemClick = { },
            )

            is PlaylistScreenState.Loading -> Loading()
        }
    }

}

@Composable
fun ShowPlaylists(
    playlists: List<Playlist>,
    onItemClick: (Playlist) -> Unit,
    isReverse: Boolean = false
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        reverseLayout = isReverse,
        modifier = Modifier
            .padding(start = 12.dp, top = 16.dp, end = 12.dp)
    ) {
        items(
            items = playlists,
            key = { playlist -> playlist.id }
        ) { playlist ->
            PlaylistGridListItem(playlist, onItemClick)
        }
    }
}

@Composable
fun PlaylistGridListItem(
    playlist: Playlist,
    onItemClick: (Playlist) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onItemClick(playlist) }
    ) {

        AsyncImage(
            model = playlist.coverPath,
            contentDescription = playlist.name,
            error = painterResource(id = R.drawable.playlist_placeholder_grid),
            placeholder = painterResource(id = R.drawable.playlist_placeholder_grid),
            modifier = Modifier
                .width(160.dp)
                .height(160.dp)
                .clip(RoundedCornerShape(8.dp)),
        )

        Text(
            text = playlist.name,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            textAlign = TextAlign.Start
        )

        Text(
            text = pluralStringResource(
                id = R.plurals.tracks_number,
                playlist.tracks?.size ?: 0,
                playlist.tracks?.size ?: 0
            ),
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
@Preview
fun PlaylistGridListItemPreview() {
    PlaylistGridListItem(
        Playlist(
            id = 12444,
            name = "Name",
            description = "Description",
            coverPath = null,
            tracks = emptyList()
        ),
        onItemClick = { }
    )
}


@Composable
fun NoPlaylists() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 46.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_not_found),
            contentDescription = "Playlists not found",
        )
        Text(
            text = stringResource(id = R.string.playlist_placeholder_text),
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 19.sp,
            textAlign = TextAlign.Center
        )
    }
}