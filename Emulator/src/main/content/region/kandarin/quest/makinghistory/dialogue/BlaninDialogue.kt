package content.region.kandarin.quest.makinghistory.dialogue

import content.region.kandarin.quest.makinghistory.handlers.MakingHistoryUtils
import core.api.getVarbit
import core.api.quest.getQuestStage
import core.api.sendDialogue
import core.api.setVarbit
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class BlaninDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        val questStage = getQuestStage(player, Quests.MAKING_HISTORY)
        val dronProgress = getVarbit(player, MakingHistoryUtils.DRON_PROGRESS)
        when {
            questStage < 1 -> playerl(FaceAnim.FRIENDLY, "Excuse me.")

            questStage >= 1 ->
                playerl(FaceAnim.FRIENDLY, "Hello there. Are you the brother of Dron?").also {
                    stage = 1
                }

            dronProgress == 2 -> playerl(FaceAnim.FRIENDLY, "Excuse me.").also { stage = 13 }

            dronProgress == 4 -> playerl(FaceAnim.FRIENDLY, "That's one less thing to worry about.").also { stage = 20 }
            else -> sendDialogue(player, "Blanin seems too busy to talk.")
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
                    FaceAnim.FRIENDLY,
                    "Look, I don't have time for weaklings, if you want conversation, talk to my brother Blanin!",
                ).also { stage = END_DIALOGUE }

            1 -> npcl(FaceAnim.FRIENDLY, "That I am. Why? Has he killed one of your family?").also { stage++ }
            2 -> playerl(FaceAnim.FRIENDLY, "Not that I know of...").also { stage++ }
            3 -> npcl(FaceAnim.FRIENDLY, "Oh good, how can I help you?").also { stage++ }
            4 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Well I'd like to talk to your brother Dron about the outpost north of Ardougne.",
                ).also { stage++ }

            5 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I'm afraid he's not easy to talk to, so it's good that you came to see me. You'll need to remember a few things when talking to him.",
                ).also { stage++ }

            6 -> playerl(FaceAnim.FRIENDLY, "Like?").also { stage++ }
            7 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You must be firm with him and don't mention that I sent you. In case he asks, he wields an iron mace in battle, eats rats for breakfast, kittens for lunch and bunnies for tea!",
                ).also { stage++ }

            8 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "His favorite drink is red spider blood, he's 36 years, 8 months and 21 days old, studies famous battles of the Fourth and Fifth ages,",
                ).also { stage++ }

            9 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "lives on the northeast side of town and his, erm... pet cat is called Fluffy.",
                ).also { stage++ }

            10 -> playerl(FaceAnim.FRIENDLY, "O...kay....").also { stage++ }
            11 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I know this sounds strange but Dron won't talk to anyone unless they know him well - he's a secretive guy.",
                ).also { stage++ }

            12 -> {
                end()
                setVarbit(player, MakingHistoryUtils.DRON_PROGRESS, 3, true)
            }

            13 -> npcl(FaceAnim.FRIENDLY, "Making progress?").also { stage++ }
            14 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Not the best. Can you give me those hints again for speaking with Dron?",
                ).also { stage++ }

            15 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Sure. You must be firm with him and don't mention that I sent you. In case he asks, he wields an iron mace in battle, eats rats for breakfast,",
                ).also { stage++ }

            16 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "kittens for lunch and bunnies for His favorite drink is red spider blood, he's 36 years, 8 months and 21 days old, he studies famous battles of the Fourth and Fifth ages,",
                ).also { stage++ }

            17 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "lives on the North East side of town and his, erm... pet cat is called Fluffy.",
                ).also { stage++ }

            18 -> playerl(FaceAnim.FRIENDLY, "I think I can remember all that.").also { stage++ }
            19 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Just remember those points when you speak to Dron. He's nearby, so you should still be able to find him.",
                ).also { stage = END_DIALOGUE }

            20 -> npcl(FaceAnim.FRIENDLY, "Glad I could help.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BLANIN_2940)
    }
}
