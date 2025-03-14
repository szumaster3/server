package content.region.kandarin.dialogue.stronghold

import core.api.addItemOrDrop
import core.api.interaction.openNpcShop
import core.api.removeItem
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class BarmanGrandTreeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.OLD_NORMAL, "Good day to you. What can I get you to drink?").also { stage = 0 }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("What do you have?", "Nothing thanks.", "Can I buy some ingredients?").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "What do you have?").also { stage = 2 }
                    2 -> playerl(FaceAnim.NEUTRAL, "Nothing thanks.").also { stage = 4 }
                    3 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "I was just wanting to buy a cocktail ingredient actually.",
                        ).also {
                            stage =
                                5
                        }
                }
            2 -> npcl(FaceAnim.OLD_NORMAL, "Here, take a look at our menu.").also { stage++ }
            3 -> {
                end()
                openNpcShop(player, NPCs.BARMAN_849)
            }
            4 -> npcl(FaceAnim.OLD_NORMAL, "Okay, take it easy.").also { stage = END_DIALOGUE }
            5 -> npcl(FaceAnim.OLD_NORMAL, "Sure thing, what did you want?").also { stage++ }
            6 -> options("A lemon.", "An orange.", "A cocktail shaker.", "Nothing thanks.").also { stage++ }
            7 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "A lemon.").also { stage = 8 }
                    2 -> playerl(FaceAnim.FRIENDLY, "An orange.").also { stage = 10 }
                    3 -> playerl(FaceAnim.FRIENDLY, "A cocktail shaker.").also { stage = 12 }
                    4 -> playerl(FaceAnim.NEUTRAL, "Nothing thanks.").also { stage = 14 }
                }

            8 -> npcl(FaceAnim.OLD_NORMAL, "20 coins please.").also { stage++ }
            9 ->
                if (!removeItem(player, Item(Items.COINS_995, 20))) {
                    playerl(FaceAnim.FRIENDLY, "Sorry, I don't have enough coins for that.").also {
                        stage = END_DIALOGUE
                    }
                } else {
                    end()
                    addItemOrDrop(player, Items.LEMON_2102)
                }

            10 -> npcl(FaceAnim.OLD_NORMAL, "20 coins please.").also { stage++ }
            11 ->
                if (!removeItem(player, Item(Items.COINS_995, 20))) {
                    playerl(FaceAnim.FRIENDLY, "Sorry, I don't have enough coins for that.").also {
                        stage = END_DIALOGUE
                    }
                } else {
                    end()
                    addItemOrDrop(player, Items.ORANGE_2108)
                }

            12 -> npcl(FaceAnim.OLD_NORMAL, "20 coins please.").also { stage++ }
            13 ->
                if (!removeItem(player, Item(Items.COINS_995, 20))) {
                    playerl(FaceAnim.FRIENDLY, "Sorry, I don't have enough coins for that.").also {
                        stage = END_DIALOGUE
                    }
                } else {
                    end()
                    addItemOrDrop(player, Items.COCKTAIL_SHAKER_2025)
                }

            14 -> playerl(FaceAnim.FRIENDLY, "Actually nothing thanks.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BarmanGrandTreeDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARMAN_849)
    }
}
