package content.minigame.templetrekking.events.combat

import content.minigame.templetrekking.TempleTrekking
import content.minigame.templetrekking.monsters.HeadNPC.Companion.spawnTentacleHeadNPC
import content.minigame.templetrekking.monsters.TentacleNPC
import content.minigame.templetrekking.monsters.TentacleNPC.Companion.spawnTentacleNPC
import core.api.MapArea
import core.api.keepRunning
import core.api.queueScript
import core.api.stopExecuting
import core.api.teleport
import core.api.utils.PlayerCamera
import core.game.activity.ActivityManager
import core.game.activity.ActivityPlugin
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.build.DynamicRegion
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.plugin.Initializable

@Initializable
class TentacleCombatActivity :
    ActivityPlugin(
        "tentacle event",
        true,
        true,
        false,
        ZoneRestriction.RANDOM_EVENTS,
        ZoneRestriction.FOLLOWERS,
        ZoneRestriction.FIRES,
    ),
    MapArea {
    private val tentacleSession = ArrayList<TentacleCombatActivitySession>()
    private var activity: TentacleCombatActivity? = null
    val tentacles = ArrayList<TentacleNPC>()
    private val wtf = ArrayList<Player>()

    init {
        activity = this
    }

    override fun configure() {
        GameWorld.Pulser.submit(
            object : Pulse(1) {
                override fun pulse(): Boolean {
                    val session =
                        TentacleCombatActivitySession(
                            DynamicRegion.create(TempleTrekking.tentacleCombatEventRegion),
                            activity!!,
                        )
                    session.start(player)
                    tentacleSession.add(session)
                    tentacleSession.removeIf { session ->
                        if (!session.isActive && session.inactiveTicks >= 1000) {
                            PlayerCamera(player).reset()
                            tentacles.clear()
                            true
                        } else {
                            if (!session.isActive) {
                                session.inactiveTicks++
                            }
                            false
                        }
                    }
                    return false
                }
            },
        )
    }

    override fun start(
        player: Player?,
        login: Boolean,
        vararg args: Any?,
    ): Boolean {
        player ?: return false
        queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    wtf.add(player)
                    teleport(player, Location.create(2575, 5035, 2))
                    PlayerCamera(player).shake(2, 0, 0, 32, 5)
                    return@queueScript keepRunning(player)
                }

                1 -> {
                    spawnTentacleNPC(player)
                    spawnTentacleHeadNPC(player)
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
        return true
    }

    override fun newInstance(p: Player?): ActivityPlugin {
        ActivityManager.register(this)
        return this
    }

    override fun getSpawnLocation(): Location? {
        return null
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(2569, 5027, 2581, 5042, 2, true))
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.TELEPORT, ZoneRestriction.FOLLOWERS)
    }
}
