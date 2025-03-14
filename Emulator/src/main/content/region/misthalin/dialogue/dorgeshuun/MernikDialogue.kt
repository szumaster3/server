package content.region.misthalin.dialogue.dorgeshuun

import core.api.sendNPCDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MernikDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when ((1..5).random()) {
            1 ->
                sendNPCDialogue(
                    player,
                    NPCs.CAVE_GOBLIN_CHILD_5816,
                    "Haha! You look funny!",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    stage =
                        1
                }
            2 ->
                sendNPCDialogue(player, NPCs.CAVE_GOBLIN_CHILD_5816, "You smell!", FaceAnim.OLD_NORMAL).also {
                    stage =
                        3
                }
            3 ->
                sendNPCDialogue(
                    player,
                    NPCs.CAVE_GOBLIN_CHILD_5816,
                    "Are you really from the surface?",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    stage =
                        4
                }
            4 ->
                sendNPCDialogue(
                    player,
                    NPCs.CAVE_GOBLIN_CHILD_5816,
                    "Are you ${player.username}? Did you help Zanik save the city?",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    stage =
                        5
                }
            5 -> sendNPCDialogue(player, NPCs.CAVE_GOBLIN_CHILD_5816, "Shan't!", FaceAnim.OLD_NORMAL).also { stage++ }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 ->
                npc(FaceAnim.OLD_NORMAL, "I'm sure you look just as funny to the surface-dweller!").also {
                    stage =
                        END_DIALOGUE
                }
            2 -> npcl(FaceAnim.OLD_NORMAL, "There's no need to be rude!").also { stage++ }
            3 -> npcl(FaceAnim.OLD_NORMAL, "I'm sorry about that, surface-dweller.").also { stage = END_DIALOGUE }
            4 -> playerl(FaceAnim.FRIENDLY, "Yes I am.").also { stage = END_DIALOGUE }
            5 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Just because " + (if (player.isMale) "he's" else "she's") + " a surface-dweller, doesn't mean " +
                        (if (player.isMale) "he's" else "she's") +
                        " ${player.username}! There are lots of surface-dwellers!",
                ).also { stage++ }
            6 -> playerl(FaceAnim.FRIENDLY, "Actually I am ${player.username}.").also { stage++ }
            7 -> npcl(FaceAnim.OLD_NORMAL, "${player.username}? Really?").also { stage++ }
            8 ->
                npcl(FaceAnim.OLD_NORMAL, "Zanik told me about you! I'm so pleased to meet you!").also {
                    stage =
                        END_DIALOGUE
                }
            9 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Well don't blame me if he doesn't want to talk to you either!",
                ).also { stage++ }
            10 -> npcl(FaceAnim.OLD_NORMAL, "Sorry about that.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MERNIK_5782)
    }
}
