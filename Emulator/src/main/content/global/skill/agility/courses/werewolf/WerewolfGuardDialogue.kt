package content.global.skill.agility.courses.werewolf

import core.api.inEquipment
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class WerewolfGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FaceAnim.HALF_ASKING, "What's beneath the trapdoor?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                if (!inEquipment(player, Items.RING_OF_CHAROS_4202, 1) ||
                    !inEquipment(
                        player,
                        Items.RING_OF_CHAROSA_6465,
                        1,
                    )
                ) {
                    npcl(
                        FaceAnim.CHILD_NORMAL,
                        "That's none of your business, human, and I'll never tell.",
                    ).also { stage++ }
                } else {
                    npcl(
                        FaceAnim.CHILD_NORMAL,
                        "It's an agility course designed for lycanthropes like ourselves, my friend.",
                    ).also { stage = END_DIALOGUE }
                }

            1 -> playerl(FaceAnim.HALF_GUILTY, "Oh, come on - I'm only curious.").also { stage++ }
            2 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "If it wasn't my duty to stand here and guard our agility course from the likes of you, I would be relieving you of your life right now.",
                ).also { stage++ }

            3 -> playerl(FaceAnim.HALF_GUILTY, "So it's an agility course, then?").also { stage++ }
            4 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "No ... yes ... oh blast - you didn't hear me say anything, right?",
                ).also { stage++ }

            5 -> playerl(FaceAnim.HALF_GUILTY, "No problem. Can I come in?").also { stage++ }
            6 -> npcl(FaceAnim.CHILD_NORMAL, "No, human - it's werewolves only.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WEREWOLF_1665)
    }
}
