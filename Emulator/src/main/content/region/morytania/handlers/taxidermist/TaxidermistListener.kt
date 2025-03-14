package content.region.morytania.handlers.taxidermist

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.splitLines
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs

class TaxidermistListener : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles stuff the remains of specific items.
         * https://runescape.fandom.com/wiki/Taxidermist
         */

        onUseWith(IntType.NPC, itemIDs, NPCs.TAXIDERMIST_4246) { player, used, _ ->
            val stuffed = StuffedItem.product[used.id] ?: return@onUseWith true
            face(findNPC(NPCs.TAXIDERMIST_4246)!!, player, 3)
            if (amountInInventory(player, stuffed.dropId) == 1) {
                openDialogue(
                    player,
                    object : DialogueFile() {
                        override fun handle(
                            componentID: Int,
                            buttonID: Int,
                        ) {
                            npc = NPC(NPCs.TAXIDERMIST_4246)
                            when (stage) {
                                0 -> npc(*splitLines(stuffed.message)).also { stage++ }
                                1 -> npc("I can preserve that for you for ${stuffed.price} coins.").also { stage++ }
                                2 -> options("Yes please.", "No thanks.").also { stage++ }
                                3 ->
                                    when (buttonID) {
                                        1 -> {
                                            end()
                                            if (!removeItem(
                                                    player,
                                                    Item(Items.COINS_995, stuffed.price),
                                                    Container.INVENTORY,
                                                )
                                            ) {
                                                sendDialogue(player, "You don't have enough coins in order to do that.")
                                                return
                                            }
                                            if (stuffed.dropId != used.id) {
                                                npc("Don't be silly, I can't preserve that!")
                                                return
                                            }
                                            replaceSlot(player, Item(used.id).slot, Item(stuffed.stuffedId, 1))
                                            npc("There you go!")
                                        }

                                        2 -> {
                                            end()
                                            npc("All right, come back if you change your mind, eh?")
                                        }
                                    }
                            }
                        }
                    },
                )
            }
            return@onUseWith true
        }
    }

    companion object {
        val itemIDs =
            intArrayOf(
            /*
             * Heads.
             */
                Items.CRAWLING_HAND_7975,
                Items.COCKATRICE_HEAD_7976,
                Items.BASILISK_HEAD_7977,
                Items.KURASK_HEAD_7978,
                Items.ABYSSAL_HEAD_7979,
                Items.KBD_HEADS_7980,
                Items.KQ_HEAD_7981,
            /*
             * Big fishes.
             */
                Items.BIG_SWORDFISH_7991,
                Items.BIG_SHARK_7993,
                Items.BIG_BASS_7989,
            )
    }
}
