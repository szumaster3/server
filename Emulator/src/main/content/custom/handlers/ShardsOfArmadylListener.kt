package content.custom.handlers

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items

class ShardsOfArmadylListener : InteractionListener {
    override fun defineListeners() {
        on(Items.SHARDS_OF_ARMADYL_14701, IntType.ITEM, "Combine") { player, node ->
            if (inInventory(player, node.id) && amountInInventory(player, node.id) >= 100) {
                setTitle(player, 2)
                sendDialogueOptions(player, "Combine the shards?", "Yes", "No.")
                addDialogueAction(player) { _, button ->
                    when (button) {
                        2 -> {
                            if (freeSlots(player) == 0) {
                                closeInterface(player)
                                sendMessage(player, "You don't have enough inventory space.")
                                return@addDialogueAction
                            }
                            lock(player, 2)
                            animate(player, 712, true)
                            removeItem(player, Item(node.id, 100))
                            addItemOrDrop(player, Items.ORB_OF_ARMADYL_14706)
                            sendMessage(player, "You combine the shards into the Orb of Armadyl.")
                        }
                        else -> closeInterface(player)
                    }
                }
            } else {
                sendMessage(player, "You don't have enough shards to combine.")
            }
            return@on true
        }
    }
}
