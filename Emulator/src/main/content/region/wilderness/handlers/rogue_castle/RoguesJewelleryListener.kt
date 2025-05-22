package content.region.wilderness.handlers.rogue_castle

import core.api.*
import core.api.quest.hasRequirement
import core.api.ui.closeDialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class RoguesJewelleryListener : InteractionListener {
    private val jewelleryIDs = RoguesJewellery.values().map { it.item }.toIntArray()

    override fun defineListeners() {

        /*
         * Handles exchange jewellery with rogues.
         */

        onUseWith(IntType.NPC, jewelleryIDs, NPCs.ROGUE_8122) { player, used, _ ->
            if (!hasRequirement(player, Quests.SUMMERS_END)) return@onUseWith false

            val jewellery = RoguesJewellery.JewelleryMap[used.id] ?: return@onUseWith true
            val invAmount = amountInInventory(player, jewellery.item)
            val itemName = getItemName(used.id)

            sendNPCDialogue(player, NPCs.ROGUE_8122, "I'll give you ${jewellery.price * invAmount} coins each for that $itemName. Do we have a deal?", FaceAnim.HALF_ASKING)
            addDialogueAction(player) { _, _ ->
                sendDialogueOptions(player, "Select an option", "Yes, we do.", "No, we do not.")
                addDialogueAction(player) { _, button ->
                    when (button) {
                        2 -> {
                            closeDialogue(player)

                            if (invAmount >= 10000) {
                                sendNPCDialogue(
                                    player,
                                    NPCs.ROGUE_8122,
                                    "Whoa, that's quite a bit of jewellery you've got there! Please try to keep it in amounts smaller than 10000. Big numbers make my head hurt."
                                )
                                return@addDialogueAction
                            }

                            if (freeSlots(player) < 1) {
                                sendMessage(player, "You don't have enough inventory space for that.")
                                return@addDialogueAction
                            }

                            val removed = removeItem(player, Item(jewellery.item, invAmount), Container.INVENTORY)
                            if (removed) {
                                addItem(player, Items.COINS_995, invAmount * jewellery.price)
                                sendNPCDialogue(
                                    player,
                                    NPCs.ROGUE_8122,
                                    "It was a pleasure doing business with you. Come back if you have more jewellery to sell."
                                )
                            } else {
                                sendNPCDialogue(
                                    player,
                                    NPCs.ROGUE_8122,
                                    "Sorry, but I can’t seem to take those from you. Make sure it's unenchanted gold jewellery, and not noted."
                                )
                            }
                        }

                        else -> closeDialogue(player)
                    }
                }
            }
            return@onUseWith true
        }
    }
}
