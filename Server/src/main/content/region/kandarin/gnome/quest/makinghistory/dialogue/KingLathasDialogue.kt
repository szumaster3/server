package content.region.kandarin.gnome.quest.makinghistory.dialogue

import content.region.kandarin.ardougne.east.quest.biohazard.dialogue.KingLathasBiohazardDialogue
import content.region.kandarin.gnome.quest.makinghistory.MHUtils
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

@Initializable
class KingLathasDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val questStage = getQuestStage(player, Quests.BIOHAZARD)
        val progress = getVarbit(player, MHUtils.PROGRESS)
        when {
            questStage in 16..100 -> end().also { openDialogue(player, KingLathasBiohazardDialogue()) }
            progress == 3 && inInventory(player, Items.LETTER_6756) -> npcl(FaceAnim.FRIENDLY, "What would you like to talk about?")
            inInventory(player, Items.LETTER_6757) -> npcl(FaceAnim.FRIENDLY, "Have you taken that letter to Jorral yet?").also { stage = 9 }
            !inInventory(player, Items.LETTER_6757) -> playerl(FaceAnim.FRIENDLY, "Excuse me sire, but I seem to have lost that letter you gave me.").also { stage = 10 }
            else -> {
                sendDialogue(player, "The King Lathas is not interested in talking.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        npc = NPC(NPCs.KING_LATHAS_364)
        when (stage) {
            0 -> options("Jorral and the outpost", "West Ardougne").also { stage++ }
            1 -> when (buttonId) {
                1 -> playerl(FaceAnim.FRIENDLY, "Jorral and the outpost").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "West Ardougne").also { stage++ }
            }
            2 -> playerl(FaceAnim.FRIENDLY, "Excuse me. I have been asked to hand you this from Jorral at the outpost.").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "I see.").also { stage++ }
            4 -> sendItemDialogue(player, Items.LETTER_6756, "The King reads the letter.").also { stage++ }
            5 -> npcl(FaceAnim.FRIENDLY, "I had no idea that place had any value at all! All this about my great-grandfather and Jorral's plans to make it into a museum makes for a convincing case.").also { stage++ }
            6 -> playerl(FaceAnim.FRIENDLY, "I am sure he only wants what is best.").also { stage++ }
            7 -> npcl(FaceAnim.FRIENDLY, "Very well, I will comply with his request. Take this letter back to him with my kind regards.").also { stage++ }
            8 -> {
                if (removeItem(player, Items.LETTER_6756)) {
                    end()
                    playerl(FaceAnim.FRIENDLY, "Thank you.")
                    addItemOrDrop(player, Items.LETTER_6757)
                    setQuestStage(player, Quests.MAKING_HISTORY, 99)
                }
            }
            9 -> playerl(FaceAnim.FRIENDLY, "I'm working on it!").also { stage = END_DIALOGUE }
            10 -> npcl(FaceAnim.FRIENDLY, "Very well, take another.").also { stage++ }
            11 -> {
                end()
                addItemOrDrop(player, Items.LETTER_6757)
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.KING_LATHAS_364)
}
