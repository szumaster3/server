package content.region.misthalin.dialogue.lumbridge.tutor

import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class LachtopherDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.THE_LOST_TRIBE) == 10) {
            player("Do you know what happened in the cellar?").also { stage = 11 }
        } else {
            player("Hello there.")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Hello, I suppose. I'm Lachtopher. Could you lend me some money?",
                ).also { stage++ }
            1 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "Lend you money? I really don't think so. Don't you have any of your own?",
                ).also {
                    stage++
                }
            2 -> npcl(FaceAnim.HALF_GUILTY, "I spent it all and I can't be bothered to earn any more.").also { stage++ }
            3 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "Right, and you want my hard-earned money instead? No chance!",
                ).also { stage++ }
            4 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "You're just like my sister, Victoria. She won't give me any money.",
                ).also { stage++ }
            5 -> playerl(FaceAnim.HALF_GUILTY, "Your sister sounds like she has the right idea.").also { stage++ }
            6 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Yeah, I've heard it all before. 'Oh,' she says, 'It's easy to make money: just complete Tasks for cash.'",
                ).also {
                    stage++
                }
            7 -> playerl(FaceAnim.HALF_GUILTY, "Well, if you want to make money...").also { stage++ }
            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "That's just it. I don't want to make money. I just want to have money.",
                ).also {
                    stage++
                }
            9 ->
                playerl(
                    FaceAnim.HALF_GUILTY,
                    "I've had it with you! I don't think I've come across a less worthwhile person.",
                ).also {
                    stage++
                }
            10 ->
                playerl(FaceAnim.HALF_GUILTY, "I think I'll call you Lazy Lachtopher, from now on.").also {
                    stage =
                        END_DIALOGUE
                }
            11 -> npcl(FaceAnim.HALF_GUILTY, "No. What happened in the cellar?").also { stage++ }
            12 -> playerl(FaceAnim.HALF_GUILTY, "Well, part of the wall has collapsed.").also { stage++ }
            13 -> npcl(FaceAnim.HALF_GUILTY, "Good heavens! You'd better find out what happened!").also { stage++ }
            14 -> playerl(FaceAnim.HALF_GUILTY, "Well, yes, that's what I'm doing.").also { stage++ }
            15 -> npcl(FaceAnim.HALF_GUILTY, "Good! I know we're safe in your hands.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LACHTOPHER_7870)
    }
}
