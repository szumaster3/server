package content.region.karamja.tzhaar.dialogue

import core.api.*
import core.game.dialogue.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the TzHaarMejJeh dialogue.
 */
@Initializable
class TzHaarMejJehDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.CHILD_GUILTY, "You want help JalYt-Ket-" + player.username + "?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val jadPracticeSettings = settings!!.jad_practice_enabled
        val jadPractice = getAttribute(player, "fc_practice_jad", false)
        val hasAnFireCape = inInventory(player, Items.FIRE_CAPE_6570)
        when (stage) {
            0 -> showTopics(
                IfTopic(FaceAnim.HALF_GUILTY, "I have a fire cape here.", 1, jadPracticeSettings && jadPractice && hasAnFireCape, skipPlayer = false),
                IfTopic(FaceAnim.HALF_GUILTY, "What is this place?", 2, !hasAnFireCape, skipPlayer = false),
                Topic(FaceAnim.HALF_GUILTY, "What did you call me?", 11, skipPlayer = false),
                Topic(FaceAnim.HALF_GUILTY, if(jadPracticeSettings && jadPractice) "About my challenge..." else "The challenge is too long. I want to challenge Jad directly.", if(jadPracticeSettings && jadPractice) 27 else 15, skipPlayer = false),
                Topic(FaceAnim.HALF_GUILTY, "No I'm fine thanks.", END_DIALOGUE)
            )
            1 -> end().also {interpreter.open(DialogueInterpreter.getDialogueKey("firecape-exchange"), npc) }
            2 -> player(FaceAnim.HALF_GUILTY, "What is this place?").also { stage++ }
            3 -> npc(FaceAnim.CHILD_FRIENDLY, "This is the fight caves, TzHaar-Xil made it for practice,", "but many JalYt come here to fight too.", "Just enter the cave and make sure you're prepared.").also { stage++ }
            4 -> showTopics(
                Topic(FaceAnim.HALF_GUILTY, "Are there any rules?", 5,false),
                Topic(FaceAnim.HALF_GUILTY, "Ok thanks.", END_DIALOGUE, false)
            )
            5 -> npc(FaceAnim.CHILD_GUILTY, "Rules? Survival is the only rule in there.").also { stage++ }
            6 -> showTopics(
                Topic(FaceAnim.HALF_GUILTY, "Do I win anything?", 7, false),
                Topic(FaceAnim.HALF_GUILTY, "Sounds good.", END_DIALOGUE, false)
            )
            7 -> npc(FaceAnim.CHILD_GUILTY, "You ask a lot of questions.", "Might give you TokKul if you last long enough.").also { stage++ }
            8 -> player(FaceAnim.HALF_GUILTY, "...").also { stage++ }
            9 -> npc(FaceAnim.CHILD_GUILTY, "Before you ask, TokKul is like your Coins.").also { stage++ }
            10 -> npc(FaceAnim.CHILD_GUILTY, "Gold is like you JalYt, soft and easily broken, we use", "hard rock forged in fire like TzHaar!").also { stage = END_DIALOGUE }
            11 -> npc(FaceAnim.CHILD_GUILTY, "Are you not JalYt-Ket?").also { stage++ }
            12 -> showTopics(
                Topic(FaceAnim.HALF_GUILTY, "What's a 'JalYt-Ket'?", 13, false),
                Topic(FaceAnim.HALF_GUILTY, "I guess so...", END_DIALOGUE, false),
                Topic(FaceAnim.HALF_GUILTY, "No I'm not!", END_DIALOGUE, false)
            )
            13 -> npc(FaceAnim.CHILD_GUILTY, "That what you are... you tough and strong no?").also { stage++ }
            14 -> player(FaceAnim.HALF_GUILTY, "Well yes I suppose I am...").also { stage = END_DIALOGUE }
            15 -> npc(FaceAnim.CHILD_SUSPICIOUS, "I thought you strong and tough", "but you want skip endurance training?").also { stage++ }
            16 -> npc(FaceAnim.CHILD_GUILTY, "Maybe you not JalYt-Ket afterall.").also { stage++ }
            17 -> showTopics(
                Topic(FaceAnim.HALF_GUILTY, "I don't have time for it, man.", 19, false),
                Topic(FaceAnim.HALF_GUILTY, "No, I'm JalYt-Ket!", 18, true),
            )
            18 -> player(FaceAnim.HALF_GUILTY, "No, I'm JalYt-Ket! I swear!", "I'll do the training properly.").also { stage = END_DIALOGUE }
            19 -> npc(FaceAnim.CHILD_GUILTY, "JalYt, you know you not get reward", "if you not do training properly, ok?").also { stage++ }
            20 -> showTopics(
                Topic(FaceAnim.HALF_GUILTY, "That's okay, I don't need a reward.", 21, true),
                Topic(FaceAnim.HALF_GUILTY, "Oh, nevermind then.", END_DIALOGUE, true),
            )
            21 -> player(FaceAnim.NEUTRAL, "I just wanna fight the big guy.").also { stage++ }
            22 -> npc(FaceAnim.CHILD_FRIENDLY, "Okay JalYt.", "TzTok-Jad not show up for just anyone.").also { stage++ }
            23 -> npc(FaceAnim.CHILD_FRIENDLY, "You give 8000 TokKul, TzTok-Jad know you serious.", "You get it back if you victorious.").also { stage++ }
            24 -> showTopics(
                Topic(FaceAnim.HALF_GUILTY, "That's fair, here's 8000 TokKul.", 25, true),
                Topic(FaceAnim.HALF_GUILTY, "I don't have that much on me, but I'll go get it.", END_DIALOGUE, false),
                Topic(FaceAnim.HALF_GUILTY, "TzTok-Jad must be old and tired to not just accept my challenge.", 26, false)
            )
            25 -> if(!removeItem(player, Item(Items.TOKKUL_6529, 8000))) {
                end().also { npc(FaceAnim.CHILD_FRIENDLY, "JalYt, you not have the TokKul.", "You come back when you are serious.").also { stage = END_DIALOGUE } }
            } else {
                player(FaceAnim.HALF_GUILTY, "That's fair, here's 8000 TokKul.").also { stage = 28 }
            }
            26 -> npc(FaceAnim.CHILD_FRIENDLY, "JalYt-Mor, you the old and tired one.", "You the one not want to do proper training.").also { stage = END_DIALOGUE }
            27 -> npc(FaceAnim.CHILD_FRIENDLY, "TzTok-Jad is waiting for you.", "Do not make TzTok-Jad wait long.").also { stage = END_DIALOGUE }
            28 -> {
                end()
                npc(FaceAnim.CHILD_FRIENDLY, "Okay JalYt. Enter cave when you are prepared.", "You find TzTok-Jad waiting for JalYt challenger.")
                setAttribute(player, "fc_practice_jad", true)
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(
        DialogueInterpreter.getDialogueKey("tzhaar-mej"),
        NPCs.TZHAAR_MEJ_JAL_2617
    )
}
