package bes.max.trackseeker.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import bes.max.trackseeker.R
import bes.max.trackseeker.ui.theme.YpBlack
import bes.max.trackseeker.ui.theme.ysDisplayFamily

@Composable
fun MediatekaScreen(
    navController: NavController
) {

    MediatekaScreenContent(
        navController
    )
}

@Composable
fun MediatekaScreenContent(
    navController: NavController
) {
    var tabIndex by remember {
        mutableStateOf(0)
    }

    val tabs = listOf(
        stringResource(id = R.string.mediateka_tab_title_favorite_tracks),
        stringResource(id = R.string.mediateka_tab_title_playlist)
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Title(text = stringResource(id = R.string.mediateka))
        
        TabRow(
            selectedTabIndex = tabIndex,
            contentColor = YpBlack,
            indicator = {
                Box(
                    Modifier
                        .tabIndicatorOffset(it[tabIndex])
                        .height(2.dp)
                        .border(2.dp, YpBlack)
                )
            }

            ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                ) {
                    Text(
                        text = title,
                        fontFamily = ysDisplayFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.black_white),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
        }

        when(tabIndex) {
            0 -> FavoriteTracksScreen(navController = navController)
            1 -> PlaylistsScreen()
        }

    }

}