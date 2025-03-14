package content.global.ame.pinball

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.api.ui.restoreTabs
import core.api.ui.setMinimapState
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.system.timer.impl.AntiMacro
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds

object PinballUtils {
    val VARBIT_PINBALL_SCORE = 2121
    val PINBALL_EVENT_LOCATION = Location.create(1972, 5046, 0)
    val PINBALL_EVENT_ZONE_BORDERS = ZoneBorders(1961, 5033, 1982, 5054)
    val PINBALL_EVENT_WRONG_SCENERY_IDs = intArrayOf(15001, 15003, 15005, 15007, 15009)
    val PINBALL_EVENT_SCENERY_IDs = intArrayOf(15000, 15001, 15002, 15004, 15005, 15006, 15007, 15008, 15009)
    val PINBALL_EVENT_CAVE_EXIT_SCENERY_ID = 15010
    val PINBALL_EVENT_GUARD_NPCs = intArrayOf(NPCs.FLIPPA_3912, NPCs.TILT_3913)
    val PINBALL_EVENT_MYSTERIOUS_OLD_MAN = NPC(NPCs.MYSTERIOUS_OLD_MAN_410, Location.create(1971, 5046, 0))

    val PINBALL_REWARD =
        intArrayOf(
            Items.UNCUT_DIAMOND_1618,
            Items.UNCUT_RUBY_1620,
            Items.UNCUT_EMERALD_1622,
            Items.UNCUT_SAPPHIRE_1624,
        )

    private val PILLAR_MAP =
        arrayOf(
            Scenery(15001, Location(1967, 5046, 0)),
            Scenery(15003, Location(1969, 5049, 0)),
            Scenery(15005, Location(1972, 5050, 0)),
            Scenery(15007, Location(1975, 5049, 0)),
            Scenery(15009, Location(1977, 5046, 0)),
        )

    private val SCENERY_REPLACEMENTS =
        arrayOf(
            Pair(15000, Location(1967, 5046, 0)),
            Pair(15002, Location(1969, 5049, 0)),
            Pair(15004, Location(1972, 5050, 0)),
            Pair(15006, Location(1975, 5049, 0)),
            Pair(15008, Location(1977, 5046, 0)),
        )

    fun generateTag(player: Player): Boolean {
        val score = getAttribute(player, GameAttributes.RE_PINBALL_OBJ, -1)
        if (score >= 10) return true
        for (i in 0..4) {
            if (getAttribute(player, GameAttributes.RE_PINBALL_OBJ, -1) == i) {
                replaceScenery(PILLAR_MAP[i], PILLAR_MAP[i].id - 1, -1)
                setAttribute(player, GameAttributes.RE_PINBALL_INTER, i)
                playAudio(player, Sounds.PILLARTAG_PINBALL_2278)
            }
        }
        return false
    }

    fun replaceTag(player: Player) {
        val index = getAttribute(player, GameAttributes.RE_PINBALL_INTER, -1)
        if (index in 0..4) {
            val (newId, location) = SCENERY_REPLACEMENTS[index]
            replaceScenery(Scenery(newId, location), PILLAR_MAP[index].id, -1, location)
        }
    }

    fun cleanup(player: Player) {
        player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
        clearLogoutListener(player, RandomEvent.logout())
        restoreTabs(player)
        closeOverlay(player)
        closeInterface(player)
        removeAttributes(
            player,
            RandomEvent.save(),
            GameAttributes.RE_PINBALL_START,
            GameAttributes.RE_PINBALL_OBJ,
            GameAttributes.RE_PINBALL_INTER,
        )
        openInterface(player, Components.CHATDEFAULT_137)
        setMinimapState(player, 0)
    }

    fun reward(player: Player) {
        queueScript(
            player,
            2,
            QueueStrength.STRONG, // TOUGH
        ) {
            AntiMacro.terminateEventNpc(player)
            setVarbit(player, VARBIT_PINBALL_SCORE, 0)
            addItemOrDrop(
                player,
                PINBALL_REWARD.random(),
                if (PINBALL_REWARD.contains(Items.UNCUT_DIAMOND_1618)) {
                    2
                } else {
                    (3..5).random()
                },
            )
            return@queueScript stopExecuting(player)
        }
    }
}
