package content.region.misc.handlers

import content.region.misc.dialogue.keldagrim.MagicDoorDialogue
import core.api.*
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items

class ZanarisListener : InteractionListener {
    private val enterLocation = Location(2461, 4356, 0)
    private val exitLocation = Location(2453, 4476, 0)
    private val magicDoorIDs =
        intArrayOf(org.rs.consts.Scenery.MAGIC_DOOR_12045, org.rs.consts.Scenery.MAGIC_DOOR_12047)

    override fun defineListeners() {
        on(magicDoorIDs, IntType.SCENERY, "open") { player, node ->
            if ((
                    node.id == org.rs.consts.Scenery.MAGIC_DOOR_12045 &&
                        node.location ==
                        Location(
                            2469,
                            4438,
                            0,
                        ) &&
                        player.location.x >= 2470
                ) ||
                (
                    player.location.y < 4434 &&
                        (
                            node.id == org.rs.consts.Scenery.MAGIC_DOOR_12045 ||
                                node.id == org.rs.consts.Scenery.MAGIC_DOOR_12047 &&
                                node.location ==
                                Location(
                                    2465,
                                    4434,
                                    0,
                                )
                        )
                ) ||
                (node.id == org.rs.consts.Scenery.MAGIC_DOOR_12047 && player.location.x >= 2470)
            ) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                player.dialogueInterpreter.open(MagicDoorDialogue.NAME, node)
            }
            return@on true
        }

        onUseWith(
            IntType.SCENERY,
            intArrayOf(Items.RAW_CHICKEN_2138, Items.EGG_1944),
            org.rs.consts.Scenery.CHICKEN_SHRINE_12093,
        ) { player, used, _ ->
            if (used.id != Items.RAW_CHICKEN_2138) {
                sendMessage(player, "Nice idea, but nothing interesting happens.")
                return@onUseWith false
            }
            if (!removeItem(player, used.asItem())) {
                sendMessage(player, "Nothing interesting happens.")
            } else {
                lock(player, 3)
                queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            animate(player, Animations.DISAPPEAR_2755)
                            return@queueScript keepRunning(player)
                        }

                        1 -> {
                            teleport(player, enterLocation)
                            animate(player, Animations.APPEAR_2757)
                            return@queueScript stopExecuting(player)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }
            }
            return@onUseWith true
        }

        on(org.rs.consts.Scenery.PORTAL_12260, IntType.SCENERY, "use") { player, _ ->
            teleport(player, exitLocation)
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.ROPE_954, org.rs.consts.Scenery.TUNNEL_ENTRANCE_12253) { player, used, _ ->
            if (!removeItem(player, used.asItem())) {
                sendMessage(player, "Nothing interesting happens.")
            } else {
                replaceScenery(
                    Scenery(org.rs.consts.Scenery.TUNNEL_ENTRANCE_12253, location(2455, 4380, 0)),
                    org.rs.consts.Scenery.TUNNEL_ENTRANCE_12254,
                    80,
                )
            }
            return@onUseWith true
        }

        on(org.rs.consts.Scenery.TUNNEL_ENTRANCE_12254, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.MULTI_BEND_OVER_827), Location(2441, 4381, 0))
            return@on true
        }

        on(org.rs.consts.Scenery.ROPE_12255, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.MULTI_BEND_OVER_827), Location(2457, 4380, 0))
            return@on true
        }
    }
}
