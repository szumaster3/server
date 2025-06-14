package content.minigame.vinesweeper.plugin

import core.api.getAttribute
import core.api.setAttribute
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items

abstract class FlagsHandler : DialogueFile() {
    companion object {
        val BLINKIN_FLAG_LINES =
            arrayOf(
                "Let me check for ya.",
                "Flags? It appears ya don't have enough room for 'em. Make some space and talk to me again.",
                "Ah! First things first. One of the farm lads dropped off some flags for ya. Ya can have them back. Here ya go.",
                "Righty-ho! Ya can have a total of 10 flags. To get yerself a full set of flags'll cost ya %d gold pieces. Would ya like to buy these flags?",
                "Here ya go, then.",
                "Flags? It appears ya don't have enough room for 'em. Make some space and talk to me again.",
                "Ya don't have the coins fer these, I'm afraid! Come back when yer a little bit richer p'raps?",
                "Right y'are then! See ya.",
                "It looks like ya got all the flags ya need right now. Ya don't need to buy any more.",
            )
        val WINKIN_FLAG_LINES =
            arrayOf(
                "Let me check for you.",
                "I'm sorry dear, you don't appear to have enough room. Make some space and talk to me again.",
                "Ah! First things first. One of the farmers dropped off some flags for you. You can have them back. Here you go.",
                "Alright. You can have a total of 10 flags. To obtain a full set of flags will cost you %d coins. Would you like to buy these flags?",
                "Here you are then, dear.",
                "I'm sorry dear, you don't appear to have enough room. Make some space and talk to me again.",
                "I'm afraid it looks like you don't have enough money, dear. Come back and see me again when you have a bit more.",
                "Ok, dear. Goodbye.",
                "It looks like you have all the flags you need. You don't need to buy any more.",
            )

        enum class FARMER_FLAG_LINES(
            val line: String,
        ) {
            FIND_FLAG("Ah, another flag to clear. Let's see what's there."),
            FIND_SEED("Ah! A seed. Points for everyone near me!"),
            NO_SEED(
                "Hmm, no seeds planted here, I'm afraid.",
            ),
            KEEP_FLAG("I'll have to keep this 'ere flag. Sorry."),
            FIND_PLANT("Hmm. Looks like there's a plant here."),
            DEAD_PLANT(
                "Gracious me! This one's dead",
            ),
        }
    }

    fun handleFlags(
        componentID: Int,
        buttonID: Int,
        lines: Array<String>,
    ) {
        when (stage) {
            20 -> npcl(FaceAnim.OLD_NORMAL, lines[0]).also { stage++ }
            21 -> {
                val flags = getAttribute(player!!, "vinesweeper:stored-flags", 10)
                if (flags > 0) {
                    if (!player!!.inventory.add(Item(Items.FLAG_12625, flags))) {
                        npcl(FaceAnim.OLD_NORMAL, lines[1])
                        stage = END_DIALOGUE
                    } else {
                        setAttribute(player!!, "/save:vinesweeper:stored-flags", 0)
                        npcl(FaceAnim.OLD_NORMAL, lines[2])
                        stage++
                    }
                } else {
                    stage++
                    handle(componentID, buttonID)
                }
            }

            22 -> {
                val flags = player!!.inventory.getAmount(Items.FLAG_12625)
                if (flags < 10) {
                    val price = 500 * (10 - flags)
                    npcl(FaceAnim.OLD_NORMAL, String.format(lines[3], price))
                    stage = 220
                } else {
                    stage = 23
                    handle(componentID, buttonID)
                }
            }

            220 -> options("Yes, please.", "No, thanks").also { stage++ }
            221 ->
                when (buttonID) {
                    1 -> playerl("Yes, please.").also { stage = 222 }
                    2 -> playerl("No, thanks.").also { stage = 223 }
                }

            222 -> {
                val flags = player!!.inventory.getAmount(Items.FLAG_12625)
                val price = Item(Items.COINS_995, 500 * (10 - flags))
                if (player!!.inventory.containsItem(price) && player!!.inventory.remove(price)) {
                    if (player!!.inventory.add(Item(Items.FLAG_12625, 10 - flags))) {
                        npcl(FaceAnim.OLD_NORMAL, lines[4])
                        stage = 22
                        stage = END_DIALOGUE
                    } else {
                        npcl(FaceAnim.OLD_NORMAL, lines[5])

                        player!!.inventory.add(price)
                        stage = END_DIALOGUE
                    }
                } else {
                    npcl(FaceAnim.OLD_NORMAL, lines[6])
                    stage = END_DIALOGUE
                }
            }

            223 -> npcl(FaceAnim.OLD_NORMAL, lines[7]).also { stage = END_DIALOGUE }
            23 -> npcl(FaceAnim.OLD_NORMAL, lines[8]).also { stage = END_DIALOGUE }
        }
    }
}
