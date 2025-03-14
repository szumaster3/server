package content.region.misthalin.dialogue.varrock

import core.api.*
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class BennyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        options(
            "Can I have a newspaper, please?",
            "How much does a paper cost?",
            "Varrock Herald? Never heard of it.",
            "Anything interesting in there?",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 -> player("Can I have a newspaper, please?").also { stage++ }
                    2 -> player("How much does a paper cost?").also { stage = 5 }
                    3 -> player("Varrock Herald? Never heard of it.").also { stage = 9 }
                    4 -> player("Anything interesting in there?").also { stage = 11 }
                }
            1 -> npc("Certainly, Guv. That'll be 50 gold pieces, please.").also { stage++ }
            2 -> options("Sure, here you go.", "Uh, no thanks, I've changed my mind").also { stage++ }
            3 ->
                when (buttonId) {
                    1 -> {
                        end()
                        if (!inInventory(player, Items.COINS_995, 50)) {
                            sendMessage(player, "You need 50 gold coins to buy a newspaper.")
                            return true
                        }
                        if (freeSlots(player) == 0) {
                            sendMessage(player, "You don't have enough inventory space.")
                            return true
                        }
                        if (removeItem(player, Item(Items.COINS_995, 50), Container.INVENTORY)) {
                            addItemOrDrop(player, Items.NEWSPAPER_11169, 1)
                        }
                        return true
                    }
                    2 -> player("No, thanks.").also { stage = 8 }
                }

            5 -> npc("Just 50 gold pieces! An absolute bargain! Want one?").also { stage++ }
            6 -> options("Yes, please.", "No, thanks.").also { stage++ }
            7 ->
                when (buttonId) {
                    1 -> player("Yes, please.").also { stage = 4 }
                    2 -> player("No, thanks.").also { stage = 8 }
                }
            8 -> npc("Ok, suit yourself. Plenty more fish in the sea.").also { stage = END_DIALOGUE }
            9 ->
                npc(
                    "For the illiterate amongst us, I shall elucidate. The",
                    "Varrock Herald is a new newspaper. It is edited, printed",
                    "and published by myself, Benny Gutenberg, and each",
                    "edition promises to enthrall the reader with captivating",
                ).also {
                    stage++
                }
            10 -> npc("material! Now, can I interest you in buying one", "for a mere 50 gold?").also { stage = 6 }
            11 ->
                npc(
                    "Of course there is, mate. Packed full of thought provoking",
                    "insights, contentious interviews and celebrity",
                    "scandalmongering! An excellent read and all for just 50",
                    "coins! Want one?",
                ).also {
                    stage =
                        6
                }
        }
        return false
    }

    override fun newInstance(player: Player): Dialogue {
        return BennyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BENNY_5925)
    }
}
