package content.minigame.vinesweeper.dialogue

import content.minigame.vinesweeper.plugin.FlagsHandler
import core.api.openDialogue
import core.game.component.Component
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class MrsWinkinDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        openDialogue(player!!, MrsWinkinDialogueFile(), npc)
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean = true

    override fun getIds(): IntArray = intArrayOf(NPCs.MRS_WINKIN_7132)
}

class MrsWinkinDialogueFile : FlagsHandler() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npcl(FaceAnim.OLD_NORMAL, "Oh, hello there, dear. How can I help you?").also { stage++ }
            1 -> options(
                "Where are we?",
                "Have you got any flags?",
                "Do you have a spare spade?",
                "Do you have anything for trade?",
                "Nothing. I'm fine, thanks.",
            ).also { stage++ }

            2 -> when (buttonID) {
                1 -> playerl("Where are we?").also { stage = 10 }
                2 -> playerl("Have you got any flags?").also { stage = 20 }
                3 -> playerl("Do you have a spare spade?").also { stage = 30 }
                4 -> playerl("Do you have anything for trade?").also { stage = 40 }
                5 -> playerl("Nothing. I'm fine, thanks.").also { stage = 51 }
            }

            10 -> npcl(FaceAnim.OLD_NORMAL, "This is Winkin's Farm, dear.").also { stage++ }
            11 -> playerl("Oh, I see. So where is Mr. Winkin?").also { stage++ }
            12 -> npcl(FaceAnim.OLD_NORMAL, "Oh, he headed off to the market a while back. We look after the farm while he's gone.").also { stage++ }
            13 -> npcl(FaceAnim.OLD_NORMAL, "Now, was there anything else you wanted?").also { stage = 1 }
            20, 21, 22, 220, 221, 222, 223, 23 -> handleFlags(componentID, buttonID, WINKIN_FLAG_LINES)
            30 -> npcl(FaceAnim.OLD_NORMAL, "Why, of course. I can sell you one for 5 gold pieces.").also { stage++ }
            31 -> options("Okay, thanks.", "Actually, I've changed my mind.").also { stage++ }
            32 -> when (buttonID) {
                1 -> playerl("Okay, thanks.").also { stage = 320 }
                2 -> playerl("Actually, I've changed my mind.").also { stage = 330 }
            }
            320 -> {
                val price = Item(Items.COINS_995, 5)
                if (player!!.inventory.containsItem(price) && player!!.inventory.remove(price)) {
                    npcl(FaceAnim.OLD_NORMAL, "Here you are, then.")
                    val spade = Item(Items.SPADE_952)
                    if (!player!!.inventory.add(spade)) {
                        GroundItemManager.create(spade, player)
                    }
                    stage = END_DIALOGUE
                } else {
                    npcl(
                        FaceAnim.OLD_NORMAL,
                        "I'm afraid it looks like you don't have enough money, dear. Come back and see me again when you have a bit more.",
                    )
                    stage = END_DIALOGUE
                }
            }
            330 -> npcl(FaceAnim.OLD_NORMAL, "Okay then.").also { stage = END_DIALOGUE }
            40 -> npcl(FaceAnim.OLD_NORMAL, "Of course.").also { stage++ }
            41 -> {
                end()
                player!!.interfaceManager.open(Component(686))
            }
            51 -> npcl(FaceAnim.OLD_NORMAL, "Okay, dear.").also { stage = END_DIALOGUE }
        }
    }
}
