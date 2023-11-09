package bes.max.trackseeker.ui.player

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import bes.max.trackseeker.domain.models.PlayerState
import bes.max.trackseeker.domain.models.Playlist
import bes.max.trackseeker.domain.models.Track
import bes.max.trackseeker.presentation.player.PlayerViewModel
import bes.max.trackseeker.presentation.utils.GsonTrackConverter
import bes.max.trackseeker.ui.navigation.Screen
import bes.max.trackseeker.ui.theme.YpGray
import bes.max.trackseeker.ui.theme.ysDisplayFamily
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    track: Track,
    playerViewModel: PlayerViewModel = koinViewModel {
        parametersOf(track)
    },
    navController: NavController
) {
    val playerState by playerViewModel.playerState.observeAsState(PlayerState.STATE_DEFAULT)
    val playingTime by playerViewModel.playingTime.observeAsState("00:00")
    val isFavorite by playerViewModel.isFavorite.observeAsState(false)
    val playlists by playerViewModel.playlists.observeAsState(emptyList())

    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            playerViewModel.releasePlayer()
        }
    }

    PlayerScreenContent(
        playerState = playerState,
        playingTime = playingTime,
        isFavorite = isFavorite,
        track = track,
        navigateBack = { navController.popBackStack() },
        playbackControl = { playerViewModel.playbackControl() },
        addOrDeleteFromFavorite = {
            if (isFavorite) playerViewModel.deleteFromFavorite(track)
            else playerViewModel.addToFavorite(track)
        },
        addToPlaylist = { playlist ->
            playerViewModel.addTrackToPlaylist(playlist)
        },
        openBottomSheet = {
            isSheetOpen = true
        }
    )

    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { isSheetOpen = false }
        ) {
            PlayerBottomSheetContent(
                playlists = playlists,
                createNewPlaylist = {
                    val trackArg = GsonTrackConverter.convertTrackToJson(track)
                    var encodeTrackArg = Uri.encode(trackArg)
                    navController.navigate(
                        Screen.NewPlaylistScreen.route.replace(
                            "{track}",
                            encodeTrackArg
                        )
                    )
                },
                addToPlaylist = { playlist ->
                    playerViewModel.addTrackToPlaylist(playlist)
                }
            )
        }
    }

}

@Composable
fun PlayerScreenContent(
    playerState: PlayerState,
    playingTime: String,
    isFavorite: Boolean,
    track: Track,
    navigateBack: () -> Unit,
    playbackControl: () -> Unit,
    addOrDeleteFromFavorite: () -> Unit,
    addToPlaylist: (Playlist) -> Unit,
    openBottomSheet: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        IconButton(
            onClick = { navigateBack() },
            modifier = Modifier
                .padding(all = 12.dp)
                .align(Alignment.Start),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Back arrow",
            )
        }
        AsyncImage(
            model = track.bigCover,
            contentDescription = track.trackName,
            modifier = Modifier
                .padding(vertical = 24.dp)
                .width(312.dp)
                .height(312.dp)
                .clip(RoundedCornerShape(8.dp)),
            error = painterResource(id = R.drawable.ic_picture_not_found),
            placeholder = painterResource(id = R.drawable.ic_picture_not_found),
        )

        Text(
            text = track.trackName,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(start = 24.dp, bottom = 12.dp, end = 24.dp)
                .align(Alignment.Start)
        )

        Text(
            text = track.artistName,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .align(Alignment.Start)
        )

        Row(modifier = Modifier) {
            IconButton(
                onClick = { openBottomSheet() },
                modifier = Modifier
                    .padding(top = 54.dp)
                    .size(51.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_player_add),
                    contentDescription = "Add to playlist button",
                    tint = Color.Unspecified
                )
            }

            IconButton(
                onClick = { playbackControl() },
                modifier = Modifier
                    .padding(start = 56.dp, end = 56.dp, top = 30.dp)
                    .size(100.dp)
            ) {
                Icon(
                    painter = when (playerState) {
                        PlayerState.STATE_DEFAULT, PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                            painterResource(id = R.drawable.ic_player_play)
                        }

                        PlayerState.STATE_PLAYING -> painterResource(id = R.drawable.ic_player_pause)
                    },
                    contentDescription = "Play-pause button",
                    tint = Color.Unspecified
                )
            }

            IconButton(
                onClick = { addOrDeleteFromFavorite() },
                modifier = Modifier
                    .padding(top = 54.dp)
                    .size(51.dp)
            ) {
                Icon(
                    painter = if (isFavorite) painterResource(id = R.drawable.ic_player_like_active)
                    else painterResource(id = R.drawable.ic_player_like),
                    contentDescription = "Add to favorite tracks button",
                    tint = Color.Unspecified
                )
            }
        }

        Text(
            text = playingTime,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        TrackInfo(stringResource(id = R.string.duration), track.trackTime)
        TrackInfo(stringResource(id = R.string.album), track.collectionName)
        TrackInfo(stringResource(id = R.string.year), track.year)
        TrackInfo(stringResource(id = R.string.genre), track.primaryGenreName)
        TrackInfo(stringResource(id = R.string.country), track.country)
    }
}

@Composable
fun TrackInfo(title: String, content: String) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            color = YpGray,
            textAlign = TextAlign.Center
        )
        Text(
            text = content,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            maxLines = 1
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerBottomSheetContent(
    playlists: List<Playlist>,
    createNewPlaylist: () -> Unit,
    addToPlaylist: (Playlist) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.add_to_playlist),
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 19.sp,
            modifier = Modifier
                .padding(vertical = 28.dp, )

        )

        Button(
            onClick = { createNewPlaylist() },
            modifier = Modifier
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background
            )
        ) {
            Text(
                text = stringResource(id = R.string.new_playlist),
                fontFamily = ysDisplayFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }

        PlaylistsRowList(playlists, addToPlaylist)

    }
}

@Composable
fun PlaylistsRowList(
    playlists: List<Playlist>,
    onItemClick: (Playlist) -> Unit,
    isReverse: Boolean = false
) {
    LazyColumn(
        reverseLayout = isReverse,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        items(
            items = playlists,
            key = { playlist -> playlist.id }
        ) { playlist ->
            PlaylistRowListItem(playlist, onItemClick)
        }
    }
}

@Composable
fun PlaylistRowListItem(
    playlist: Playlist,
    onItemClick: (Playlist) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onItemClick(playlist) }
    ) {
        AsyncImage(
            model = playlist.coverPath,
            contentDescription = playlist.name,
            error = painterResource(id = R.drawable.playlist_placeholder_grid),
            placeholder = painterResource(id = R.drawable.playlist_placeholder_grid),
            modifier = Modifier
                .width(45.dp)
                .height(45.dp)
                .clip(RoundedCornerShape(2.dp)),
        )

        Column {
            Text(
                text = playlist.name,
                fontFamily = ysDisplayFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = pluralStringResource(
                    id = R.plurals.tracks_number,
                    count = playlist.tracksNumber,
                ),
                fontFamily = ysDisplayFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = YpGray
            )
        }
    }
}

@Composable
@Preview
fun PlaylistRowListItemPreview() {
    PlaylistRowListItem(
        Playlist(
            name = "Name",
            description = "Descr",
            coverPath = null,
            tracks = emptyList(),
            tracksNumber = 4
        ),
        { }
    )
}
