package content.region.karamja.quest.roots.dialogue

import core.api.setQuestStage
import core.api.setVarbit
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Vars

class GarthDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.GARTH_2330)
        when(stage) {
            0  -> playerl(FaceAnim.FRIENDLY, "Horacio has told me that you know a lot about taking root cuttings.").also { stage++ }
            1  -> npcl(FaceAnim.FRIENDLY, "Oo, oi. That I do.").also { stage++ }
            2  -> playerl(FaceAnim.FRIENDLY, "Erm... do you think you could pass on some of that knowledge to me?").also { stage++ }
            3  -> npcl(FaceAnim.FRIENDLY, "Oo, arr. That I could.").also { stage++ }
            4  -> playerl(FaceAnim.FRIENDLY, "I see. Please tell me how to take a root cutting from the Jade Vine over to the east of Shilo Village?").also { stage++ }
            5  -> npcl(FaceAnim.FRIENDLY, "See, you only have to ask properly and you shall gets. Now... let's get to the root of the problem.").also { stage++ }
            6  -> npcl(FaceAnim.FRIENDLY, "Root cuttings are very fragile, especially from that great vine. I've tried several times but never managed to keep the thing alive long enough. It doesn't seem to like to be exposed to the air much, so sealing it up some").also { stage++ }
            7  -> npcl(FaceAnim.FRIENDLY, "how would be good.").also { stage++ }
            8  -> playerl(FaceAnim.FRIENDLY, "Yes, Horacio said that, so I went to Wizard Cromperty and he told me about this magical preservation device.").also { stage++ }
            9  -> npcl(FaceAnim.FRIENDLY, "Oo, he is a wonderful wizard.").also { stage++ }
            10 -> playerl(FaceAnim.FRIENDLY, "Oh, yes, he's a wonderful wizard because...").also { stage++ }
            11 -> npcl(FaceAnim.FRIENDLY, "Because?").also { stage++ }
            12 -> playerl(FaceAnim.FRIENDLY, "Because... he reminded me of this pot lid to seal the cutting away in a pot.").also { stage++ }
            13 -> npcl(FaceAnim.FRIENDLY, "Oo, oi. That looks about right, though you might need to try several times to find a cutting that will take. Okay, tools you'll need for taking the cutting would be a plant pot full of earth like you can use for trees,").also { stage++ }
            14 -> npcl(FaceAnim.FRIENDLY, "secateurs and obviously a spade to dig down to the roots. You'll also need to do this right close to the main trunk; I had no success at all with the smaller roots further out. That might prove a bit of a problem.").also { stage++ }
            15 -> playerl(FaceAnim.FRIENDLY, "Oh? What's the problem?").also { stage++ }
            16 -> npcl(FaceAnim.FRIENDLY, "Well, you might have a problem getting to the main roots now, it's been a while since I last tried... and even then it wasn't easy at all. You'll definitely need yer wood hatchet and something to hack down the jungle.").also { stage++ }
            17 -> npcl(FaceAnim.FRIENDLY, "It's kind of wild and overgrown - make sure you have an escape root.").also { stage++ }
            18 -> playerl(FaceAnim.FRIENDLY, "Oh dear. I should go prepared then.").also { stage++ }
            19 -> npcl(FaceAnim.FRIENDLY, "Oo, oi. Most definitely. You'll have problems if you're not prepared. Just like soil if it's not been weeded. Don't forget to seal that cutting away as soon as it takes, else you'll be needing to go back there again.").also {
                setVarbit(player!!, Vars.VARBIT_QUEST_BACK_TO_MY_ROOTS_PROGRESS_4055, 35, true)
                setQuestStage(player!!, Quests.BACK_TO_MY_ROOTS, 6)
                stage++
            }
        }
    }
}