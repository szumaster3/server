package content.global.handlers.item

import core.api.*
import core.api.quest.hasRequirement
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.impl.WildernessZone
import org.rs.consts.Items
import org.rs.consts.Quests

class TeleportCrystalListener : InteractionListener {
    override fun defineListeners() {
        on(crystalIDs, IntType.ITEM, "activate") { player, node ->
            if (!hasRequirement(player, Quests.MOURNINGS_END_PART_I)) return@on true
            if (!WildernessZone.checkTeleport(player, 20)) {
                sendMessage(player, "The crystal is unresponsive.")
                return@on false
            }
            if (hasTimerActive(player, "teleblock")) {
                sendMessage(player, "A magical force has stopped you from teleporting.")
                return@on true
            }
            sendDialogueOptions(player, "Select an Option", "Teleport to Lletya", "Cancel").also {
                addDialogueAction(player) { player, button ->
                    if (button == 1) {
                        player.teleporter.send(Location(2329, 3172), TeleportType.NORMAL)
                        degrade(player, Item(node.id))
                    }
                    return@addDialogueAction
                }
            }
            return@on true
        }
    }

    companion object {
        val crystalIDs =
            intArrayOf(
                Items.TP_CRYSTAL_4_6099,
                Items.TP_CRYSTAL_3_6100,
                Items.TP_CRYSTAL_2_6101,
                Items.TP_CRYSTAL_1_6102,
            )

        private fun degrade(
            player: Player,
            item: Item,
        ) {
            val id = item.id
            val newItem = item.id + 1
            if (id < 6102) {
                removeItem(player, Item(id, 1))
                addItem(player, newItem, 1)
                sendMessage(player, "Your teleportation crystal has degraded from use.")
            } else {
                removeItem(player, Item(id, 1))
                addItem(player, newItem, 1)
                sendMessage(player, "Your teleportation crystal has degraded to a tiny elf crystal,")
                sendMessage(player, "Eluned can re-enchant it.")
            }
        }
    }
}
