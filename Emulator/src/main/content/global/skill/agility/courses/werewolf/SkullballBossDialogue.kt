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
class SkullballBossDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!inEquipment(player, Items.RING_OF_CHAROS_4202, 1) || !inEquipment(player, Items.RING_OF_CHAROSA_6465, 1)) {
            npc(FaceAnim.CHILD_NORMAL, "Grrr - you don't belong in here, human!").also { stage = END_DIALOGUE }
        } else {
            options(
                "I would like to do the skullball course.",
                "What are the instructions for the skullball course?",
            ).also {
                stage++
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "I would like to do the skullball course.").also { stage = 5 }
                    2 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "What are the instructions for using the skullball course?",
                        ).also { stage++ }
                }
            1 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "The skullball comes out of one of these four spawnholes. Just kick the ball through the middle of each goal, through the skeleton's feet.",
                ).also {
                    stage++
                }
            2 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "There are 10 goals, which you must complete in order, and one final goal.",
                ).also {
                    stage++
                }
            3 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "An arrow will point to your ball, just in case lots of people are using the course at the same time as yourself.",
                ).also {
                    stage++
                }
            4 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "The better your time, the more agility XP you will be awarded. The timer starts when you score your first goal.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            5 ->
                when (buttonId) {
                    1 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "What are the instructions for using the skullball course?",
                        ).also {
                            stage =
                                1
                        }
                    2 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "I seem to have lost my ball - can I have another one?",
                        ).also { stage++ }
                    3 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "I give up, I can't do it - take my ball away.",
                        ).also { stage = 7 }
                }
            6 ->
                npcl(
                    FaceAnim.CHILD_NORMAL,
                    "No problem, here's another one. You'll have to start from the beginning again, but the timer will be restarted too.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            7 -> npcl(FaceAnim.CHILD_NORMAL, "Oh dear, such a defeatist.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SKULLBALL_BOSS_1660)
    }
}
