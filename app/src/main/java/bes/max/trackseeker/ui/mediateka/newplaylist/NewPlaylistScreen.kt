package bes.max.trackseeker.ui.mediateka.newplaylist

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bes.max.trackseeker.R
import bes.max.trackseeker.presentation.mediateka.newplaylist.NewPlaylistViewModel
import bes.max.trackseeker.ui.theme.YpBlue
import bes.max.trackseeker.ui.theme.YpGray
import bes.max.trackseeker.ui.theme.YpLightGray
import bes.max.trackseeker.ui.theme.ysDisplayFamily
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewPlaylistScreen(
    navigateBack: () -> Unit,
    track: String? = null,
    newPlaylistViewModel: NewPlaylistViewModel = koinViewModel()
) {

    var name: String? = null
    var description: String? = null
    val createPlaylist = {
        name?.let {name ->
            newPlaylistViewModel.createPlaylist(
                name = name,
                description = description,
                trackArg = track,
                uri = null
            )
        }
    }

    NewPlaylistScreenContent(
        navigateBack = { navigateBack() },
        fillName = { name = it },
        fillDescription = { description = it },
        doOnButtonClick = { createPlaylist() }
    )
}

@Composable
fun NewPlaylistScreenContent(
    navigateBack: () -> Unit,
    fillName: (String) -> Unit,
    fillDescription: (String) -> Unit,
    doOnButtonClick: () -> Unit,
) {
    var buttonEnabled by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        TitleWithArrow(
            title = stringResource(id = R.string.new_playlist),
            navigateBack = { navigateBack() }
        )

        PlaylistCoverSection(
            doOnClick = { }
        )

        UserInput(
            hintRes = R.string.new_playlist_name,
            onValueChanged = {
                buttonEnabled = it.isNotBlank()
                fillName(it)
            }
        )

        UserInput(
            hintRes = R.string.Description,
            onValueChanged = { fillDescription(it) }
        )

        Spacer(modifier = Modifier.weight(1f))

        NewPlaylistScreenButton(
            titleRes = R.string.Create,
            doOnButtonClick = { doOnButtonClick() }
        )

    }


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
            fontSize = 22.sp,
            modifier = Modifier
                .padding(start = 12.dp),
        )

    }

}

@Composable
fun PlaylistCoverSection(
    doOnClick: () -> Unit,
) {

    val stroke = Stroke(
        width = 4f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(60f, 60f), 1f)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(24.dp)
            .clip(RoundedCornerShape(8.dp))
            .drawBehind {
                drawRoundRect(color = YpGray, style = stroke)
            },
        contentAlignment = Alignment.Center
    ) {

        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
            Image(
                painter = painterResource(
                    id = R.drawable.img_new_playlist_placeholder
                ),
                contentDescription = "Default playlist cover"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInput(
    @StringRes hintRes: Int,
    onValueChanged: ((String) -> Unit)? = null
) {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = {
            text = it
            onValueChanged?.invoke(it)
        },
        label = { if (text.isBlank()) Text(text = stringResource(id = hintRes)) },
        maxLines = 1,
        textStyle = TextStyle(
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = YpLightGray,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            textColor = Color.Black
        ),
        trailingIcon = {
            if (text.isNotBlank()) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_clear_text),
                    contentDescription = "clear text icon",
                    modifier = Modifier
                        .clickable {
                            text = ""
                            if (onValueChanged != null) {
                                onValueChanged("")
                            }
                        }
                )
            }
        },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .onFocusChanged {
                if (it.hasFocus && text.isBlank()) {
                    onValueChanged?.invoke(text)
                }
            }
    )
}

@Composable
fun NewPlaylistScreenButton(
    @StringRes titleRes: Int,
    doOnButtonClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = { doOnButtonClick() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = YpBlue,
            contentColor = Color.White
        ),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = stringResource(id = titleRes),
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
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
        doOnClick = { }
    )
}