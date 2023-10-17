package bes.max.trackseeker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bes.max.trackseeker.R
import bes.max.trackseeker.domain.models.Track
import bes.max.trackseeker.presentation.search.SearchScreenState
import bes.max.trackseeker.presentation.search.SearchViewModel
import bes.max.trackseeker.presentation.utils.GsonTrackConverter
import bes.max.trackseeker.ui.theme.YpBlue
import bes.max.trackseeker.ui.theme.YpLightGray
import bes.max.trackseeker.ui.theme.ysDisplayFamily
import org.koin.androidx.compose.koinViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel = koinViewModel()
) {
    val uiState by searchViewModel.screenState.observeAsState(SearchScreenState.Default)
    SearchScreenContent(
        uiState = uiState,
        doOnUserInput = { input -> searchViewModel.searchDebounce(input) },
        refreshSearch = { searchViewModel.refreshSearch() },
        clearHistory = { searchViewModel.clearHistory() },
        onItemClick = { track ->
            searchViewModel.saveTrackToHistory(track)
            val trackArg = GsonTrackConverter.convertTrackToJson(track)
            var encodeTrackArg = URLEncoder.encode(
                trackArg,
                StandardCharsets.UTF_8.toString()
            ) //Does not work without encoded value
            navController.navigate("playerScreen/{track}".replace("{track}", encodeTrackArg))
        }
    )
}

@Composable
fun SearchScreenContent(
    uiState: SearchScreenState,
    doOnUserInput: (String) -> Unit,
    refreshSearch: () -> Unit,
    clearHistory: () -> Unit,
    onItemClick: (Track) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Title(text = stringResource(id = R.string.search))
        UserInput(onValueChanged = { value -> doOnUserInput.invoke(value) })

        when (uiState) {
            is SearchScreenState.Default -> {}

            is SearchScreenState.SearchError -> SearchError(refreshSearch = refreshSearch)

            is SearchScreenState.History ->
                SearchHistory(
                    tracks = uiState.tracks,
                    clearHistory = clearHistory,
                    onItemClick = onItemClick
                )

            is SearchScreenState.Tracks -> TrackList(
                tracks = uiState.tracks,
                onItemClick = onItemClick,
                isReverse = false
            )

            is SearchScreenState.Loading -> SearchLoading()

            is SearchScreenState.TracksNotFound -> SearchTracksNotFound()

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInput(onValueChanged: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = {
            text = it
            onValueChanged.invoke(it)
        },
        label = { if (text.isBlank()) Text(text = stringResource(id = R.string.search)) },
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
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_find),
                contentDescription = "search icon",
            )
        },
        trailingIcon = {
            if (text.isNotBlank()) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_clear_text),
                    contentDescription = "clear text icon",
                    modifier = Modifier
                        .clickable {
                            text = ""
                            onValueChanged("")
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
                    onValueChanged.invoke(text)
                }
            }
    )
}

@Composable
fun SearchError(refreshSearch: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 104.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_no_internet),
            contentDescription = "No internet image",
        )
        Text(
            text = stringResource(id = R.string.search_screen_placeholder_text_error),
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 19.sp,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = { refreshSearch.invoke() },
            modifier = Modifier
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.button_background),
                contentColor = colorResource(id = R.color.button_text)
            )
        ) {
            Text(
                text = stringResource(id = R.string.refresh),
                fontFamily = ysDisplayFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun SearchLoading() {
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
fun SearchTracksNotFound() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 104.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_not_found),
            contentDescription = "Tracks not found",
        )
        Text(
            text = stringResource(id = R.string.nothing_found),
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 19.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SearchHistory(tracks: List<Track>, clearHistory: () -> Unit, onItemClick: (Track) -> Unit) {
    if (tracks.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 42.dp, end = 16.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.you_searched),
                fontFamily = ysDisplayFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 19.sp,
                textAlign = TextAlign.Center
            )
            TrackList(tracks = tracks, onItemClick = onItemClick, isReverse = true)
            Button(
                onClick = { clearHistory.invoke() },
                modifier = Modifier
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.button_background),
                    contentColor = colorResource(id = R.color.button_text)
                )
            ) {
                Text(
                    text = stringResource(id = R.string.clear_history),
                    fontFamily = ysDisplayFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}