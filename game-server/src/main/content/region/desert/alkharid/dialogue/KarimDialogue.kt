package content.region.desert.alkharid.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the Karim dialogue.
 */
@Initializable
class KarimDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.HALF_ASKING, "Would you like to buy a nice kebab? Only one gold.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("I think I'll give it a miss.", "Yes please.", "What do you think of Ali Morrisane?").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(FaceAnim.HALF_GUILTY, "I think I'll give it a miss.").also { stage = END_DIALOGUE }
                2 -> player(FaceAnim.HAPPY, "Yes please.").also { stage++ }
                3 -> player(FaceAnim.HALF_ASKING, "What do you think of Ali Morrisane?").also { stage = 3 }
            }
            2 -> {
                if (freeSlots(player) == 0 && amountInInventory(player, Items.COINS_995) > 1) {
                    player(FaceAnim.SAD, "I don't have enough room, sorry.")
                    stage = END_DIALOGUE
                } else if (!removeItem(player, Item(Items.COINS_995, 1), Container.INVENTORY)) {
                    sendMessage(player, "You can't afford that.")
                    stage = 11
                } else {
                    end()
                    addItem(player, Items.KEBAB_1971, 1, Container.INVENTORY)
                    sendMessage(player, "You buy a kebab.")
                }
            }
            3 -> npcl(FaceAnim.HALF_GUILTY, "I don't like him. He makes all these claims about being the best salesman in the world, and he even has some thugs that he's hired to threaten us.").also { stage++ }
            4 -> npcl(FaceAnim.HALF_GUILTY, "One of them came round here and threatened to cut my head off if I didn't cooperate with them!").also { stage++ }
            5 -> npcl(FaceAnim.HALF_GUILTY, "I've also heard some rumours about them from my friend Isma'il in Pollnivneach...").also { stage++ }
            6 -> player(FaceAnim.HALF_ASKING, "What sort of rumours?").also { stage++ }
            7 -> npcl(FaceAnim.HALF_GUILTY, "Apparently he has men there as well, who have also been threatening people.").also { stage++ }
            8 -> npcl(FaceAnim.HALF_GUILTY, "And it's said that they even intend to steal from the ruler of Al Kharid himself!").also { stage++ }
            9 -> player(FaceAnim.HALF_ASKING, "Really?").also { stage++ }
            10 -> npcl(FaceAnim.HALF_ASKING, "That's what I've heard. Anyway, are you sure you wouldn't like a kebab?").also { stage = 0 }
            11 -> npcl(FaceAnim.NEUTRAL, "Come back when you have some.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = KarimDialogue(player)
    override fun getIds(): IntArray = intArrayOf(NPCs.KARIM_543)
}
