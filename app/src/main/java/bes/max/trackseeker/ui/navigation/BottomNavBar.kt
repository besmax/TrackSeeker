package bes.max.trackseeker.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import bes.max.trackseeker.R
import bes.max.trackseeker.ui.theme.YpBlack
import bes.max.trackseeker.ui.theme.YpBlue
import bes.max.trackseeker.ui.theme.ysDisplayFamily

@Composable
fun BottomNavBar(
    navController: NavHostController,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {

    val screens = listOf(
        Screen.MediatekaScreen,
        Screen.SearchScreen,
        Screen.SettingsScreen
    )

    NavigationBar(
        modifier = Modifier
            .background(colorResource(id = R.color.black_white))
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->

            NavigationBarItem(
                label = {
                    Text(
                        text = stringResource(id = screen.titleResId),
                        fontFamily = ysDisplayFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                },
                selected = currentRoute == screen.route,
                icon = {
                    Icon(
                        painter = painterResource(
                            id = screen.iconResId ?: R.drawable.ic_picture_not_found
                        ), contentDescription = "Nav bar icon"
                    )
                },
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = colorResource(id = R.color.black_white),
                    selectedIconColor = YpBlue,
                    selectedTextColor = YpBlue,
                    unselectedTextColor = colorResource(id = R.color.black_white),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }

}