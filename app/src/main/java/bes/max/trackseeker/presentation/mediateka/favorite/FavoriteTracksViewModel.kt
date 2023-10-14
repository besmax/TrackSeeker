package bes.max.trackseeker.presentation.mediateka.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bes.max.trackseeker.domain.mediateka.FavoriteTracksInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val interactor: FavoriteTracksInteractor
) : ViewModel() {

    private val _screenState: MutableLiveData<FavoriteScreenState> =
        MutableLiveData(FavoriteScreenState.Empty)
    val screenState: LiveData<FavoriteScreenState> = _screenState

    init {
        getFavoriteTracks()
    }

    fun getFavoriteTracks() {
        _screenState.value = FavoriteScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getAllFavoriteTracks().collect() { favTracks ->
                if (favTracks.isNullOrEmpty()) {
                    _screenState.postValue(FavoriteScreenState.Empty)
                } else {
                    _screenState.postValue(FavoriteScreenState.Content(favTracks))
                }
            }
        }
    }



}