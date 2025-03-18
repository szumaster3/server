package content.minigame.fistofguthix.handlers

import core.game.activity.ActivityPlugin
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.plugin.ClassScanner.definePlugin

class FOGActivityPlugin : ActivityPlugin("Fist of Guthix", false, true, true) {
    var round = 0

    @Throws(Throwable::class)
    override fun newInstance(p: Player): ActivityPlugin = FOGActivityPlugin()

    override fun getSpawnLocation(): Location? = null

    override fun configure() {
        definePlugin(FOGLobby())
        definePlugin(FOGWaitingZone())
        register(ZoneBorders(1625, 5638, 1715, 5747))
    }

    companion object {
        const val MAX_PLAYERS = 250
        const val WAITING_INTERFACE = 731
    }
}
