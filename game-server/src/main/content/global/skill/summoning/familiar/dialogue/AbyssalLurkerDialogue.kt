package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Abyssal lurker dialogue.
 */
@Initializable
class AbyssalLurkerDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = AbyssalLurkerDialogue(player)

    /**
     * Instantiates a new Abyssal lurker dialogue.
     */
    constructor()

    /**
     * Instantiates a new Abyssal lurker dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 4).toInt()) {
            0 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Djrej gf'ig sgshe...")
                stage = 0
            }

            1 -> {
                npcl(FaceAnim.CHILD_NORMAL, "To poshi v'kaa!")
                stage = 1
            }

            2 -> {
                npcl(FaceAnim.CHILD_NORMAL, "G-harrve shelmie?")
                stage = 2
            }

            3 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Jehifk i'ekfh skjd.")
                stage = 3
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.HALF_ASKING, "What? Are we in danger, or something?")
                stage = END_DIALOGUE
            }

            1 -> {
                playerl(FaceAnim.HALF_ASKING, "What? Is that even a language?")
                stage = END_DIALOGUE
            }

            2 -> {
                playerl(FaceAnim.HALF_ASKING, "What? Do you want something?")
                stage = END_DIALOGUE
            }

            3 -> {
                playerl(FaceAnim.HALF_ASKING, "What? Is there somebody down an old well, or something?")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ABYSSAL_LURKER_6820, NPCs.ABYSSAL_LURKER_6821)
}
