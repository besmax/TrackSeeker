package bes.max.trackseeker.presentation.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import bes.max.trackseeker.R
import bes.max.trackseeker.domain.models.EmailData
import bes.max.trackseeker.domain.settings.SettingsInteractor
import bes.max.trackseeker.domain.settings.SharingInteractor
import bes.max.trackseeker.presentation.utils.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val _showingToast = MutableLiveData<Int>()
    val showingToast: LiveData<Int> = _showingToast

    val isNightModeActive = settingsInteractor.isNightModeActive()


    private fun setIsNightModeActive(isNightModeActive: Boolean) {
        viewModelScope.launch {
            settingsInteractor.setIsNightModeActive(isNightModeActive)
        }
    }

    fun setIsNightModeActiveDebounce(isNightModeActive: Boolean) {
        val debounceSwitch = debounce<Boolean>(
            delayMillis = SET_NIGHT_MODE_DELAY,
            coroutineScope = viewModelScope,
            useLastParam = false,
            action = {
                setIsNightModeActive(it)
            }
        )
        debounceSwitch.invoke(isNightModeActive)
    }

    fun shareApp(link: String) {
        try {
            sharingInteractor.shareApp(link)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            _showingToast.value = R.string.error_no_suitable_app
            _showingToast.value = 0
        }
    }

    fun contactSupport(
        emailAddress: String,
        emailSubject: String,
        emailText: String
    ) {
        try {
            sharingInteractor.contactSupport(
                EmailData(
                    emailAddress,
                    emailSubject,
                    emailText
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            _showingToast.value = R.string.error_no_suitable_app
            _showingToast.value = 0
        }
    }

    fun openUserAgreement(link: String) {
        try {
            sharingInteractor.openUserAgreement(link)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            _showingToast.value = R.string.error_no_suitable_app
        }
    }

    companion object {
        private const val TAG = "SettingsViewModel"
        private const val SET_NIGHT_MODE_DELAY = 100L
    }

}