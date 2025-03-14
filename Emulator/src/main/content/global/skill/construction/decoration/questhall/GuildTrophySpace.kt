package content.global.skill.construction.decoration.questhall

import core.api.*
import core.api.ui.closeDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs.consts.Items
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class GuildTrophySpace : InteractionListener {
    val TELEPORTS =
        arrayOf(
            Location.create(3087, 3495, 0),
            Location.create(2919, 3175, 0),
            Location.create(3081, 3250, 0),
            Location.create(3304, 3124, 0),
        )

    override fun defineListeners() {
        on(org.rs.consts.Scenery.AMULET_OF_GLORY_13523, IntType.SCENERY, "rub", "remove") { player, node ->
            val option = getUsedOption(player)
            when (option) {
                "rub" -> {
                    setTitle(player, 5)
                    sendDialogueOptions(
                        player,
                        "Where would you like to teleport to?",
                        "Edgeville",
                        "Karamja",
                        "Draynor Village",
                        "Al Kharid",
                        "Nowhere",
                    )
                    addDialogueAction(player) { player, button ->
                        when (button) {
                            2 -> mountedGloryAction(player, node, 0)
                            3 -> mountedGloryAction(player, node, 1)
                            4 -> mountedGloryAction(player, node, 2)
                            5 -> mountedGloryAction(player, node, 3)
                            6 -> closeDialogue(player)
                        }
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

    private fun mountedGloryAction(
        player: Player,
        `object`: Node,
        int: Int,
    ) {
        if (!player.zoneMonitor.teleport(1, Item(Items.AMULET_OF_GLORY_1704))) {
            return
        }
        Executors.newSingleThreadScheduledExecutor().schedule({
            player.pulseManager.run(
                object : Pulse(4) {
                    override fun pulse(): Boolean {
                        player.lock(5)
                        teleport(player, TELEPORTS[int], TeleportManager.TeleportType.RANDOM_EVENT_OLD)
                        return false
                    }
                },
            )
        }, 0, TimeUnit.SECONDS)
    }
}
