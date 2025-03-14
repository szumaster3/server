package content.region.misthalin.handlers

import core.api.*
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Components
import org.rs.consts.Scenery

class EdgevilleListener : InteractionListener {
    override fun defineListeners() {
        on(Scenery.POSTER_29586, IntType.SCENERY, "pull-back") { player, _ ->
            sendDialogue(player, "There appears to be a tunnel behind this poster.")
            teleport(player, Location(3140, 4230, 2))
            return@on true
        }

        on(Scenery.TRAPDOOR_12267, IntType.SCENERY, "open") { player, _ ->
            animate(player, Animations.OPEN_CHEST_536)
            setVarbit(player, 1888, 1)
            return@on true
        }

        on(Scenery.OPEN_TRAPDOOR_12268, IntType.SCENERY, "close") { player, _ ->
            setVarbit(player, 1888, 0)
            return@on true
        }

        on(Scenery.OPEN_TRAPDOOR_12268, IntType.SCENERY, "go-down") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.MULTI_BEND_OVER_827), Location(3077, 9893, 0))
            return@on true
        }

        on(Scenery.CELLAR_STAIRS_12265, IntType.SCENERY, "climb") { player, _ ->
            ClimbActionHandler.climb(player, null, Location(3078, 3493, 0))
            return@on true
        }

        on(12266, IntType.SCENERY, "open", "close") { player, _ ->
            if (getUsedOption(player) == "open") {
                setVarp(player, 680, 1 shl 22, false)
            } else {
                setVarp(player, 680, 0)
            }
            return@on true
        }

        on(
            intArrayOf(Scenery.ROSES_9261, Scenery.ROSES_9262, Scenery.ROSES_30806),
            IntType.SCENERY,
            "take-seed",
        ) { player, node ->
            sendMessage(player, "There doesn't seem to be any seeds on this rosebush.")
            return@on true
        }

        on(26933, IntType.SCENERY, "open") { player, node ->
            animate(player, Animations.OPEN_CHEST_536)
            sendMessage(player, "The trapdoor opens...")
            replaceScenery(node.asScenery(), node.id + 1, -1)
            return@on true
        }

        on(26934, IntType.SCENERY, "close", "climb-down") { player, node ->
            if (getUsedOption(player) == "close") {
                animate(player, 535)
                sendMessage(player, "You close the trapdoor.")
                replaceScenery(node.asScenery(), node.id - 1, -1)
            } else {
                sendMessage(player, "You climb down through the trapdoor...")
                ClimbActionHandler.climbLadder(player, node.asScenery(), "climb-down")
            }
            return@on true
        }

        on(intArrayOf(Scenery.METAL_DOOR_29319, Scenery.METAL_DOOR_29320), IntType.SCENERY, "open") { player, node ->
            if (getUsedOption(player) == "open" && player.location.y < 9918) {
                openInterface(player, Components.WILDERNESS_WARNING_382)
                setAttribute(player, "wildy-gate", node)
            } else {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            }
            return@on true
        }
    }
}
