package bes.max.trackseeker.domain.player

import bes.max.trackseeker.domain.models.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface Player {

    val playerState: StateFlow<PlayerState>

    fun preparePlayer(dataSourceUrl: String)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getCurrentPosition(): Int

}