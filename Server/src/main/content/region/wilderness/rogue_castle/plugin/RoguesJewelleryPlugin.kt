package content.region.wilderness.rogue_castle.plugin

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

class RoguesJewelleryPlugin : InteractionListener {
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

            openDialogue(player, RogueJewelleryDialogue(jewellery, invAmount, itemName))
            return@onUseWith true
        }
    }

    inner class RogueJewelleryDialogue(
        private val jewellery: RoguesJewellery,
        private val invAmount: Int,
        private val itemName: String
    ) : DialogueFile() {

        init { stage = 0 }

        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> {
                    sendNPCDialogue(player!!, NPCs.ROGUE_8122, "I'll give you ${jewellery.price * invAmount} coins each for that $itemName. Do we have a deal?", FaceAnim.HALF_ASKING)
                    stage = 1
                }
                1 -> {
                    options("Yes, we do.", "No, we do not.")
                    stage = 2
                }
                2 -> {
                    when(buttonID) {
                        1 -> {
                            end()
                            if (invAmount >= 10000) {
                                sendNPCDialogue(player!!, NPCs.ROGUE_8122, "Whoa, that's quite a bit of jewellery you've got there! Please try to keep it in amounts smaller than 10000. Big numbers make my head hurt.")
                            } else if (freeSlots(player!!) < 1) {
                                sendMessage(player!!, "You don't have enough inventory space for that.")
                            } else {
                                val removed = removeItem(player!!, Item(jewellery.item, invAmount), Container.INVENTORY)
                                if (removed) {
                                    addItem(player!!, Items.COINS_995, invAmount * jewellery.price)
                                    sendNPCDialogue(player!!, NPCs.ROGUE_8122, "It was a pleasure doing business with you. Come back if you have more jewellery to sell.")
                                } else {
                                    sendNPCDialogue(player!!, NPCs.ROGUE_8122, "Sorry, but I can’t seem to take those from you. Make sure it's unenchanted gold jewellery, and not noted.")
                                }
                            }
                        }
                        2 -> end()
                    }
                    end()
                }
            }
        }
    }
}