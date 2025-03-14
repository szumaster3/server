package content.global.skill.agility.courses.werewolf

import core.api.face
import core.api.findLocalNPC
import core.api.inEquipment
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class AgilityBossDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!inEquipment(player, Items.RING_OF_CHAROS_4202, 1) || !inEquipment(player, Items.RING_OF_CHAROSA_6465, 1)) {
            npc(FaceAnim.CHILD_NORMAL, "Grrr - you don't belong in here, human!").also { stage = END_DIALOGUE }
        } else {
            player(FaceAnim.ASKING, "How do I use the agility course?").also { stage = 0 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "I'll throw you a stick, which you need to fetch as",
                    "quickly as possible, from the area beyond the pipes.",
                ).also {
                    stage++
                }
            1 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Be wary of the deathslide - you must hang by your",
                    "teeth, and if your strength is not up to the job you will",
                    "fall into a pit of spikes. Also, I would advise not",
                    "carrying too much extra weight.",
                ).also {
                    stage++
                }
            2 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Bring the stick back to the werewolf waiting at the end",
                    "of the death slide to get your agility bonus.",
                ).also {
                    stage++
                }
            3 ->
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "I will throw your stick as soon as you jump onto the",
                    "first stone.",
                ).also { stage++ }
            4 -> {
                end()
                findLocalNPC(player, NPCs.AGILITY_BOSS_1661)?.let { face(it, Location(3540, 9876, 0)) }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AGILITY_BOSS_1661)
    }
}
