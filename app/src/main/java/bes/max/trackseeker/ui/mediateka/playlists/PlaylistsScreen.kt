package bes.max.trackseeker.ui.mediateka.playlists

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bes.max.trackseeker.R
import bes.max.trackseeker.presentation.mediateka.playlists.PlaylistsViewModel
import bes.max.trackseeker.ui.navigation.Screen
import bes.max.trackseeker.ui.theme.ysDisplayFamily
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistsScreen(
    navController: NavController,
    playlistsViewModel: PlaylistsViewModel = koinViewModel()
) {

    PlaylistsScreenContent(
        addPlaylist = { navController.navigate(Screen.NewPlaylistScreen.route) }
    )
}

@Composable
fun PlaylistsScreenContent(
    addPlaylist: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = { addPlaylist() },
            modifier = Modifier
                .padding(top = 24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.button_background),
                contentColor = colorResource(id = R.color.button_text)
            )
        ) {
            Text(
                text = stringResource(id = R.string.playlist_placeholder_button),
                fontFamily = ysDisplayFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
        }

        NoPlaylists()

    }

}

@Composable
fun NoPlaylists() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 46.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_not_found),
            contentDescription = "Playlists not found",
        )
        Text(
            text = stringResource(id = R.string.playlist_placeholder_text),
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 19.sp,
            textAlign = TextAlign.Center
        )
    }
}