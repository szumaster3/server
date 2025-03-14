package content.region.misc.dialogue.keldagrim

import core.api.addItemOrDrop
import core.api.inInventory
import core.api.removeItem
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class BarmaidDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Welcome to the Laughing Miner pub, human traveller.",
                ).also { stage++ }
            1 ->
                showTopics(
                    Topic(FaceAnim.FRIENDLY, "Who is that man walking around outside?", 2),
                    Topic(FaceAnim.FRIENDLY, "I'd like a beer please.", 12),
                    Topic(FaceAnim.FRIENDLY, "I'd like some food please.", 16),
                )
            2 -> npcl(FaceAnim.OLD_DEFAULT, "What man?").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "I mean the dwarf, the one with the sign.").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Oh, him. Yes, we employ him to advertise our pub, he's the cheapest labour we could find. We don't have a lot of money to spare you know, we pay him in beer.",
                ).also {
                    stage++
                }
            5 -> playerl(FaceAnim.FRIENDLY, "But what's wrong with him?").also { stage++ }
            6 -> npcl(FaceAnim.OLD_DEFAULT, "Well, he's drunk isn't he?").also { stage++ }
            7 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "I told you, he's cheap. He couldn't get any other work since the Red Axe fired him. Been drinking ever since.",
                ).also {
                    stage++
                }
            8 -> playerl(FaceAnim.FRIENDLY, "How did that happen?").also { stage++ }
            9 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "I'm not quite sure, I think he kept wearing the wrong coloured cap all the time. They don't much like it if you don't wear your red uniform into work.",
                ).also {
                    stage++
                }
            10 -> playerl(FaceAnim.FRIENDLY, "They fired him for that??").also { stage++ }
            11 ->
                npcl(FaceAnim.OLD_DEFAULT, "The Red Axe will fire you for just about anything if they want to.").also {
                    stage =
                        END_DIALOGUE
                }
            12 -> npcl(FaceAnim.OLD_DEFAULT, "That'll be 2 gold coins.").also { stage++ }
            13 ->
                showTopics(
                    Topic(FaceAnim.FRIENDLY, "Pay.", 14, true),
                    Topic(FaceAnim.FRIENDLY, "Don't pay.", 15, true),
                )
            14 -> {
                end()
                if (!inInventory(player, Items.COINS_995, 2)) {
                    playerl(FaceAnim.FRIENDLY, "Sorry, I don't have 2 coins on me.")
                    stage = END_DIALOGUE
                } else {
                    if (removeItem(player, Item(Items.COINS_995, 2))) {
                        addItemOrDrop(player, Items.BEER_1917)
                        npcl(FaceAnim.OLD_DEFAULT, "Thanks for your custom.")
                        stage = END_DIALOGUE
                    }
                }
            }

            15 -> playerl(FaceAnim.FRIENDLY, "Sorry, I changed my mind.").also { stage = END_DIALOGUE }
            16 -> npcl(FaceAnim.OLD_DEFAULT, "I can make you a stew for 20 gold coins.").also { stage++ }
            17 ->
                showTopics(
                    Topic(FaceAnim.FRIENDLY, "Pay.", 18, true),
                    Topic(FaceAnim.FRIENDLY, "Don't pay.", 19, true),
                )
            18 -> {
                end()
                if (!removeItem(player, Item(Items.COINS_995, 20))) {
                    playerl(FaceAnim.FRIENDLY, "Sorry, I don't have 20 coins on me.")
                } else {
                    addItemOrDrop(player, Items.STEW_2003)
                    npcl(FaceAnim.OLD_DEFAULT, "Thanks for your custom.")
                }
            }
            19 -> playerl(FaceAnim.FRIENDLY, "Sorry, I changed my mind.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return BarmaidDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARMAID_2178)
    }
}
