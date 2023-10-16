package bes.max.trackseeker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bes.max.trackseeker.R
import bes.max.trackseeker.domain.models.PlayerState
import bes.max.trackseeker.domain.models.Track
import bes.max.trackseeker.presentation.player.PlayerViewModel
import bes.max.trackseeker.ui.theme.YpBlack
import bes.max.trackseeker.ui.theme.YpGray
import bes.max.trackseeker.ui.theme.ysDisplayFamily
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.RequestBuilderTransform
import com.bumptech.glide.integration.compose.placeholder
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PlayerScreen(
    track: Track,
    navigateBack: () -> Unit,
    playerViewModel: PlayerViewModel = koinViewModel {
        parametersOf(track)
    },
) {
    val playerState by playerViewModel.playerState.observeAsState(PlayerState.STATE_DEFAULT)
    val playingTime by playerViewModel.playingTime.observeAsState("00:00")
    val isFavorite by playerViewModel.isFavorite.observeAsState(false)

    PlayerScreenContent(
        playerState = playerState,
        playingTime = playingTime,
        isFavorite = isFavorite,
        track = track,
        navigateBack = navigateBack,
        playbackControl = { playerViewModel.playbackControl() },
        addOrDeleteFromFavorite = {
            if(isFavorite) playerViewModel.deleteFromFavorite(track)
            else playerViewModel.addToFavorite(track)
        }
    )

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PlayerScreenContent(
    playerState: PlayerState,
    playingTime: String,
    isFavorite: Boolean,
    track: Track,
    navigateBack: () -> Unit,
    playbackControl: () -> Unit,
    addOrDeleteFromFavorite: () -> Unit
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
        GlideImage(
            model = track.bigCover,
            contentDescription = track.trackName,
            modifier = Modifier
                .padding(vertical = 24.dp)
                .width(312.dp)
                .height(312.dp)
                .clip(RoundedCornerShape(8.dp)),
            failure = placeholder(painterResource(id = R.drawable.ic_picture_not_found)),
            loading = placeholder(painterResource(id = R.drawable.ic_picture_not_found)),
        )

        Text(
            text = track.trackName,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            color = YpBlack,
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
            color = YpBlack,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .align(Alignment.Start)
        )

        Row(modifier = Modifier) {
            IconButton(
                onClick = { /*TODO*/ },
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
            color = YpBlack,
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
            color = YpBlack,
            textAlign = TextAlign.End,
            maxLines = 1
        )
    }
}
