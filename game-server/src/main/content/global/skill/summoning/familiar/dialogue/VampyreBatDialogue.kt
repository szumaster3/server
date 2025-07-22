package content.global.skill.summoning.familiar.dialogue

import core.api.inBorders
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Vampyre bat dialogue.
 */
@Initializable
class VampyreBatDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = VampyreBatDialogue(player)

    /**
     * Instantiates a new Vampyre bat dialogue.
     */
    constructor()

    /**
     * Instantiates a new Vampyre bat dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (inBorders(player, 3139, 9535, 3306, 9657)) {
            npc(FaceAnim.CHILD_NORMAL, "Ze creatures ov ze dark; vat vonderful music zey make.")
            stage = 0
            return true
        }
        val randomChoice = (Math.random() * 3).toInt()
        when (randomChoice) {
            0 -> {
                npc(FaceAnim.CHILD_NORMAL, "You're vasting all that blood, can I have some?")
                stage = 3
            }

            1 -> {
                npc(FaceAnim.CHILD_NORMAL, "Ven are you going to feed me?")
                stage = 4
            }

            2 -> {
                npc(FaceAnim.CHILD_NORMAL, "Ven can I eat somethink?")
                stage = 5
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
                playerl(FaceAnim.FRIENDLY, "Riiight.")
                stage++
            }

            1 -> {
                npc(FaceAnim.CHILD_NORMAL, "I like it down here. Let's stay and eat moths!")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "I think I'll pass, thanks.")
                stage = END_DIALOGUE
            }

            3 -> {
                playerl(FaceAnim.FRIENDLY, "No!")
                stage = END_DIALOGUE
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Well for a start, I'm not giving you any of my blood.")
                stage = END_DIALOGUE
            }

            5 -> {
                playerl(FaceAnim.FRIENDLY, "Just as soon as I find something to attack.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.VAMPIRE_BAT_6835, NPCs.VAMPIRE_BAT_6836)
}
