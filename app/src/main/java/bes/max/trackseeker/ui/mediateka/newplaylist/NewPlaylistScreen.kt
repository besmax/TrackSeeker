package bes.max.trackseeker.ui.mediateka.newplaylist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bes.max.trackseeker.R
import bes.max.trackseeker.domain.models.Track
import bes.max.trackseeker.presentation.mediateka.NewPlaylistViewModel
import bes.max.trackseeker.ui.theme.ysDisplayFamily
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewPlaylistScreen(
    navigateBack: () -> Unit,
    track: Track? = null,
    newPlaylistViewModel: NewPlaylistViewModel = koinViewModel()
) {

}

@Composable
fun NewPlaylistScreenContent(
    navigateBack: () -> Unit,
) {

    TitleWithArrow(
        title = stringResource(id = R.string.new_playlist),
        navigateBack = { navigateBack() }
    )
}

@Composable
fun TitleWithArrow(
    title: String,
    navigateBack: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateBack() },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = "Back arrow",
            modifier = Modifier
                .padding(all = 12.dp),
        )

        Text(
            text = title,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp
        )

    }

}

@Composable
fun PlaylistCoverSection(
    doOnClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(312.dp)
            .padding(24.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(R.drawable.shape_playlist_cover), contentDescription = null,
            modifier = Modifier.matchParentSize())

        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
            Image(painter = painterResource(
                id = R.drawable.img_new_playlist_placeholder),
                contentDescription = "Default playlist cover"
            )
        }
    }
}

@Composable
@Preview
fun TitleWithArrowPreview() {
    TitleWithArrow(
        "New Playlist",
        navigateBack = { },
    )
}

@Composable
@Preview
fun PlaylistCoverSectionPreview() {
    PlaylistCoverSection(
        doOnClick = {  }
    )
}