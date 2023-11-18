package bes.max.trackseeker.ui.mediateka.playlistdetails

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import bes.max.trackseeker.R
import bes.max.trackseeker.domain.models.Playlist
import bes.max.trackseeker.domain.models.Track
import bes.max.trackseeker.presentation.mediateka.playlistdetails.PlaylistDetailsViewModel
import bes.max.trackseeker.presentation.utils.GsonTrackConverter
import bes.max.trackseeker.ui.PlaylistRowListItem
import bes.max.trackseeker.ui.TrackList
import bes.max.trackseeker.ui.navigation.Screen
import bes.max.trackseeker.ui.theme.YpBlack
import bes.max.trackseeker.ui.theme.YpBlue
import bes.max.trackseeker.ui.theme.YpLightGray
import bes.max.trackseeker.ui.theme.ysDisplayFamily
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailsScreen(
    navController: NavController,
    playlistDetailsViewModel: PlaylistDetailsViewModel = koinViewModel(),
) {

    val screenState by playlistDetailsViewModel.screenState.observeAsState(
        PlaylistDetailsScreenState.Default
    )

    val scaffoldState = rememberBottomSheetScaffoldState()

    val bottomSheetMenuState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheetMenu by remember { mutableStateOf(false) }

    var showDeletePlaylistDialog by remember {
        mutableStateOf(false)
    }

    var showDeleteTrackDialog by remember {
        mutableStateOf(false)
    }

    var trackIdForDelete by remember {
        mutableLongStateOf(0)
    }

    Box(modifier = Modifier.background(color = YpLightGray)) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                TracksBottomSheetContent(
                    tracks = (screenState as? PlaylistDetailsScreenState.Content)?.playlistDetails?.tracks
                        ?: emptyList(),
                    onItemClick = { track ->
                        val trackArg = GsonTrackConverter.convertTrackToJson(track)
                        val encodeTrackArg = Uri.encode(trackArg)
                        navController.navigate(
                            Screen.PlayerScreen.route.replace(
                                "{track}",
                                encodeTrackArg
                            )
                        )
                    },
                    onLongItemClick = { trackId ->
                        trackIdForDelete = trackId
                        showDeleteTrackDialog = true
                    }
                )
            },
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetSwipeEnabled = true,
            sheetPeekHeight = 200.dp,
            sheetContainerColor = MaterialTheme.colorScheme.background,
            containerColor = YpLightGray,
        ) {

            if (screenState is PlaylistDetailsScreenState.Content) {
                PlaylistDetailsScreenContent(
                    screenState = screenState as PlaylistDetailsScreenState.Content,
                    navigateBack = { navController.navigateUp() },
                    sharePlaylist = {
                        playlistDetailsViewModel.sharePlaylist((screenState as PlaylistDetailsScreenState.Content).playlistDetails.playlist)
                    },
                    openMenu = {
                        scope.launch {
                            showBottomSheetMenu = true
                        }
                    },
                    modifier = Modifier.clickable {
                        showBottomSheetMenu = false
                        scope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                        }
                    }
                )
            }

            if (showBottomSheetMenu) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheetMenu = false
                    },
                    sheetState = bottomSheetMenuState,
                    content = {
                        MenuBottomSheetContent(
                            playlist = (screenState as PlaylistDetailsScreenState.Content).playlistDetails.playlist,
                            sharePlaylist = {
                                playlistDetailsViewModel.sharePlaylist(
                                    (screenState as PlaylistDetailsScreenState.Content).playlistDetails.playlist
                                )
                            },
                            editPlaylist = {
                                navController.navigate(
                                    Screen.EditPlaylistScreen.route.replace(
                                        "{playlistId}",
                                        (screenState as PlaylistDetailsScreenState.Content).playlistDetails.playlist.id.toString()
                                    )
                                )
                            },
                            deletePlaylist = { showDeletePlaylistDialog = true })
                    }
                )
            }

            if (showDeletePlaylistDialog) {
                DeleteDialog(
                    onDismissRequest = { showDeletePlaylistDialog = false },
                    onConfirm = {
                        playlistDetailsViewModel.deletePlaylist(
                            (screenState as PlaylistDetailsScreenState.Content).playlistDetails.playlist
                        )
                        navController.navigateUp()
                    },
                    titleResId = R.string.delete_playlist,
                    textResId = R.string.want_delete_playlist
                )
            }

            if (showDeleteTrackDialog) {
                DeleteDialog(
                    onDismissRequest = { showDeleteTrackDialog = false },
                    onConfirm = {
                        playlistDetailsViewModel.deleteTrackFromPlaylist(trackIdForDelete)
                        showDeleteTrackDialog = false
                    },
                    titleResId = R.string.playlistdetails_screen_dialog_title,
                    textResId = R.string.playlistdetails_screen_dialog_message
                )
            }
        }
    }

}

