package content.region.desert.dialogue.alkharid

import core.api.animate
import core.api.getStatLevel
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.skill.Skills
import core.tools.END_DIALOGUE
import org.rs.consts.Animations

class AlKharidHealDialogue(
    val skipFirst: Boolean,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        if (stage == 0 && skipFirst) stage++
        when (stage) {
            0 -> playerl(FaceAnim.ASKING, "Can you heal me?").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "Of course!").also { stage++ }
            2 -> {
                animate(npc!!, Animations.HUMAN_PICKPOCKETING_881)
                if (player!!.skills.lifepoints < getStatLevel(player!!, Skills.HITPOINTS)) {
                    player!!.skills.heal(21)
                    npcl(FaceAnim.FRIENDLY, "There you go!")
                } else {
                    npcl(FaceAnim.FRIENDLY, "You look healthy to me!")
                }
                stage = END_DIALOGUE
            }
        }
    }
}
