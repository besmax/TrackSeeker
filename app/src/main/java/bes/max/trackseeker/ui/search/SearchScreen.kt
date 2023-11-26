package bes.max.trackseeker.ui.search

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.modifier.modifierLocalConsumer
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
import bes.max.trackseeker.presentation.search.SearchViewModel
import bes.max.trackseeker.presentation.utils.GsonTrackConverter
import bes.max.trackseeker.ui.Loading
import bes.max.trackseeker.ui.Title
import bes.max.trackseeker.ui.TrackList
import bes.max.trackseeker.ui.navigation.Screen
import bes.max.trackseeker.ui.theme.YpBlack
import bes.max.trackseeker.ui.theme.YpLightGray
import bes.max.trackseeker.ui.theme.ysDisplayFamily
import org.koin.androidx.compose.koinViewModel

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
            var encodeTrackArg = Uri.encode(trackArg)
            navController.navigate(Screen.PlayerScreen.route.replace("{track}", encodeTrackArg))
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

            is SearchScreenState.Loading -> Loading()

            is SearchScreenState.TracksNotFound -> SearchTracksNotFound()

        }
    }
}


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
        colors = TextFieldDefaults.colors(
            focusedContainerColor = YpLightGray,
            unfocusedContainerColor = YpLightGray,
            disabledContainerColor = YpLightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedTextColor = YpBlack,
            unfocusedTextColor = YpBlack
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
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.background
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
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 42.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.you_searched),
                fontFamily = ysDisplayFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 19.sp,
                textAlign = TextAlign.Center
            )
            TrackList(tracks = tracks, onItemClick = onItemClick, isReverse = true, modifier = Modifier.weight(1f))
            Button(
                onClick = { clearHistory.invoke() },
                modifier = Modifier
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
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