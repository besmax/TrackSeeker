package bes.max.trackseeker.domain.settings

import kotlinx.coroutines.flow.Flow

interface SettingsInteractor {

    fun isNightModeActive(): Flow<Boolean>

    suspend fun setIsNightModeActive(isNightModeActive: Boolean)

}