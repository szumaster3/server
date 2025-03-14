package content.region.wilderness.handlers.castle

import core.api.*
import core.api.quest.hasRequirement
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class RoguesJewelleryListener : InteractionListener {
    private val jewelleryIDs = RoguesJewellery.values().map { it.item }.toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.NPC, jewelleryIDs, NPCs.ROGUE_8122) { player, used, _ ->
            if (!hasRequirement(player, Quests.SUMMERS_END)) return@onUseWith false
            val jewellery = RoguesJewellery.JewelleryMap[used.id] ?: return@onUseWith true
            val invAmount = amountInInventory(player, jewellery.item)
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        npc = NPC(NPCs.ROGUE_8122)
                        when (stage) {
                            0 -> {
                                face(findNPC(NPCs.ROGUE_8122)!!, player, 2)
                                npcl(
                                    FaceAnim.HALF_ASKING,
                                    "I'll give you ${jewellery.price * invAmount} coins each for that ${getItemName(
                                        used.id,
                                    )}. Do we have a deal?",
                                ).also {
                                    stage++
                                }
                            }
                            1 -> options("Yes, we do.", "No, we do not.").also { stage++ }
                            2 ->
                                when (buttonID) {
                                    1 -> player("Yes, we do.").also { stage = 3 }
                                    2 -> player("No, we do not.").also { stage = END_DIALOGUE }
                                }
                            3 -> {
                                if (invAmount >= 10000) {
                                    end()
                                    npcl(
                                        FaceAnim.FRIENDLY,
                                        "Whoa, that's quite a bit of jewellery you've got there! Please try to keep it in amounts smaller than 10000. Big numbers make my head hurt.",
                                    )
                                } else if (freeSlots(player) < 1) {
                                    end()
                                    sendMessage(player, "You don't have enough inventory space for that.")
                                } else if (removeItem(player, Item(jewellery.item, invAmount), Container.INVENTORY)) {
                                    end()
                                    addItem(player, Items.COINS_995, invAmount * jewellery.price)
                                    npcl(
                                        FaceAnim.FRIENDLY,
                                        "It was a pleasure doing business with you. Come back if you have more jewellery to sell.",
                                    )
                                } else {
                                    end()
                                    npcl(
                                        FaceAnim.HALF_ASKING,
                                        "Sorry, but I don't deal with those. I only trade unenchanted gold jewellery, nothing else. Bring notes if you like, but I prefer the real deal.",
                                    )
                                }
                            }
                        }
                    }
                },
                NPCs.ROGUE_8122,
            )
            return@onUseWith true
        }
    }
}
