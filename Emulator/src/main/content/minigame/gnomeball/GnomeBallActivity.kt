package content.minigame.gnomeball

import core.game.activity.ActivityManager
import core.game.activity.ActivityPlugin
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.zone.ZoneRestriction

class GnomeBallActivity :
    ActivityPlugin(
        "gnomeball",
        false,
        false,
        false,
        ZoneRestriction.CANNON,
        ZoneRestriction.FIRES,
        ZoneRestriction.FOLLOWERS,
        ZoneRestriction.RANDOM_EVENTS,
    ) {
    private val waitTime = if (GameWorld.settings?.isDevMode == true) 10 else 203
    private val waitingPlayers = ArrayList<Player>()

    // private val sessions = ArrayList<GnomeBallSession>()
    private var activity: GnomeBallActivity? = null
    private var nextStart = GameWorld.ticks

    init {
        activity = this
    }

    override fun configure() {
    }

    override fun start(
        player: Player?,
        login: Boolean,
        vararg args: Any?,
    ): Boolean {
        player ?: return false
        waitingPlayers.add(player)
        return true
    }

    fun addPlayer(player: Player) {
        if (waitingPlayers.isEmpty()) {
            nextStart = GameWorld.ticks + waitTime
        }
        waitingPlayers.add(player)
    }

    fun removePlayer(player: Player) {
        waitingPlayers.remove(player)
    }

    override fun newInstance(p: Player?): ActivityPlugin {
        ActivityManager.register(this)
        return this
    }

    override fun getSpawnLocation(): Location {
        return Location.create(2383, 3488, 0)
    }
}
