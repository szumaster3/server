package content.region.kandarin.dialogue.plaguecity

import content.region.kandarin.quest.biohazard.dialogue.MournerHeadquartersDialogue
import content.region.kandarin.quest.elena.dialogue.HeadMournerDialogue
import content.region.kandarin.quest.elena.dialogue.MournerWestDialogue
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MournerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when (npc.id) {
            NPCs.MOURNER_347, NPCs.MOURNER_348, NPCs.MOURNER_357, NPCs.MOURNER_369, NPCs.MOURNER_371, NPCs.MOURNER_370 ->
                openDialogue(
                    player,
                    MournerHeadquartersDialogue(),
                )
            NPCs.HEAD_MOURNER_716 -> openDialogue(player, HeadMournerDialogue())
            NPCs.MOURNER_717, NPCs.MOURNER_3216 -> openDialogue(player, MournerWestDialogue())
            NPCs.MOURNER_718 -> npcl(FaceAnim.HALF_ASKING, "What are you up to?")
            NPCs.MOURNER_719 -> playerl(FaceAnim.FRIENDLY, "Hello.").also { stage = 4 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.NEUTRAL, "Nothing.").also { stage++ }
            1 -> npcl(FaceAnim.THINKING, "I don't trust you.").also { stage++ }
            2 -> playerl(FaceAnim.NEUTRAL, "You don't have to.").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "If I find you attempting to cross the wall I'll make sure you never return.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            4 -> npcl(FaceAnim.NEUTRAL, "Good day. Are you in need of assistance?").also { stage++ }
            5 -> playerl(FaceAnim.FRIENDLY, "Yes, but I don't think you can help.").also { stage++ }
            6 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "You will be surprised at how much help the brute force of the Guard can be.",
                ).also {
                    stage++
                }
            7 ->
                playerl(FaceAnim.FRIENDLY, "Well I'll be sure to ask if I'm in need of some muscle.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.MOURNER_347,
            NPCs.MOURNER_348,
            NPCs.MOURNER_357,
            NPCs.MOURNER_369,
            NPCs.MOURNER_371,
            NPCs.MOURNER_370,
            NPCs.HEAD_MOURNER_716,
            NPCs.MOURNER_717,
            NPCs.MOURNER_718,
            NPCs.MOURNER_719,
            NPCs.MOURNER_3216,
        )
    }
}
