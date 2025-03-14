package content.region.asgarnia.dialogue.trollheim

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
class KnightGWDDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if ((getVarp(player, 1048) and 16) != 0) {
            sendMessage(player, "The knight has already died.")
            return false
        }
        player("Who are you? What are you doing here in the snow?")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    "My name is...Sir Gerry. I am...a member of a",
                    "secret...society of knights. My time is short and I",
                    "need...your help.",
                ).also {
                    stage++
                }
            1 ->
                player(
                    "A secret society of knights? What a surprise! Is there",
                    "an old charter or decree that says if you're a knight",
                    "you have to belong to a secret order?",
                ).also {
                    stage++
                }
            2 ->
                npc(
                    "I'm sorry, my friend... I do not understand your",
                    "meaning. Please, time is short... Take this scroll to Sir",
                    "Tiffy. You will find him in Falador park... You should",
                    "not...read it... It contains information for his eyes only.",
                ).also {
                    stage++
                }
            3 -> {
                end()
                if (player.inventory.add(Item(Items.KNIGHTS_NOTES_11734))) {
                    sendItemDialogue(player, Item(Items.KNIGHTS_NOTES_11734), "The knight hands you a scroll.")
                    setVarbit(player, 3936, 1, true)
                } else {
                    sendDialogue(player, "The knight tries to give you something, but your inventory is full.").also {
                        stage =
                            END_DIALOGUE
                    }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KNIGHT_6202)
    }
}
