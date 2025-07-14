package content.region.desert.alkharid.quest.feud.dialogue

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.shops.Shops
import core.game.world.GameWorld
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Vars

/**
 * Represents the Ali Morrisane dialogue (The Feud quest).
 */
class AliMorrisaneFeudDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.ALI_MORRISANE_1862)
        when(stage) {
            0 -> npc(FaceAnim.FRIENDLY, "Well one can only do and sell so much. If I had more", "staff I'd be able to sell more, rather than waste my", "time on menial things I could get on with selling sand", "to the Bedabin and useless tourist trinkets to everyone.").also { stage++ }
            1 -> options("I'm far too busy - adventuring is a full time job you know.", "I'd like to help you but.....").also { stage++ }
            2 -> when (buttonID) {
                1 -> playerl(FaceAnim.FRIENDLY, "I'm far too busy - adventuring is a full time job you know.").also { stage = END_DIALOGUE }
                2 -> playerl(FaceAnim.FRIENDLY, "I'd like to help you but.....").also { stage = 4 }
            }
            3 -> npcl(FaceAnim.FRIENDLY, "No problem my friend, perhaps another time.").also { stage = 11 }
            4 -> npc(FaceAnim.FRIENDLY, "Yes I know, I know - the life of a shop keeper isn't", "slaying dragons and wooing damsels but it has its", "charms.").also { stage++ }
            5 -> npc(FaceAnim.FRIENDLY, "Although you can help me in another way, a nephew of", "mine lives not too far from here in a little town called", "Pollnivneach. If you could fetch him here I'll give you", "the first month of his salary!").also { stage++ }
            6 -> options("I'm far too busy - adventuring is a full time job you know.", "I'll find you your help.").also { stage++ }
            7 -> when (buttonID) {
                1 -> playerl(FaceAnim.FRIENDLY, "I'm far too busy to carry out errands for shopkeepers - maybe later.").also { stage = END_DIALOGUE }
                2 -> playerl(FaceAnim.FRIENDLY, "I'll find you your help.").also { stage++ }
            }
            8 -> player("But you had better become the biggest merchant in", "${GameWorld.settings?.name} once I succeed!").also { stage++ }
            9 -> npc(FaceAnim.FRIENDLY, "Ah, many thanks, my friend. My nephew's name is 'Ali'", "and he lives to the south in a town called 'Pollnivneach'.").also { stage++ }
            10 -> npc(FaceAnim.FRIENDLY, "Tell him uncle Ali M needs his help up in Al Kharid.").also { stage++ }
            11 -> npcl(FaceAnim.FRIENDLY, "Now have a look at my wares.").also { stage++ }
            12 -> options("No, I'm really too busy.", "Okay.").also { stage++ }
            13 -> when (buttonID) {
                1 -> playerl(FaceAnim.FRIENDLY, "No, I'm really too busy.").also { stage = END_DIALOGUE }
                2 -> {
                    end()
                    Shops.openId(player!!, 107)
                    sendMessage(player!!, "Despite your best efforts Ali M still manages to sell you some junk.")
                    setVarbit(player!!, Vars.VARBIT_THE_FEUD_PROGRESS_334, 1, true)
                }
            }

        }
    }
}