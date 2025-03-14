package content.region.desert.handlers

import core.api.lock
import core.api.openInterface
import core.api.quest.hasRequirement
import core.api.runTask
import core.api.teleport
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Quests
import org.rs.consts.Scenery

class SophanemListener : InteractionListener {
    companion object {
        private const val LADDER_UP = Scenery.LADDER_20277
        private const val LADDER_DOWN = Scenery.LADDER_20275
    }

    override fun defineListeners() {
        on(LADDER_UP, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.USE_LADDER_828), Location(3315, 2796, 0))
            return@on true
        }

        on(LADDER_DOWN, IntType.SCENERY, "climb-down") { player, _ ->
            if (!hasRequirement(player, Quests.CONTACT)) return@on true
            ClimbActionHandler.climb(player, Animation(Animations.MULTI_BEND_OVER_827), Location(2799, 5160, 0))
            return@on true
        }

        on(Scenery.DOOR_6614, IntType.SCENERY, "open") { player, _ ->
            lock(player, 3)
            openInterface(player, Components.FADE_TO_BLACK_115)
            runTask(player, 2) {
                teleport(player, Location.create(3277, 9171, 0), TeleportManager.TeleportType.INSTANT)
            }
            return@on true
        }
    }
}
