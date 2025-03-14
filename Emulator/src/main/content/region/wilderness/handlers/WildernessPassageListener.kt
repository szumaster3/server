package content.region.wilderness.handlers

import core.api.quest.hasRequirement
import core.api.sendMessage
import core.api.teleport
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs.consts.Quests
import org.rs.consts.Scenery

class WildernessPassageListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.ENTRANCE_37749, IntType.SCENERY, "go-through") { player, _ ->
            teleport(player, Location.create(2885, 4372, 2))
            return@on true
        }

        on(Scenery.EXIT_37928, IntType.SCENERY, "go-through") { player, _ ->
            teleport(player, Location.create(3214, 3782, 0))
            return@on true
        }

        on(Scenery.TRAPDOOR_39188, IntType.SCENERY, "open") { player, _ ->
            if (!hasRequirement(player, Quests.DEFENDER_OF_VARROCK)) return@on true
            ClimbActionHandler.climb(
                player,
                ClimbActionHandler.CLIMB_DOWN,
                Location.create(3241, 9991, 0),
                "You descend into the cavern below.",
            )
            return@on true
        }

        on(Scenery.LADDER_39191, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(
                player,
                ClimbActionHandler.CLIMB_UP,
                Location.create(3239, 3606, 0),
                "You climb up the ladder to the surface.",
            )
            return@on true
        }

        on(Scenery.DOOR_39200, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "The door doesn't open.")
            return@on true
        }
    }
}
