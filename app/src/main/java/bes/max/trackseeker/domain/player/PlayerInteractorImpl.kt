package bes.max.trackseeker.domain.player

class PlayerInteractorImpl (private val player: Player) : PlayerInteractor {

    override val state = player.playerState

    override fun preparePlayer(dataSourceUrl: String) {
        player.preparePlayer(dataSourceUrl)
    }

    override fun play() {
        player.startPlayer()
    }

    override fun pause() {
        player.pausePlayer()
    }

    override fun release() {
        player.releasePlayer()
    }

    override fun getCurrentTime() =
        player.getCurrentPosition()
}