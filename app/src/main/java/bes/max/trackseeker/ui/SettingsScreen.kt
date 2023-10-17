package bes.max.trackseeker.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bes.max.trackseeker.R
import bes.max.trackseeker.presentation.settings.SettingsViewModel
import bes.max.trackseeker.ui.theme.ysDisplayFamily
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = koinViewModel()
) {

    val link = stringResource(id = R.string.link_for_app_share)
    val emailAddress = stringResource(id = R.string.email_for_support)
    val emailSubject = stringResource(id = R.string.email_theme_for_support)
    val emailText = stringResource(id = R.string.email_text_for_support)

    val isNightModeActive by settingsViewModel.isNightModeActive.observeAsState(initial = false)

    Log.d("LOOK HERE", "$isNightModeActive") //TODO delete this line

    SettingsScreenContent(
        switchTheme = { checked -> settingsViewModel.setIsNightModeActiveDebounce(checked) },
        shareApp = { settingsViewModel.shareApp(link) },
        contactSupport = {
            settingsViewModel.contactSupport(
                emailAddress = emailAddress,
                emailSubject = emailSubject,
                emailText = emailText
            )
        },
        openAgreement = { settingsViewModel.openUserAgreement(link) },
        isNightModeActive = isNightModeActive
    )
}

@Composable
fun SettingsScreenContent(
    switchTheme: (Boolean) -> Unit,
    shareApp: () -> Unit,
    contactSupport: () -> Unit,
    openAgreement: () -> Unit,
    isNightModeActive: Boolean
) {

    Column(modifier = Modifier.fillMaxSize()) {
        Title(text = stringResource(R.string.settings))

        SwitchSettingsSection(
            title = stringResource(id = R.string.dark_theme),
            onClick = switchTheme,
            initialPosition = isNightModeActive
        )

        SettingsSection(
            title = stringResource(id = R.string.share_app),
            iconId = R.drawable.ic_share,
            onClick = { shareApp() }
        )
        SettingsSection(
            title = stringResource(id = R.string.contact_support),
            iconId = R.drawable.ic_support,
            onClick = { contactSupport() }
        )
        SettingsSection(
            title = stringResource(id = R.string.user_agreement),
            iconId = R.drawable.ic_arrow,
            onClick = { openAgreement() }
        )
    }

}

@Composable
fun SettingsSection(
    title: String,
    iconId: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 21.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = title,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        )

        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "settings icon $title",
            tint = Color.Unspecified
        )
    }

}

@Composable
fun SwitchSettingsSection(
    title: String,
    onClick: (Boolean) -> Unit,
    initialPosition: Boolean
) {
    var checked by remember { mutableStateOf(initialPosition) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 21.dp)
            .clickable { checked = !checked },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = title,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        )

        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                onClick.invoke(checked)
            },
        )
    }


}


@Composable
@Preview
fun SettingsSectionPreview() {
    SettingsSection("Share settings", R.drawable.ic_share, onClick = {  })
}