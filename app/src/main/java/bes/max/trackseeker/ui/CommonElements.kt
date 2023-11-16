package bes.max.trackseeker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bes.max.trackseeker.R
import bes.max.trackseeker.domain.models.Track
import bes.max.trackseeker.ui.theme.YpBlue
import bes.max.trackseeker.ui.theme.YpGray
import bes.max.trackseeker.ui.theme.ysDisplayFamily
import coil.compose.AsyncImage

@Composable
fun Title(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 12.dp)
    ) {
        Text(
            text = text,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp
        )
    }
}

@Composable
fun TrackListItem(track: Track, onItemClick: (Track) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 20.dp, top = 8.dp, bottom = 8.dp)
            .clickable { onItemClick.invoke(track) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        AsyncImage(
            model = track.bigCover,
            contentDescription = track.trackName,
            error = painterResource(id = R.drawable.ic_picture_not_found),
            placeholder = painterResource(id = R.drawable.ic_picture_not_found),
            modifier = Modifier
                .width(45.dp)
                .height(45.dp)
                .clip(RoundedCornerShape(2.dp)),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = track.trackName,
                fontFamily = ysDisplayFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = track.artistName,
                    fontFamily = ysDisplayFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = YpGray
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_circle_between_text),
                    contentDescription = "text separator",
                    tint = YpGray,
                    modifier = Modifier.padding(horizontal = 5.dp)

                )
                Text(
                    text = track.trackTime,
                    fontFamily = ysDisplayFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = YpGray
                )
            }

        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow),
            contentDescription = "right arrow",
            tint = YpGray,
        )
    }
}

@Composable
fun TrackList(
    tracks: List<Track>,
    onItemClick: (Track) -> Unit,
    isReverse: Boolean,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        reverseLayout = isReverse,
        modifier = modifier.padding(top = 16.dp)
    ) {
        items(
            items = tracks,
            key = { track -> track.trackId }
        ) { track ->
            TrackListItem(track, onItemClick)
        }
    }
}

@Composable
fun Loading() {
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

@Composable
@Preview
fun TitlePreview() {
    Title(text = "Поиск")
}