@Composable
fun PlaylistDetailsScreenContent(
    screenState: PlaylistDetailsScreenState.Content,
    navigateBack: () -> Unit,
    sharePlaylist: () -> Unit,
    openMenu: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = YpLightGray)
    ) {

        Box() {

            AsyncImage(
                model = screenState.playlistDetails.cover?.toUri(),
                contentDescription = "playlist cover",
                error = painterResource(id = R.drawable.playlist_placeholder_grid),
                placeholder = painterResource(id = R.drawable.playlist_placeholder_grid),
                modifier = Modifier.aspectRatio(1f),
                contentScale = ContentScale.FillBounds
            )

            IconButton(
                onClick = { navigateBack() },
                modifier = Modifier
                    .padding(all = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back arrow",
                )
            }
        }

        Text(
            text = screenState.playlistDetails.title,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = YpBlack,
            modifier = Modifier
                .padding(start = 16.dp, top = 24.dp)
        )

        Text(
            text = screenState.playlistDetails.description,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            color = YpBlack,
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
                color = YpBlack
            )

            Icon(
                painterResource(id = R.drawable.ic_circle_between_text),
                contentDescription = "Text separator",
                tint = YpBlack,
                modifier = Modifier
                    .padding(top = 10.dp, start = 3.dp, end = 3.dp)
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
                color = YpBlack
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
        ) {
            IconButton(onClick = { sharePlaylist() }) {
                Icon(
                    painterResource(id = R.drawable.ic_share),
                    contentDescription = "Text separator",
                    tint = YpBlack
                )
            }

            IconButton(onClick = { openMenu() }) {
                Icon(
                    painterResource(id = R.drawable.ic_more_vert),
                    contentDescription = "Text separator",
                    tint = YpBlack
                )
            }
        }
    }
}

@Composable
fun MenuBottomSheetContent(
    playlist: Playlist,
    sharePlaylist: () -> Unit,
    editPlaylist: () -> Unit,
    deletePlaylist: () -> Unit
) {
    Column {
        PlaylistRowListItem(playlist = playlist, onItemClick = { })

        Text(
            text = stringResource(id = R.string.share),
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { sharePlaylist() }
                .padding(horizontal = 16.dp, vertical = 20.dp))

        Text(
            text = stringResource(id = R.string.edit_playlist),
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { editPlaylist() }
                .padding(horizontal = 16.dp, vertical = 20.dp)
        )


        Text(
            text = stringResource(id = R.string.delete_playlist),
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { deletePlaylist() }
                .padding(start = 20.dp, top = 16.dp, bottom = 56.dp)
        )
    }

}

@Composable
fun TracksBottomSheetContent(
    tracks: List<Track>,
    onItemClick: (Track) -> Unit,
    onLongItemClick: (Long) -> Unit
) {

    TrackList(
        tracks = tracks,
        onItemClick = { track -> onItemClick(track) },
        isReverse = false,
        onLongItemClick = { trackId -> onLongItemClick(trackId) }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    @StringRes titleResId: Int,
    @StringRes textResId: Int,
) {

    AlertDialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Column(
            modifier = Modifier.background(color = Color.White)
        ) {
            Text(
                text = stringResource(id = titleResId),
                fontFamily = ysDisplayFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = YpBlack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 20.dp)
            )

            Text(
                text = stringResource(id = textResId),
                fontFamily = ysDisplayFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = YpBlack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 16.dp, bottom = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.yes).uppercase(),
                    fontFamily = ysDisplayFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = YpBlue,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable { onConfirm() }
                )

                Text(
                    text = stringResource(id = R.string.no).uppercase(),
                    fontFamily = ysDisplayFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = YpBlue,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { onDismissRequest() }
                )
            }
        }

    }

}
