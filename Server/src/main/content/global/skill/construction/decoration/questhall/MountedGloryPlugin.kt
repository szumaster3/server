package content.global.skill.construction.decoration.questhall

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Items
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MountedGloryPlugin : InteractionListener {
    val TELEPORTS = arrayOf(
        Location.create(3087, 3495, 0),
        Location.create(2919, 3175, 0),
        Location.create(3081, 3250, 0),
        Location.create(3304, 3124, 0),
    )

    override fun defineListeners() {
        on(shared.consts.Scenery.AMULET_OF_GLORY_13523, IntType.SCENERY, "rub", "remove") { player, node ->
            when (getUsedOption(player)) {

                "rub" -> {
                    setTitle(player, 5)
                    sendOptions(
                        player,
                        "Where would you like to teleport to?",
                        "Edgeville",
                        "Karamja",
                        "Draynor Village",
                        "Al Kharid",
                        "Nowhere"
                    )

                    addDialogueAction(player) { _, button ->
                        val destination = when (button) {
                            2 -> 0 // Edgeville
                            3 -> 1 // Karamja
                            4 -> 2 // Draynor
                            5 -> 3 // Al Kharid
                            else -> null
                        }

                        if (destination != null) {
                            teleport(player, node, destination)
                        }

                        closeDialogue(player)
                    }
                }

                "remove" -> {
                    if (!player.houseManager.isBuildingMode) {
                        sendMessage(player, "You have to be in building mode to do this.")
                        return@on true
                    }

                    openDialogue(player, "con:removedec", node.asScenery())
                }

                else -> return@on false
            }

            return@on true
        }
    }

    private fun teleport(player: Player, node: Node, index: Int) {
        if (!player.zoneMonitor.teleport(1, Item(Items.AMULET_OF_GLORY_1704))) {
            return
        }

        player.lock(5)
        player.animate(Animation(Animations.PULL_LEVER_POH_3611))

        queueScript(player, 4, QueueStrength.SOFT) { _ ->
            teleport(player, TELEPORTS[index], TeleportManager.TeleportType.RANDOM_EVENT_OLD)
            return@queueScript stopExecuting(player)
        }
    }
}
