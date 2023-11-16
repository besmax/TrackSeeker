package bes.max.trackseeker.ui.mediateka.playlistdetails

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import bes.max.trackseeker.R
import bes.max.trackseeker.domain.models.Track
import bes.max.trackseeker.presentation.mediateka.playlistdetails.PlaylistDetailsScreenState
import bes.max.trackseeker.presentation.mediateka.playlistdetails.PlaylistDetailsViewModel
import bes.max.trackseeker.presentation.utils.GsonTrackConverter
import bes.max.trackseeker.ui.TrackList
import bes.max.trackseeker.ui.navigation.Screen
import bes.max.trackseeker.ui.player.PlayerBottomSheetContent
import bes.max.trackseeker.ui.player.PlayerScreenContent
import bes.max.trackseeker.ui.theme.YpBlack
import bes.max.trackseeker.ui.theme.YpLightGray
import bes.max.trackseeker.ui.theme.ysDisplayFamily
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailsScreen(
    navController: NavController,
    playlistDetailsViewModel: PlaylistDetailsViewModel = koinViewModel()
) {

    val screenState by playlistDetailsViewModel.screenState.observeAsState(
        PlaylistDetailsScreenState.Default
    )

    val scaffoldState = rememberBottomSheetScaffoldState()

    val bottomSheetMenuState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheetMenu by remember { mutableStateOf(false) }

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
                }
            )
        },
        sheetSwipeEnabled = true,
        sheetPeekHeight = 200.dp,
        sheetContainerColor = MaterialTheme.colorScheme.background,
        containerColor = MaterialTheme.colorScheme.background
    ) {

        if (screenState is PlaylistDetailsScreenState.Content) {
            PlaylistDetailsScreenContent(
                screenState = screenState as PlaylistDetailsScreenState.Content,
                navigateBack = { navController.navigateUp() },
                modifier = Modifier.clickable {
                    scope.launch {
                        scaffoldState.bottomSheetState.partialExpand()
                    }
                }
            )
        }

    }


}

@Composable
fun PlaylistDetailsScreenContent(
    screenState: PlaylistDetailsScreenState.Content,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = YpLightGray)
    ) {

        Box() {
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

            AsyncImage(
                model = screenState.playlistDetails.cover?.toUri(),
                contentDescription = "playlist cover",
                error = painterResource(id = R.drawable.playlist_placeholder_grid),
                placeholder = painterResource(id = R.drawable.playlist_placeholder_grid),
                modifier = Modifier.aspectRatio(1f),
                contentScale = ContentScale.FillBounds
            )
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

@Composable
fun TracksBottomSheetContent(
    tracks: List<Track>,
    onItemClick: (Track) -> Unit
) {

    TrackList(
        tracks = tracks,
        onItemClick = { track -> onItemClick(track) },
        isReverse = false
    )

}
