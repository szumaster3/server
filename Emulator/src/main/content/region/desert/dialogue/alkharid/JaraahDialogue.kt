package content.region.desert.dialogue.alkharid

import core.api.animate
import core.api.getStatLevel
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.NPCs

@Initializable
class JaraahDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hi!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npcl(FaceAnim.ANNOYED, "What? Can't you see I'm busy?!").also { stage++ }
            1 ->
                showTopics(
                    Topic("Can you heal me?", 101),
                    Topic("You must see some gruesome things?", 201),
                    Topic("Why do they call you 'The Butcher'?", 301),
                )

            101 -> {
                end()
                animate(npc!!, Animations.HUMAN_PICKPOCKETING_881)
                if (player!!.skills.lifepoints < getStatLevel(player!!, Skills.HITPOINTS)) {
                    player!!.skills.heal(21)
                    npcl(FaceAnim.FRIENDLY, "There you go!")
                } else {
                    npcl(FaceAnim.FRIENDLY, "Okay, this will hurt you more than it will me.")
                }
            }
            201 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "It's a gruesome business and with the tools they give me it gets mroe gruesome before it gets better!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            301 -> npcl(FaceAnim.HALF_THINKING, "'The Butcher'?").also { stage++ }
            302 -> npcl(FaceAnim.LAUGH, "Ha!").also { stage++ }
            303 -> npcl(FaceAnim.HALF_ASKING, "Would you like me to demonstrate?").also { stage++ }
            304 -> player(FaceAnim.AFRAID, "Er...I'll give it a miss, thanks.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return JaraahDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JARAAH_962)
    }
}
