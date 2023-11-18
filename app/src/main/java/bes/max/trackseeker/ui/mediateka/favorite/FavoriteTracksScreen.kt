package bes.max.trackseeker.ui.mediateka.favorite

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bes.max.trackseeker.R
import bes.max.trackseeker.domain.models.Track
import bes.max.trackseeker.presentation.mediateka.favorite.FavoriteTracksViewModel
import bes.max.trackseeker.presentation.utils.GsonTrackConverter
import bes.max.trackseeker.ui.TrackList
import bes.max.trackseeker.ui.navigation.Screen
import bes.max.trackseeker.ui.theme.YpBlue
import bes.max.trackseeker.ui.theme.ysDisplayFamily
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteTracksScreen(
    navController: NavController,
    favoriteViewModel: FavoriteTracksViewModel = koinViewModel()
) {

    val uiState by favoriteViewModel.screenState.observeAsState(initial = FavoriteScreenState.Empty)

    LaunchedEffect(key1 = Unit) {
        favoriteViewModel.getFavoriteTracks()
    }

    FavoriteTracksScreenContent(
        uiState = uiState,
        onItemClick = { track ->
            val trackArg = GsonTrackConverter.convertTrackToJson(track)
            var encodeTrackArg = Uri.encode(trackArg) //Does not work without encoding
            navController.navigate(Screen.PlayerScreen.route.replace("{track}", encodeTrackArg))
        }
    )

}

@Composable
fun FavoriteTracksScreenContent(
    uiState: FavoriteScreenState,
    onItemClick: (Track) -> Unit
) {

    when (uiState) {
        is FavoriteScreenState.Empty -> EmptyFavoriteTracks()
        is FavoriteScreenState.Loading -> FavoriteTracksLoading()
        is FavoriteScreenState.Content -> TrackList(
            tracks = uiState.tracks,
            onItemClick = onItemClick,
            isReverse = false
        )
    }

}

@Composable
fun EmptyFavoriteTracks() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 118.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_not_found),
            contentDescription = "Favorite tracks not found",
        )
        Text(
            text = stringResource(id = R.string.favorite_tracks_placeholder_text),
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 19.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FavoriteTracksLoading() {
    Column(
        modifier = Modifier
            .padding(top = 140.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(44.dp),
            color = YpBlue,
        )
    }
}