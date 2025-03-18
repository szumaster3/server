package content.minigame.duelarena.handlers

import core.api.setVarp
import core.game.activity.ActivityPlugin
import core.game.component.Component
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.impl.PulseManager
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.request.RequestType
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.ClassScanner.definePlugin
import core.plugin.ClassScanner.definePlugins
import core.plugin.Initializable
import core.tools.RandomFunction
import java.util.*

@Initializable
class DuelArenaActivity : ActivityPlugin("Duel arena", false, false, true) {
    override fun enter(e: Entity): Boolean {
        if (e is Player) {
            val player = e
            player.interfaceManager.openOverlay(OVERLAY)
            player.interaction.set(CHALLENGE_OPTION)
            setVarp(player, 286, 0)
        }
        return super.enter(e)
    }

    override fun leave(
        e: Entity,
        logout: Boolean,
    ): Boolean {
        if (e is Player) {
            val player = e
            player.interfaceManager.closeOverlay()
            player.interaction.remove(CHALLENGE_OPTION)
        }
        return super.leave(e, logout)
    }

    @Throws(Throwable::class)
    override fun newInstance(p: Player): ActivityPlugin = this

    override fun getSpawnLocation(): Location? = null

    override fun continueAttack(
        e: Entity,
        target: Node,
        style: CombatStyle,
        message: Boolean,
    ): Boolean =
        if (e.isPlayer &&
            e
                .asPlayer()
                .zoneMonitor.zones.size > 1
        ) {
            super.continueAttack(e, target, style, message)
        } else {
            false
        }

    override fun interact(
        e: Entity,
        target: Node,
        o: Option,
    ): Boolean {
        if (e.isPlayer) {
            when (target.id) {
                3192 -> {
                    openScoreboard(e.asPlayer())
                    return true
                }
            }
        }
        return super.interact(e, target, o)
    }

    override fun death(
        e: Entity,
        killer: Entity,
    ): Boolean {
        if (e.isPlayer &&
            e
                .asPlayer()
                .zoneMonitor.zones.size > 1
        ) {
            return true
        }
        if (e is Player && killer is Player) {
            e.getSkills().heal(100)
            PulseManager.cancelDeathTask(e)
            return true
        }
        return true
    }

    override fun configure() {
        for (area in DUEL_AREAS) {
            ZoneBuilder.configure(area)
        }
        parseScoreboard()
        register(ZoneBorders(3325, 3201, 3396, 3280))
        definePlugin(DuelArea.ForfeitTrapdoorPlugin())
        definePlugins(DuelSession(null, null, false), DuelComponentPlugin(), ChallengeOptionPlugin())
    }

    companion object {
        val FRIEND_REQUEST = RequestType("Sending duel offer...", ":duelfriend:", DuelRequestModule(false))
        val STAKE_REQUEST = RequestType("Sending duel offer...", ":duelstake:", DuelRequestModule(true))
        val DUEL_AREAS =
            arrayOf(
                DuelArea(0, ZoneBorders(3332, 3244, 3357, 3258), false, Location.create(3345, 3251, 0)),
                DuelArea(1, ZoneBorders(3364, 3244, 3388, 3259), true, Location.create(3378, 3251, 0)),
                DuelArea(2, ZoneBorders(3333, 3224, 3357, 3239), true, Location.create(3345, 3231, 0)),
                DuelArea(3, ZoneBorders(3364, 3225, 3388, 3240), false, Location.create(3376, 3231, 0)),
                DuelArea(4, ZoneBorders(3333, 3205, 3357, 3220), false, Location.create(3346, 3212, 0)),
                DuelArea(5, ZoneBorders(3364, 3206, 3388, 3221), true, Location.create(3377, 3213, 0)),
            )
        val CHALLENGE_OPTION = Option("Challenge", 0)
        val DUEL_TYPE_SELECT = Component(640)
        private val OVERLAY = Component(638)
        private val SCOREBOARD = arrayOfNulls<String>(50)

        fun openScoreboard(player: Player) {
            player.lock(2)
            parseScoreboard()
            player.interfaceManager.open(Component(632))
            var index = 0
            for (i in 16..64) {
                player.packetDispatch.sendString(
                    if (SCOREBOARD.get(index) == null) {
                        ""
                    } else {
                        SCOREBOARD.get(
                            index,
                        )
                    },
                    632,
                    i - 1,
                )
                index++
            }
        }

        fun parseScoreboard() {}

        fun insertEntry(
            winner: Player?,
            looser: Player?,
        ) {}

        fun getDuelArea(obstacles: Boolean): DuelArea {
            val options: MutableList<DuelArea> = ArrayList(20)
            for (area in DUEL_AREAS) {
                if (!obstacles && area.isObstacles || obstacles && !area.isObstacles) {
                    continue
                }
                options.add(area)
            }
            return options[RandomFunction.random(options.size)]
        }
    }
}
