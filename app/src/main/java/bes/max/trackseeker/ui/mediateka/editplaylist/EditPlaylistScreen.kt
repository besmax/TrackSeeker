package bes.max.trackseeker.ui.mediateka.editplaylist

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.net.toUri
import androidx.navigation.NavController
import bes.max.trackseeker.R
import bes.max.trackseeker.presentation.mediateka.editplaylist.EditPlaylistViewModel
import bes.max.trackseeker.ui.mediateka.newplaylist.NewOrEditPlaylistScreenContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditPlaylistScreen(
    navController: NavController,
    editPlaylistViewModel: EditPlaylistViewModel = koinViewModel()
) {

    val playlist by editPlaylistViewModel.playlist.observeAsState()

    when (playlist) {
        is EditPlaylistScreenState.Editing -> {
            var name: String? = null
            var description: String? = null
            var uri: Uri? = null

            val updatePlaylist = {
                name.let { name ->
                    if (name != null) {
                        editPlaylistViewModel.updatePlaylist(
                            name = name,
                            description = description,
                            uri = uri
                        )
                    }
                }
            }

            NewOrEditPlaylistScreenContent(
                navigateBack = { navController.navigateUp() },
                fillName = { name = it },
                fillDescription = { description = it },
                fillUri = { uri = it },
                doOnButtonClick = {
                    updatePlaylist()
                    navController.navigateUp()
                },
                screenTitleRes = R.string.edit,
                buttonTitleRes = R.string.save,
                initialName = (playlist as EditPlaylistScreenState.Editing).playlist.name,
                initialDescription = (playlist as EditPlaylistScreenState.Editing).playlist.description,
                initialCoverUri = (playlist as EditPlaylistScreenState.Editing).playlist.coverPath?.toUri()
            )
        }

        is EditPlaylistScreenState.Updated -> navController.navigateUp()
        else -> {}
    }


}


