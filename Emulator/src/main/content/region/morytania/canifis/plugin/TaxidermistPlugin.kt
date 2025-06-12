package content.region.morytania.canifis.plugin

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.splitLines
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs

class TaxidermistPlugin : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles stuffing the remains of specific items.
         * https://runescape.fandom.com/wiki/Taxidermist
         */

        onUseWith(IntType.NPC, ITEM_IDS, NPCs.TAXIDERMIST_4246) { player, used, _ ->
            val stuffed = StuffedItem.product[used.id] ?: return@onUseWith true
            openDialogue(player, object : DialogueFile() {
                    override fun handle(componentID: Int, buttonID: Int) {
                        npc = NPC(NPCs.TAXIDERMIST_4246)
                        when (stage) {
                        0 -> {
                            npc(*splitLines(stuffed.message))
                            stage++
                        }
                        1 -> {
                            npc("I can preserve that for you for ${stuffed.price} coins.")
                            stage++
                        }
                        2 -> {
                            options("Yes please.", "No thanks.")
                            stage++
                        }
                        3 -> {
                            when (buttonID) {
                                1 -> {
                                    end()

                                    if (stuffed.dropId != used.id) {
                                        npc("Don't be silly, I can't preserve that!")
                                        return
                                    }

                                    if (!removeItem(player, Item(Items.COINS_995, stuffed.price))) {
                                        sendDialogue(player, "You don't have enough coins in order to do that.")
                                        return
                                    }

                                    replaceSlot(player, used.index, Item(stuffed.stuffedId, 1))
                                    npc("There you go!")
                                }

                                2 -> {
                                    end()
                                    npc("All right, come back if you change your mind, eh?")
                                }
                            }
                        }
                    }
                }
            })

            return@onUseWith true
        }
    }

    companion object {
        private val ITEM_IDS = StuffedItem.values.map { it.dropId }.toIntArray()
    }
}
private enum class StuffedItem(
    val dropId: Int,
    val stuffedId: Int,
    val price: Int,
    val message: String,
) {
    CRAWLING_HAND_DROP(
        dropId = Items.CRAWLING_HAND_7975,
        stuffedId = Items.CRAWLING_HAND_7982,
        price = 1000,
        message = "That's a very fine crawling hand.",
    ),
    COCKATRICE_HEAD_DROP(
        dropId = Items.COCKATRICE_HEAD_7976,
        stuffedId = Items.COCKATRICE_HEAD_7983,
        price = 2000,
        message = "A cockatrice! Beautiful, isn't it? Look at the plumage!",
    ),
    BASILISK_HEAD_DROP(
        dropId = Items.BASILISK_HEAD_7977,
        stuffedId = Items.BASILISK_HEAD_7984,
        price = 4000,
        message = "My, he's a scary-looking fellow, isn't he? He'll look good on your wall!",
    ),
    KURASK_HEAD_DROP(
        dropId = Items.KURASK_HEAD_7978,
        stuffedId = Items.KURASK_HEAD_7985,
        price = 6000,
        message = "A kurask? Splendid! Look at those horns!",
    ),
    ABYSSAL_HEAD_DROP(
        dropId = Items.ABYSSAL_HEAD_7979,
        stuffedId = Items.ABYSSAL_HEAD_7986,
        price = 12000,
        message = "Goodness, an abyssal demon! See how it's still glowing?  I'll have to use some magic to preserve that.",
    ),
    KBD_HEADS_DROP(
        dropId = Items.KBD_HEADS_7980,
        stuffedId = Items.KBD_HEADS_7987,
        price = 50000,
        message = "This must be a King Black Dragon! I'll have to get out my heavy duty tools, this skin's as tough as iron!",
    ),
    KQ_HEAD_DROP(
        dropId = Items.KQ_HEAD_7981,
        stuffedId = Items.KQ_HEAD_7988,
        price = 50000,
        message = "That must be the biggest kalphite I've ever seen! Preserving insects is always tricky. I'll have to be careful...",
    ),
    BIG_BASS_DROP(
        dropId = Items.BIG_BASS_7989,
        stuffedId = Items.BIG_BASS_7990,
        price = 1000,
        message = "That's a mighty fine sea bass you've caught there.",
    ),
    BIG_SWORDFISH_DROP(
        dropId = Items.BIG_SWORDFISH_7991,
        stuffedId = Items.BIG_SWORDFISH_7992,
        price = 2500,
        message = "Don't point that thing at me!",
    ),
    BIG_SHARK_DROP(
        dropId = Items.BIG_SHARK_7993,
        stuffedId = Items.BIG_SHARK_7994,
        price = 5000,
        message = "That's quite a fearsome shark! You've done everyone a service by removing it from the sea!",
    ),
    ;

    companion object {
        val values = enumValues<StuffedItem>()
        val product = values.associateBy { it.dropId }
    }
}
