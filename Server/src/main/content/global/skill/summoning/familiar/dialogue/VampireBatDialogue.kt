package content.global.skill.summoning.familiar.dialogue

import core.api.inBorders
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Vampire Bat familiar dialogue.
 */
@Initializable
class VampireBatDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = VampireBatDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        branch = if (inBorders(player, 3139, 9535, 3306, 9657)) 0 else (1..3).random()

        stage = when (branch) {
            0 -> 0
            1 -> 3
            2 -> 4
            3 -> 5
            else -> 0
        }

        when (branch) {
            0 -> npc(FaceAnim.CHILD_NORMAL, "Ze creatures ov ze dark; vat vonderful music zey make.")
            1 -> npc(FaceAnim.CHILD_NORMAL, "You're vasting all that blood, can I have some?")
            2 -> npc(FaceAnim.CHILD_NORMAL, "Ven are you going to feed me?")
            3 -> npc(FaceAnim.CHILD_NORMAL, "Ven can I eat somethink?")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { stage++; playerl(FaceAnim.FRIENDLY, "Riiight.") }
                1 -> { stage++; npc(FaceAnim.CHILD_NORMAL, "I like it down here. Let's stay and eat moths!") }
                2 -> stage = END_DIALOGUE.also { playerl(FaceAnim.FRIENDLY, "I think I'll pass, thanks.") }
            }

            1 -> stage = END_DIALOGUE.also { playerl(FaceAnim.FRIENDLY, "No!") }
            2 -> stage = END_DIALOGUE.also { playerl(FaceAnim.FRIENDLY, "Well for a start, I'm not giving you any of my blood.") }
            3 -> stage = END_DIALOGUE.also { playerl(FaceAnim.FRIENDLY, "Just as soon as I find something to attack.") }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.VAMPIRE_BAT_6835, NPCs.VAMPIRE_BAT_6836)
}